package com.example.projectmppl.fragment.metode;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.activity.MainActivity;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.adapter.ListNonOrganikAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeAntarFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ListKantongAdapter listKantongAdapter = new ListKantongAdapter();

    private ArrayList<Kantong>listKey;
    private ArrayList<KantongNonOrganik>kantongNonOrganiks;
    private ArrayList<Kantong>inputPakaian;
    private int totalPoint;
    private ArrayList<String> list;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.btn_request)
    Button btnRequest;
    @BindView(R.id.spinner)
    Spinner lokasiSpinner;




    public MetodeAntarFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metode_antar, container, false);
        ButterKnife.bind(this,view);

        hideProgress();
        initFirebase();

        btnRequest.setOnClickListener(this::onClick);
        return view;

    }



    private void addTransaksi(Transaksi transaksi){
        hideProgress();
        firebaseDatabase
                .getReference()
                .child("transaksipenukaransampah")
                .child(transaksi.getIdPenukar().replaceAll("\\.", "_"))
                .push()
                .setValue(transaksi)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(), KantongActivity.class);
                        intent.putExtra("saveData","removeData");
                        startActivity(intent);
                    }
                });

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

//    private void removeData(){
//        firebaseDatabase
//                .getReference()
//                .child("kantong")
//                .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"))
//                .removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent intent = new Intent(getActivity(), KantongActivity.class);
//                        intent.putExtra("saveData","removeData");
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                     @Override
//                     public void onFailure(@NonNull Exception e) {
//
//                     }
//                });
//    }

    private void loadDataFirebase() {
        initFirebase();
        String lokasi = lokasiSpinner.getSelectedItem().toString().trim();
        String username = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        showProgress();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();

        kantongNonOrganiks = new ArrayList<>();
        listKey = new ArrayList<>();
        inputPakaian = new ArrayList<>();
        totalPoint = 0;

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null){

                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("elektronik").getChildren()) {
                    Kantong kantong = dataItem.getValue(Kantong.class);
                    listKey.add(kantong);
                    totalPoint = totalPoint+kantong.getJumlahPoint();
                }

                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("nonOrganik").getChildren()) {
                    KantongNonOrganik kantongNonOrganik = dataItem.getValue(KantongNonOrganik.class);
                    kantongNonOrganiks.add(kantongNonOrganik);
                    totalPoint = totalPoint+kantongNonOrganik.getJumlahPoint();
                }
                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("pakaian").getChildren()) {
                    Kantong kantongPakaian = dataItem.getValue(Kantong.class);
                    inputPakaian.add(kantongPakaian);
                    totalPoint = totalPoint+kantongPakaian.getJumlahPoint();
                }

                if (totalPoint != 0){
                    Transaksi transaksi = new Transaksi("url", listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks,inputPakaian);
                    addTransaksi(transaksi);
                }
            }
        });

    }



    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Tag", "FragmentAntar.onPause() has been called.");
        if (listKey != null && kantongNonOrganiks !=null && inputPakaian!= null && list != null) {
            listKey.clear();
            kantongNonOrganiks.clear();
            inputPakaian.clear();
            list.clear();
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == btnRequest){
            loadDataFirebase();
        }

    }
}

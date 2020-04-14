package com.example.projectmppl.fragment.metode;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.example.projectmppl.model.Transaksi;
import com.google.firebase.auth.FirebaseAuth;
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
public class MetodeAntarFragment extends Fragment {

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

    public MetodeAntarFragment(ArrayList<Kantong>kantongs,ArrayList<KantongNonOrganik>kantongNonOrganiks,ArrayList<Kantong>inputPakaian,int totalPoint,ArrayList<String> listKey) {
        this.listKey = kantongs;
        this.kantongNonOrganiks = kantongNonOrganiks;
        this.inputPakaian = inputPakaian;
        this.totalPoint = totalPoint;
        this.list = listKey;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metode_antar, container, false);
        ButterKnife.bind(this,view);

        hideProgress();

        sendDataConstructor(listKey,  kantongNonOrganiks,inputPakaian, totalPoint, list);
        initFirebase();
        return view;

    }


    private void addTransaksi(Transaksi transaksi){
        hideProgress();
        databaseReference
                .child(transaksi.getIdPenukar().replaceAll("\\.", "_"))
                .push()
                .setValue(transaksi);
        removeData(transaksi);

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
    }

    private void removeData(Transaksi transaksi){
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
        databaseReference2.child(transaksi.getIdPenukar().replaceAll("\\.", "_")).removeValue();
        Intent intent = new Intent(getActivity(), KantongActivity.class);
        intent.putExtra("saveData","removeData");
        startActivity(intent);
    }

    private void sendDataConstructor(ArrayList<Kantong> listKey, ArrayList<KantongNonOrganik> kantongNonOrganiks, ArrayList<Kantong> inputPakaian, int totalPoint, ArrayList<String> list){
        initFirebase();
        String lokasi = lokasiSpinner.getSelectedItem().toString().trim();
        String username = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (totalPoint != 0){
            btnRequest.setOnClickListener(view -> {
                showProgress();
                Transaksi transaksi = new Transaksi("url", listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks,inputPakaian);
                addTransaksi(transaksi);
            });
        }
    }

    public void sendData(ArrayList<Kantong> listKey, ArrayList<KantongNonOrganik> kantongNonOrganiks,ArrayList<Kantong>inputPakaian, int totalPoint, ArrayList<String> list){
        this.listKey = listKey;
        this.kantongNonOrganiks = kantongNonOrganiks;
        this.inputPakaian = inputPakaian;
        this.totalPoint = totalPoint;
        this.list =list;

        initFirebase();
        String lokasi = lokasiSpinner.getSelectedItem().toString().trim();
        String username = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (totalPoint != 0){
            btnRequest.setOnClickListener(view -> {
                showProgress();
                Transaksi transaksi = new Transaksi("url", listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks,inputPakaian);
                addTransaksi(transaksi);
            });
        }
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
}

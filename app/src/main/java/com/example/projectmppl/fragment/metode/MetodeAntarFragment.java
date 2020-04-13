package com.example.projectmppl.fragment.metode;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeAntarFragment extends Fragment {
    public Spinner lokasiSpinner;
    Button btnRequest;
    private TextView textView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseDatabase firebaseDatabase;
    private ListKantongAdapter listKantongAdapter = new ListKantongAdapter();

    private MetodeAntarFragmentListener listener;

    private ArrayList<Kantong>listKey;
    private ArrayList<KantongNonOrganik>kantongNonOrganiks;
    private ArrayList<Kantong>inputPakaian;
    private int totalPoint;
    private ArrayList<String> list;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    public interface  MetodeAntarFragmentListener{
        void onInputKantongFragmentSent (String removeData);
    }

    public MetodeAntarFragment() {
        // Required empty public constructor
    }

    public MetodeAntarFragment(ArrayList<Kantong>kantongs,ArrayList<KantongNonOrganik>kantongNonOrganiks,ArrayList<Kantong>inputPakaian,int totalPoint,ArrayList<String> listKey) {
        this.listKey = kantongs;
        this.kantongNonOrganiks = kantongNonOrganiks;
        this.inputPakaian = inputPakaian;
        this.totalPoint = totalPoint;
        this.list = listKey;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metode_antar, container, false);
        btnRequest = view.findViewById(R.id.btn_request);
        lokasiSpinner = (Spinner) view.findViewById(R.id.spinner);
        textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(String.valueOf(totalPoint));
        ButterKnife.bind(this,view);

        hideProgress();

        sendDataConstructor(listKey,  kantongNonOrganiks,inputPakaian, totalPoint, list);
        initFirebase();
        return view;

    }


    public void addTransaksi(Transaksi transaksi){
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
        databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
        databaseReference2.child(transaksi.getIdPenukar().replaceAll("\\.", "_")).removeValue();
//        listener.onInputKantongFragmentSent("removeData");
        Intent intent = new Intent(getActivity(), KantongActivity.class);
        intent.putExtra("saveData","removeData");
        startActivity(intent);
    }

    public void sendDataConstructor(ArrayList<Kantong> listKey, ArrayList<KantongNonOrganik> kantongNonOrganiks,ArrayList<Kantong>inputPakaian, int totalPoint, ArrayList<String> list){
        initFirebase();
        String lokasi = lokasiSpinner.getSelectedItem().toString().trim();
        String username = firebaseAuth.getCurrentUser().getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        textView.setText(String.valueOf(totalPoint));

        if (totalPoint != 0){
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    Transaksi transaksi = new Transaksi("url", listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks,inputPakaian);
                    addTransaksi(transaksi);
                }
            });
        }
    }

    public void sendData(ArrayList<Kantong> listKey, ArrayList<KantongNonOrganik> kantongNonOrganiks,ArrayList<Kantong>inputPakaian, int totalPoint, ArrayList<String> list){
        initFirebase();
        String lokasi = lokasiSpinner.getSelectedItem().toString().trim();
        String username = firebaseAuth.getCurrentUser().getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        textView.setText(String.valueOf(totalPoint));

        if (totalPoint != 0){
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    Transaksi transaksi = new Transaksi("url", listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks,inputPakaian);
                    addTransaksi(transaksi);
                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MetodeAntarFragmentListener){
            listener = (MetodeAntarFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Tag", "FragmentAntar.onPause() has been called.");
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);


    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);


    }
}

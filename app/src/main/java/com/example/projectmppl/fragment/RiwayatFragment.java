package com.example.projectmppl.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.activity.MainActivity;
import com.example.projectmppl.adapter.ListRiwayatAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatFragment extends Fragment {


    private List<Transaksi> listData;
    private List<String>keyTransaksi;
    @BindView(R.id.rv_riwayat)
    RecyclerView recyclerViewData;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ListRiwayatAdapter listRiwayatAdapter;

    public RiwayatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        ButterKnife.bind(this, view);
        loadDataFirebase();
        initFirebase();
        return view;
    }


    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
        firebaseDatabase = FirebaseDatabase.getInstance();
        recyclerViewData.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void loadDataFirebase(){
        showProgress();
        listData = new ArrayList<>();
        keyTransaksi = new ArrayList<>();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("transaksipenukaransampah")
                .child(currentUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            hideProgress();
                            for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                                Transaksi transaksi = dataItem.getValue(Transaksi.class);
                                listData.add(transaksi);
                                keyTransaksi.add(dataItem.getKey());
                            }
                            listRiwayatAdapter = new ListRiwayatAdapter(listData,keyTransaksi,getContext());
                            recyclerViewData.setAdapter(listRiwayatAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }



    @Override
    public void onPause() {
        super.onPause();
        listData.clear();
        keyTransaksi.clear();
        listRiwayatAdapter = new ListRiwayatAdapter(listData,keyTransaksi,getActivity());
        recyclerViewData.setAdapter(listRiwayatAdapter);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

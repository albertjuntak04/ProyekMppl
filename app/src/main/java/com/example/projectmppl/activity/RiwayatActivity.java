package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListRiwayatAdapter;
import com.example.projectmppl.adapter.ListRiwayatKuponAdapter;
import com.example.projectmppl.model.RiwayatKupon;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatActivity extends AppCompatActivity {

    @BindView(R.id.rv_riwayat_kupon)
    RecyclerView rvRiwayat;
    @BindView(R.id.poin_user)
    TextView poinUser;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ListRiwayatKuponAdapter listRiwayatAdapter;
    private ArrayList<RiwayatKupon>riwayatKupons;
    private ArrayList<String> listKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        ButterKnife.bind(this);
        initRecycleView();
        initFirebase();
        loadDataRiwayat();
        loadPoinUser();
    }

    private void loadDataRiwayat(){
        showProgress();
        riwayatKupons = new ArrayList<>();
        listKey = new ArrayList<>();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("transaksikupon")
                .child(currentUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                                RiwayatKupon riwayatKupon = dataItem.getValue(RiwayatKupon.class);
                                riwayatKupons.add(riwayatKupon);
                                listKey.add(dataItem.getKey());
                            }
                            hideProgress();
                            listRiwayatAdapter = new ListRiwayatKuponAdapter(riwayatKupons,RiwayatActivity.this,listKey);
                            rvRiwayat.setAdapter(listRiwayatAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void initRecycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvRiwayat.setLayoutManager(linearLayoutManager);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void loadPoinUser(){
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataUser();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        liveData.observe(this, dataSnapshot -> {
            String poin = dataSnapshot.child(currentUser).child("poin").getValue().toString();
            try {
                poinUser.setText(poin);
            }
            catch (Exception e){

            }
        });
    }
}

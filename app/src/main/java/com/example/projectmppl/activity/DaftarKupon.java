package com.example.projectmppl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.adapter.ListKuponAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.Kupon;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaftarKupon extends AppCompatActivity {
    @BindView(R.id.rv_kupon)
    RecyclerView rvKupon;
    @BindView(R.id.rv_indomaret)
    RecyclerView rvIndomaret;
    @BindView(R.id.rv_koperasi)
    RecyclerView rvKoperasi;
    @BindView(R.id.rv_keasramaan)
    RecyclerView rvKeasramaan;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.poin_user)
    TextView poinUser;
    ListKuponAdapter listKuponAdapter;
    ListKuponAdapter listKuponKoperasi;
    ListKuponAdapter listKuponKemahasiswaan;
    ArrayList<Kupon> kuponKu;
    ArrayList<Kupon> koperasi;
    ArrayList<Kupon> kemahasiswaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kupon);
        ButterKnife.bind(this);
        initRecycleView();
        loadDataIndomaret();
        loadDataKeasramaan();
        loadDataKoperasi();
        loadPoinUser();
    }

    public void initRecycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutIndomaret = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutKoperasi = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutKeasramaan = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        rvKupon.setLayoutManager(linearLayoutManager);
        rvIndomaret.setLayoutManager(linearLayoutIndomaret);
        rvKoperasi.setLayoutManager(linearLayoutKoperasi);
        rvKeasramaan.setLayoutManager(linearLayoutKeasramaan);
    }

    public void loadDataIndomaret(){
        showProgress();
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getDaftarKupon();
        kuponKu = new ArrayList<>();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot!=null){
                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                    try {
                        Kupon kupon = dataItem.getValue(Kupon.class);
                        if (kupon.getJeniskupon().equals("Indomaret")){
                            kuponKu.add(kupon);
                        }
                    }catch (Exception e){

                    }
                }
                listKuponAdapter = new ListKuponAdapter(kuponKu,this);
                rvIndomaret.setAdapter(listKuponAdapter);
            }
        });
    }

    public void loadDataKoperasi(){
        showProgress();
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getDaftarKupon();
        koperasi = new ArrayList<>();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot!=null){
                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                    try {
                        Kupon kupon = dataItem.getValue(Kupon.class);
                        if (kupon.getJeniskupon().equals("Koperasi")){
                            koperasi.add(kupon);
                        }
                    }catch (Exception e){

                    }
                }
                listKuponKoperasi = new ListKuponAdapter(koperasi,this);
                rvKoperasi.setAdapter(listKuponKoperasi);
            }
        });
    }


    public void loadDataKeasramaan(){
        showProgress();
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getDaftarKupon();
        kemahasiswaan = new ArrayList<>();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot!=null){
                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                    try {
                        Kupon kupon = dataItem.getValue(Kupon.class);
                        if (kupon.getJeniskupon().equals("Kemahasiswaan")){
                            kemahasiswaan.add(kupon);
                        }
                    }catch (Exception e){

                    }
                }
                listKuponKemahasiswaan = new ListKuponAdapter(kemahasiswaan,this);
                rvKeasramaan.setAdapter(listKuponKemahasiswaan);
            }

        });

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

    @Override
    protected void onPause() {
        super.onPause();
        try {
            kuponKu.clear();
            kemahasiswaan.clear();
            koperasi.clear();
        }catch (Exception e){

        }
    }

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }
}

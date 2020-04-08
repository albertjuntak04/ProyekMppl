package com.example.projectmppl.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.projectmppl.data.FirebaseQueryLiveData;
import com.example.projectmppl.model.Kantong;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class ViewModelFirebase extends androidx.lifecycle.ViewModel {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance()
            .getReference()
            .child("kantong");

    private static final DatabaseReference kondisi = FirebaseDatabase.getInstance()
            .getReference()
            .child("sampah");

    private static final DatabaseReference databaseUser = FirebaseDatabase.getInstance()
            .getReference()
            .child("pengguna");

    private static final DatabaseReference dataTransaksi = FirebaseDatabase.getInstance()
            .getReference()
            .child("transaksipenukaransampah");



    private final FirebaseQueryLiveData liveDataKantong = new FirebaseQueryLiveData(mDatabase);
    private final FirebaseQueryLiveData liveDataKondisi = new FirebaseQueryLiveData(kondisi);
    private final FirebaseQueryLiveData liveDataDataUser = new FirebaseQueryLiveData(databaseUser);

    private final FirebaseQueryLiveData liveDataTransaksi = new FirebaseQueryLiveData(dataTransaksi);

    @NonNull
    public LiveData<DataSnapshot> getdataSnapshotLiveData(){
        return liveDataKantong;
    }

    @NonNull
    public LiveData<DataSnapshot> getdataKondisi(){
        return liveDataKondisi;
    }

    @NonNull
    public LiveData<DataSnapshot> getdataUser(){
        return liveDataDataUser;
    }

    @NonNull
    public LiveData<DataSnapshot> getdataTransaksi(){
        return liveDataTransaksi;
    }
}

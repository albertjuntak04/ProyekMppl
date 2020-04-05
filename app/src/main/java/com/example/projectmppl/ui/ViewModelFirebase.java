package com.example.projectmppl.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.projectmppl.data.FirebaseQueryLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewModelFirebase extends androidx.lifecycle.ViewModel {

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance()
            .getReference()
            .child("kantong")
            .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "_"))
            .child("data");

    private static final DatabaseReference kondisi = FirebaseDatabase.getInstance()
            .getReference()
            .child("sampah");
    private final FirebaseQueryLiveData liveDataKantong = new FirebaseQueryLiveData(mDatabase);
    private final FirebaseQueryLiveData liveDataKondisi = new FirebaseQueryLiveData(kondisi);

    @NonNull
    public LiveData<DataSnapshot> getdataSnapshotLiveData(){
        return liveDataKantong;
    }

    @NonNull
    public LiveData<DataSnapshot> getdataKondisi(){
        return liveDataKondisi;
    }
}
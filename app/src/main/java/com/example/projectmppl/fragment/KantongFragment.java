package com.example.projectmppl.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import android.widget.TextView;


import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.adapter.ListNonOrganikAdapter;
import com.example.projectmppl.fragment.metode.MetodeJemputFragment;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class KantongFragment extends Fragment implements ListKantongAdapter.OnRemovedListener,ListNonOrganikAdapter.OnRemovedNonOrganikListener {
    private static final String TAG = "KantongActivity";
    public static String KEY_ACTIVITY = "DaftarSampah";
    @BindView(R.id.recycleview)
    RecyclerView recyclerViewData;
    @BindView(R.id.recycleviewNonOrganik)
    RecyclerView recyclerViewNonOrganik;
    @BindView(R.id.recycleviewPakaian)
    RecyclerView recyclerViewPakaian;

    private List<Kantong> listData;
    private List<KantongNonOrganik> listDataNonOrganik;
    private ArrayList<String> listKeyNonOrganik;
    private ArrayList<Kantong> listPakaian;
    private ArrayList<String> keyElektronik;
    @BindView(R.id.progress)
    ProgressBar progressBar;


    @BindView(R.id.kondisi_kantong)
    TextView kondisiKantong;

    private ListKantongAdapter kantongAdapter;
    private ListKantongAdapter kantongAdapterPakaian;
    private ListNonOrganikAdapter listNonOrganikAdapter;


    public KantongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kantong, container, false);

        ButterKnife.bind(this, view);
        initFirebase();
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataNonOrganik();
    }


    private void loadDataFirebaseElektronik() {
        showProgress();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();

        listData = new ArrayList<>();
        keyElektronik = new ArrayList<>();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null){

                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("elektronik").getChildren()) {
                    Kantong kantong = dataItem.getValue(Kantong.class);
                    listData.add(kantong);
                    keyElektronik.add(dataItem.getKey());
                }
                kantongAdapter = new ListKantongAdapter(listData,keyElektronik,getActivity());
                kantongAdapter.setOnShareClickedListener(this);
                recyclerViewData.setAdapter(kantongAdapter);
                recyclerViewData.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewData.setHasFixedSize(true);
                kantongAdapter.notifyDataSetChanged();
                kondisiKantong.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void loadDataNonOrganik() {
//        showProgress();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();


        listKeyNonOrganik = new ArrayList<>();
        listDataNonOrganik = new ArrayList<>();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null){

//                hideProgress();

                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("nonOrganik").getChildren()) {
                    KantongNonOrganik kantongNonOrganik = dataItem.getValue(KantongNonOrganik.class);
                    listDataNonOrganik.add(kantongNonOrganik);
                    listKeyNonOrganik.add(dataItem.getKey());
                }
                listNonOrganikAdapter = new ListNonOrganikAdapter(listDataNonOrganik,listKeyNonOrganik);
                recyclerViewNonOrganik.setAdapter(listNonOrganikAdapter);
                recyclerViewNonOrganik.setLayoutManager(new LinearLayoutManager(getContext()));
                listNonOrganikAdapter.setDeleteNonOrganikClickedListener(this);
                kondisiKantong.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void loadDataFirebasePakaian() {
        showProgress();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();

        listPakaian = new ArrayList<>();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null){

                hideProgress();

                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("pakaian").getChildren()) {
                    Kantong kantongPakaian = dataItem.getValue(Kantong.class);
                    listPakaian.add(kantongPakaian);

                }

                kantongAdapterPakaian = new ListKantongAdapter(listPakaian,keyElektronik,getActivity());
                kantongAdapter.setOnShareClickedListener(this);
                recyclerViewPakaian.setAdapter(kantongAdapterPakaian);

                kondisiKantong.setVisibility(View.INVISIBLE);
            }
        });

    }



    private void initFirebase() {

//        recyclerViewPakaian.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerViewPakaian.setHasFixedSize(true);
//        recyclerViewNonOrganik.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerViewNonOrganik.setHasFixedSize(true);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Tag", "FragmentA.onDestroyView() has been called.");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Tag", "FragmentA.onResume() has been called.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Tag", "FragmentA.onPause() has been called.");
        if (listData != null && listDataNonOrganik !=null && listPakaian != null) {
            listData.clear();
            listDataNonOrganik.clear();
            listPakaian.clear();
        }

    }

    private void initView(){
        loadDataFirebaseElektronik();
        loadDataFirebasePakaian();
//        loadDataNonOrganik();
    }

    @Override
    public void RemoveClicked(String key,int position) {
            String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
            databaseReference2.child(currentUser)
                    .child("data")
                    .child("elektronik")
                    .child(key)
                    .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            kantongAdapter.removeItem(position);
//                            kantongAdapter.notifyDataSetChanged();
                        }
                    });

    }


    @Override
    public void RemoveNonOrganikClicked(String key, int position) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
        databaseReference2.child(currentUser)
                .child("data")
                .child("nonOrganik")
                .child(key)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listNonOrganikAdapter.notifyDataSetChanged();
                        listNonOrganikAdapter.removeItem(position);
                        listDataNonOrganik.clear();
//                        putList(idSampah,idSampahNonOrganik,listPakaian,totalPoint,listKey);
                    }
                });

    }

}


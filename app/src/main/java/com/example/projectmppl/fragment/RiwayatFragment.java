package com.example.projectmppl.fragment;


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
import com.example.projectmppl.adapter.ListRiwayatAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.Transaksi;
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
public class RiwayatFragment extends Fragment implements ListRiwayatAdapter.OnRemovedListener {


    private List<Transaksi> listData;
    private List<String>keyTransaksi;
    @BindView(R.id.rv_riwayat)
    RecyclerView recyclerViewData;
    @BindView(R.id.progress)
    ProgressBar progressBar;

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

    private void loadDataFirebase() {
        showProgress();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataTransaksi();
        List<String> listKey = new ArrayList<>();
        listData = new ArrayList<>();
        keyTransaksi = new ArrayList<>();
        ArrayList<Kantong> listKantong = new ArrayList<>();
        final int[] index = {0};
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null){
                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).getChildren()) {
                    Transaksi transaksi = dataItem.getValue(Transaksi.class);
                    listData.add(transaksi);
                    keyTransaksi.add(dataItem.getKey());
                }
                listRiwayatAdapter = new ListRiwayatAdapter(listData,keyTransaksi,getContext());
                recyclerViewData.setAdapter(listRiwayatAdapter);
                listRiwayatAdapter.setOnShareClickedListener(this);
            }

        });
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initFirebase() {
        recyclerViewData.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerViewData.setHasFixedSize(true);
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
    public void RemoveClicked( String key,int position) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
        databaseReference2.child(currentUser)
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

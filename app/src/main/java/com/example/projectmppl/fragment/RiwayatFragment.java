package com.example.projectmppl.fragment;


import android.os.Bundle;

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
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.adapter.ListRiwayatAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatFragment extends Fragment {


    private View view;
    private List<Transaksi> listData;
    private List<String> listKey;
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
        view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        ButterKnife.bind(this,view);
        loadDataFirebase();
        initFirebase();

        return view;
    }

    public void loadDataFirebase() {
        showProgress();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataTransaksi();
        listKey = new ArrayList<>();
        listData = new ArrayList<>();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    hideProgress();
                    for (DataSnapshot dataItem : dataSnapshot.child(currentUser).getChildren()) {
                        Transaksi transaksi = dataItem.getValue(Transaksi.class);
                        listKey.add(dataItem.getKey());
                        listData.add(transaksi);
                    }
                    listRiwayatAdapter = new ListRiwayatAdapter(listData);
                    recyclerViewData.setAdapter(listRiwayatAdapter);
                }

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
}

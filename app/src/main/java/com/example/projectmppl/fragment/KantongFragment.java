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
public class KantongFragment extends Fragment {
    private static final String TAG = "KantongActivity";
    public static String KEY_ACTIVITY = "DaftarSampah";
    @BindView(R.id.recycleview)
    RecyclerView recyclerViewData;
    @BindView(R.id.recycleviewNonOrganik)
    RecyclerView recyclerViewNonOrganik;
    @BindView(R.id.recycleviewPakaian)
    RecyclerView recyclerViewPakaian;

    public ViewModelFirebase viewModelFirebase;
    private int someStateValue;
    private final String SOME_VALUE_KEY = "someValueToSave";

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Kantong> listData;
    private List<KantongNonOrganik> listDataNonOrganik;
    private ArrayList<String> listKey;
    private ArrayList<Kantong> idSampah;
    private ArrayList<KantongNonOrganik> idSampahNonOrganik;
    private ArrayList<String>idSampahElektronik;
    private ArrayList<String> idPakaian;
    private ArrayList<Kantong> listPakaian;
    private int totalPoint;
    @BindView(R.id.progress)
    ProgressBar progressBar;


    @BindView(R.id.kondisi_kantong)
    TextView kondisiKantong;

    private  View view;
    private ListKantongAdapter kantongAdapter;
    private ListKantongAdapter kantongAdapterPakaian;
    private ListNonOrganikAdapter listNonOrganikAdapter;
    private MetodeJemputFragment metodeJemputFragment;

    private FragmentElektronikListener listener;

    private LiveData liveData;

    public interface FragmentElektronikListener {
        void onInputKantongFragmentSent(ArrayList<Kantong> input, ArrayList<KantongNonOrganik> kantongNonOrganiks, ArrayList<Kantong> inputPakaian, int totalPoint, ArrayList<String> listKey);

    }


    public KantongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kantong, container, false);

        ButterKnife.bind(this, view);

        initFirebase();
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void loadDataFirebase(String removeData) {
        showProgress();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();
//        totalPoint = 0;
        listKey = new ArrayList<>();
        listData = new ArrayList<>();
        idSampah = new ArrayList<>();
        listPakaian = new ArrayList<>();
        totalPoint = 0;
        listDataNonOrganik = new ArrayList<>();
        idPakaian = new ArrayList<>();
        idSampahNonOrganik = new ArrayList<>();
        idSampahElektronik = new ArrayList<>();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){

                    hideProgress();
                    for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("elektronik").getChildren()) {
                        Kantong kantong = dataItem.getValue(Kantong.class);
                        listData.add(kantong);
                        idSampah.add(kantong);
                        idSampahElektronik.add(kantong.getIdSampah());
                        totalPoint = totalPoint+kantong.getJumlahPoint();
                    }

                    for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("nonOrganik").getChildren()) {
                        KantongNonOrganik kantongNonOrganik = dataItem.getValue(KantongNonOrganik.class);
                        listDataNonOrganik.add(kantongNonOrganik);
                        idSampahNonOrganik.add(kantongNonOrganik);
                        totalPoint = totalPoint+kantongNonOrganik.getJumlahPoint();
                    }

                    for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("pakaian").getChildren()) {
                        Kantong kantongPakaian = dataItem.getValue(Kantong.class);
                        listPakaian.add(kantongPakaian);
                        totalPoint = totalPoint+kantongPakaian.getJumlahPoint();
                    }

                    putList(idSampah,idSampahNonOrganik,listPakaian,totalPoint,listKey);

                    listNonOrganikAdapter = new ListNonOrganikAdapter(listDataNonOrganik);
                    recyclerViewNonOrganik.setAdapter(listNonOrganikAdapter);

                    kantongAdapter = new ListKantongAdapter(listData);
                    kantongAdapterPakaian = new ListKantongAdapter(listPakaian);

                    recyclerViewData.setAdapter(kantongAdapter);
                    recyclerViewPakaian.setAdapter(kantongAdapterPakaian);


                    kondisiKantong.setVisibility(View.INVISIBLE);
                }
            }
        });

    }




    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewData.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewData.setHasFixedSize(true);

        recyclerViewPakaian.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPakaian.setHasFixedSize(true);
        recyclerViewNonOrganik.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNonOrganik.setHasFixedSize(true);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void putList(ArrayList<Kantong> list,ArrayList<KantongNonOrganik>kantongNonOrganiks,ArrayList<Kantong>inputPakaian, int totalPoint,ArrayList<String> listKey) {

        listener.onInputKantongFragmentSent(list,kantongNonOrganiks,inputPakaian,totalPoint,listKey);

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentElektronikListener){
            listener = (FragmentElektronikListener) context;
        }else {
            throw new RuntimeException(context.toString()
            + " must implement FragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void removeData() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubScriber();
    }

    private void unSubScriber(){
        if (viewModelFirebase != null && viewModelFirebase.getdataSnapshotLiveData().hasObservers()){
            viewModelFirebase.getdataSnapshotLiveData().removeObserver((Observer<? super DataSnapshot>) getActivity());
        }
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
        listData.clear();
        listDataNonOrganik.clear();
        listPakaian.clear();

        listNonOrganikAdapter.notifyDataSetChanged();
        kantongAdapter.notifyDataSetChanged();
    }

    public void initView(){
        loadDataFirebase("showData");
    }
}


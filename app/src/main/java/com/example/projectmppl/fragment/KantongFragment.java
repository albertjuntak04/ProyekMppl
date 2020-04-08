package com.example.projectmppl.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.activity.MainActivity;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.fragment.metode.MetodeJemputFragment;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public ViewModelFirebase viewModelFirebase;


    private int someStateValue;
    private final String SOME_VALUE_KEY = "someValueToSave";

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Kantong> listData;
    private ArrayList<String> listKey;
    private ArrayList<String> idSampah;
    private int totalPoint;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.kondisi_kantong)
    TextView kondisiKantong;
    private  View view;
    private ListKantongAdapter kantongAdapter;
    private MetodeJemputFragment metodeJemputFragment;

    private FragmentKantongListener listener;

    private LiveData liveData;


    public interface  FragmentKantongListener{
        void onInputKantongFragmentSent (ArrayList<String> input, int totalPoint, ArrayList<String> listKey);
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
        loadDataFirebase("showData");
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
        totalPoint = 0;
        listKey = new ArrayList<>();
        listData = new ArrayList<>();
        idSampah = new ArrayList<>();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){

                    hideProgress();
                    for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").getChildren()) {
                        Kantong kantong = dataItem.getValue(Kantong.class);
//                        listKey.add(dataItem.getKey());
                        listData.add(kantong);
                        idSampah.add(kantong.getIdSampah());
                        totalPoint = totalPoint+kantong.getJumlahPoint();
                    }
                    putList(idSampah,totalPoint,listKey);

                    kantongAdapter = new ListKantongAdapter(listData);
                    recyclerViewData.setAdapter(kantongAdapter);
                    kondisiKantong.setVisibility(View.INVISIBLE);
                }
            }
        });

//        counter++;
    }


    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewData.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewData.setHasFixedSize(true);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void putList(ArrayList<String> list, int totalPoint,ArrayList<String> listKey) {
            listener.onInputKantongFragmentSent(list,totalPoint,listKey);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentKantongListener){
            listener = (FragmentKantongListener) context;
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
        listData.clear();
        kantongAdapter = new ListKantongAdapter();
        kantongAdapter.notifyDataSetChanged();

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
        kantongAdapter = new ListKantongAdapter(listData);
        recyclerViewData.setAdapter(kantongAdapter);
    }
}


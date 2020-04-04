package com.example.projectmppl.fragment.metode;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.model.Transaksi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeAntarFragment extends Fragment implements View.OnClickListener {
    private View view;
    @BindView(R.id.spinner)
    Spinner lokasiSpinner;
    @BindView(R.id.btn_request)
    Button btnRequest;
    @BindView(R.id.textView2)
    TextView textView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;



    public MetodeAntarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_metode_antar, container, false);
        ButterKnife.bind(this,view);
        btnRequest.setOnClickListener((View.OnClickListener) this);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();
    }

    @Override
    public void onClick(View view) {
        ArrayList<String> list = new ArrayList<>() ;
        int totalPoint  = this.getArguments().getInt("totalPoint");
        list = this.getArguments().getStringArrayList(KantongActivity.KEY_ACTIVITY);
        String lokasi = lokasiSpinner.getSelectedItem().toString();
        String username = firebaseAuth.getCurrentUser().getEmail();
        String metode = "Antar";
        String status = "Request";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (lokasi!=null){
            switch (view.getId()){
                case R.id.btn_request:
                    Transaksi transaksi = new Transaksi("url",list,username,metode,status,datetime,totalPoint,lokasi);
                    addTransaksi(transaksi);
            }
        }else{
            Toast.makeText(getContext(),"Silahkan masukkan lokasi",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //


    public void addTransaksi(Transaksi transaksi){
        databaseReference
                .child(transaksi.getIdPenukar().replaceAll("\\.", "_"))
                .push()
                .setValue(transaksi);

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
    }
}

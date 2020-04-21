package com.example.projectmppl.fragment.metode;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.adapter.ListRiwayatAdapter;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeAntarFragment extends Fragment {
    public Spinner lokasiSpinner;
    Button btnRequest;
    private TextView textView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReferenceRiwayat;
    private FirebaseDatabase firebaseDatabase;
    private ListKantongAdapter listKantongAdapter = new ListKantongAdapter();

    private MetodeAntarFragmentListener listener;

    public interface  MetodeAntarFragmentListener{
        void onInputKantongFragmentSent (String removeData);
    }

    public MetodeAntarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metode_antar, container, false);
        btnRequest = view.findViewById(R.id.btn_request);
        lokasiSpinner = (Spinner) view.findViewById(R.id.spinner);
        textView = (TextView) view.findViewById(R.id.textView2);
        initFirebase();
        return view;

    }


    public void addTransaksi(Transaksi transaksi){
        databaseReference
                .child(transaksi.getIdPenukar().replaceAll("\\.", "_"))
                .push()
                .setValue(transaksi);
        saveRiwayat();
        removeData(transaksi);

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
        databaseReferenceRiwayat  = FirebaseDatabase.getInstance().getReference("riwayat");
    }

    private void removeData(Transaksi transaksi){
        databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
        databaseReference2.child(transaksi.getIdPenukar().replaceAll("\\.", "_")).removeValue();
        listener.onInputKantongFragmentSent("removeData");
        Intent intent = new Intent(getActivity(), KantongActivity.class);
        intent.putExtra("saveData","removeData");
        startActivity(intent);
    }

    public void sendData(ArrayList<Kantong> listKey, ArrayList<KantongNonOrganik> kantongNonOrganiks, int totalPoint, ArrayList<String> list){
        initFirebase();
        String lokasi = lokasiSpinner.getSelectedItem().toString().trim();
        String username = firebaseAuth.getCurrentUser().getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (totalPoint != 0){
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Transaksi transaksi = new Transaksi("url", listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks);
                    addTransaksi(transaksi);

                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MetodeAntarFragmentListener){
            listener = (MetodeAntarFragmentListener) context;
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


    public void saveRiwayat() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataTransaksi();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    for (DataSnapshot dataItem : dataSnapshot.child(currentUser).getChildren()) {
                        databaseReferenceRiwayat
                                .child(currentUser)
                                .setValue(dataItem.getKey());
                    }
                }

            }
        });
    }

}

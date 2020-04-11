package com.example.projectmppl.fragment.metode;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Transaksi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeJemputFragment extends Fragment implements View.OnClickListener {



    private View view;
    @BindView(R.id.input_lokasi)
    EditText lokasiPenjemputan;
    @BindView(R.id.btn_request)
    Button btnRequest;
    @BindView(R.id.tambah_gambar)
    Button btnTambahGambar;
    @BindView(R.id.image_sampah)
    ImageView imageSampah;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    public MetodeJemputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_metode_jemput, container, false);
        ButterKnife.bind(this,view);
        btnRequest.setOnClickListener(this::onClick);
        btnTambahGambar.setOnClickListener(this::onClick);
//        btnTambahGambar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openFileChooser();
//            }
//        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFirebase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tambah_gambar:
                openFileChooser();

//            case R.id.btn_request:
//                ArrayList<String> list = new ArrayList<>() ;
//                int totalPoint  = this.getArguments().getInt("totalPoint");
//                list = this.getArguments().getStringArrayList(KantongActivity.KEY_ACTIVITY);
//                String lokasi = lokasiPenjemputan.getText().toString();
//                String username = firebaseAuth.getCurrentUser().getEmail();
//                String metode = "Penjemputan";
//                String status = "Request";
//                String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                if (lokasi.isEmpty()){
//                    if (view.getId() == R.id.btn_request) {
//                        Transaksi transaksi = new Transaksi("url", list, username, metode, status, datetime, totalPoint, lokasi);
//                        addTransaksi(transaksi);
//                    }
//                }else {
//                    Toast.makeText(getContext(),"Silahkan masukkan lokasi",Toast.LENGTH_SHORT).show();
//                }

        }


    }

    @Override
    public void onStart() {
        super.onStart();
    }

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


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Picasso.get().load(mImageUri).into(imageSampah);
            }
//
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

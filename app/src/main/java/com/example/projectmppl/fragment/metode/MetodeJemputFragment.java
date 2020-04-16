package com.example.projectmppl.fragment.metode;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeJemputFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.input_lokasi)
    EditText lokasiPenjemputan;
    @BindView(R.id.btn_request_jemput)
    Button btnRequest;
    @BindView(R.id.tambah_gambar)
    Button btnTambahGambar;
    @BindView(R.id.image_sampah)
    ImageView imageSampah;
    @BindView(R.id.textView2)
    TextView textView;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private ArrayList<Kantong> listKey;
    private ArrayList<KantongNonOrganik> kantongNonOrganiks;
    private ArrayList<Kantong> inputPakaian;
    private int totalPoint;
    private ArrayList<String> list;

    public MetodeJemputFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metode_jemput, container, false);
        ButterKnife.bind(this, view);
        btnRequest.setOnClickListener(this);
        btnTambahGambar.setOnClickListener(this);
        sendData();
        hideProgress();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFirebase();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tambah_gambar) {
            openFileChooser();
        }


    }

    private void addTransaksi(Transaksi transaksi) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        hideProgress();
        firebaseDatabase
                .getReference()
                .child("transaksipenukaransampah")
                .child(currentUser)
                .push()
                .setValue(transaksi)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(), KantongActivity.class);
                        intent.putExtra("saveData","removeData");
                        intent.putExtra("removeData","remove");
                        startActivity(intent);
                    }
                });

    }


    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("upload");
    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Picasso.get().load(mImageUri).into(imageSampah);
            }

        }
//        this.listKey.clear();
//        this.kantongNonOrganiks.clear();
//        this.inputPakaian.clear();
//        this.list.clear();
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void sendData() {
        initFirebase();
            btnRequest.setOnClickListener(view -> {
                if (mImageUri != null) {
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));
                    fileReference.putFile(mImageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {
//                                                mProgressBar.setProgress(0);
                                }, 500);

                                loadDataFirebase(fileReference.getDownloadUrl().toString());
                            }).addOnFailureListener(e -> {

                    }).addOnProgressListener(taskSnapshot -> {
                        showProgress();
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);

                    });

                } else {
                    Toast.makeText(getContext(), "Tidak ada foto yang dipilih", Toast.LENGTH_SHORT).show();
                }

            });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = Objects.requireNonNull(getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Tag", "FragmentJemput.onResume() has been called.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Tag", "FragmentJemput.onPause() has been called.");

    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void loadDataFirebase(String url) {
        initFirebase();
        String lokasi = "";
        String username = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        showProgress();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();

        kantongNonOrganiks = new ArrayList<>();
        listKey = new ArrayList<>();
        inputPakaian = new ArrayList<>();
        totalPoint = 0;

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                hideProgress();
                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("elektronik").getChildren()) {
                    Kantong kantong = dataItem.getValue(Kantong.class);
                    listKey.add(kantong);
                    totalPoint = totalPoint + kantong.getJumlahPoint();
                }

                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("nonOrganik").getChildren()) {
                    KantongNonOrganik kantongNonOrganik = dataItem.getValue(KantongNonOrganik.class);
                    kantongNonOrganiks.add(kantongNonOrganik);
                    totalPoint = totalPoint + kantongNonOrganik.getJumlahPoint();
                }
                for (DataSnapshot dataItem : dataSnapshot.child(currentUser).child("data").child("pakaian").getChildren()) {
                    Kantong kantongPakaian = dataItem.getValue(Kantong.class);
                    inputPakaian.add(kantongPakaian);
                    totalPoint = totalPoint + kantongPakaian.getJumlahPoint();
                }

                if (totalPoint != 0) {
                    Transaksi transaksi = new Transaksi(url, listKey, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks, inputPakaian);
                    addTransaksi(transaksi);
                }
            }
        });

    }
}

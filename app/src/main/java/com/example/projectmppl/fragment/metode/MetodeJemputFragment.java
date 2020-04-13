package com.example.projectmppl.fragment.metode;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetodeJemputFragment extends Fragment implements View.OnClickListener {



    private View view;
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
    private DatabaseReference databaseReference2;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private ArrayList<Kantong>listKey;
    private ArrayList<KantongNonOrganik>kantongNonOrganiks;
    private ArrayList<Kantong>inputPakaian;
    private int totalPoint;
    private ArrayList<String> list;

    public MetodeJemputFragment(){

    }

    public MetodeJemputFragment(ArrayList<Kantong>kantongs,ArrayList<KantongNonOrganik>kantongNonOrganiks,ArrayList<Kantong>inputPakaian,int totalPoint,ArrayList<String> listKey) {
        this.listKey = kantongs;
        this.kantongNonOrganiks = kantongNonOrganiks;
        this.inputPakaian = inputPakaian;
        this.totalPoint = totalPoint;
        this.list = listKey;
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_metode_jemput, container, false);
        btnRequest = (Button) view.findViewById(R.id.btn_request);
        ButterKnife.bind(this,view);
        btnRequest.setOnClickListener(this::onClick);
        btnTambahGambar.setOnClickListener(this::onClick);
        textView.setText(String.valueOf(totalPoint));
        hideProgress();


        sendData(listKey,kantongNonOrganiks,inputPakaian,totalPoint,list);
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

        removeData(transaksi);

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("transaksipenukaransampah");
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
        if (requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Picasso.get().load(mImageUri).into(imageSampah);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void sendData(ArrayList<Kantong> listKey, ArrayList<KantongNonOrganik> kantongNonOrganiks, ArrayList<Kantong>inputPakaian, int totalPoint, ArrayList<String> list){
        initFirebase();
        String lokasi = lokasiPenjemputan.getText().toString().trim();
        String username = firebaseAuth.getCurrentUser().getEmail();
        String metode = "Antar";
        String status = "Diproses";
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (totalPoint != 0){
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mImageUri !=null){
                        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                + "." + getFileExtension(mImageUri));
                        fileReference.putFile(mImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
//                                                mProgressBar.setProgress(0);
                                            }
                                        }, 500);

                                        Transaksi transaksi = new Transaksi(taskSnapshot.getStorage().getDownloadUrl().toString(), listKey, username, metode, status, datetime, totalPoint, lokasi, kantongNonOrganiks, inputPakaian);
                                        addTransaksi(transaksi);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        showProgress();
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        progressBar.setProgress((int) progress);

                                    }
                                });

                    }
                    else {
                        Toast.makeText(getContext(),"Tidak ada foto yang dipilih",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void removeData(Transaksi transaksi){
        databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
        databaseReference2.child(transaksi.getIdPenukar().replaceAll("\\.", "_")).removeValue();
        Intent intent = new Intent(getActivity(), KantongActivity.class);
        intent.putExtra("saveData","removeData");
        startActivity(intent);
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
        this.listKey.clear();
        this.kantongNonOrganiks.clear();
        this.inputPakaian.clear();
        this.list.clear();
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);


    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);


    }
}

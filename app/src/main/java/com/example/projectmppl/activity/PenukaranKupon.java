package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Kupon;
import com.example.projectmppl.model.RiwayatKupon;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenukaranKupon extends AppCompatActivity {
    @BindView(R.id.img_kupon)
    ImageView imgKupon;
    @BindView(R.id.nama_kupon)
    TextView namaKupon;
    @BindView(R.id.txt_id_kupon)
    TextView idKupon;
    @BindView(R.id.txt_jns_kupon)
    TextView jnsKupon;
    @BindView(R.id.poin)
    TextView poin;
    @BindView(R.id.tukarkan)
    Button btnTukarkan;
    @BindView(R.id.poin_user)
    TextView poinUser;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    int jumlahPoin = 0;
    int updatePoin = 0;

    Kupon kupon;
    public static String KUPON_CLASS = "kupon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penukaran_kupon);
        ButterKnife.bind(this);
        initFirebase();

        kupon = new Kupon();
        kupon = getIntent().getExtras().getParcelable(KUPON_CLASS);
        String nama = getIntent().getStringExtra("Nama");
        namaKupon.setText(nama);
        loadInitView(kupon);
        loadPoinUser();
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        btnTukarkan.setOnClickListener(view -> {
            showProgress();
            if (jumlahPoin>=kupon.getJumlahpoin()){
                updatePoin = jumlahPoin - kupon.getJumlahpoin();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PenukaranKupon.this);
                alertDialogBuilder.setTitle("Pemberitahuan");
                alertDialogBuilder.setMessage("Selamat Anda dapat menggunakan kupon Anda");
                alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    updateJumlahPoinUser("poin",updatePoin);
                    addTransaksi(new RiwayatKupon(kupon.getIdkupon(),"Belum Dipakai",datetime,"-"));
                });
                alertDialogBuilder.show();
            }else {
                Toast.makeText(PenukaranKupon.this,"Anda tidak memiliki cukup poin",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadInitView(Kupon kupon){
        Picasso.get().load(kupon.getUrl()).into(imgKupon);
        namaKupon.setText(kupon.getNamakupon());
        idKupon.setText(kupon.getIdkupon());
        jnsKupon.setText(kupon.getJeniskupon());
        poin.setText(String.valueOf(kupon.getJumlahpoin()));
    }

    private void loadPoinUser(){
        jumlahPoin = 0;
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataUser();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        liveData.observe(this, dataSnapshot -> {
            String poin = dataSnapshot.child(currentUser).child("poin").getValue().toString();
            try {
                jumlahPoin = Integer.parseInt(poin);
                poinUser.setText(poin);
            }
            catch (Exception e){

            }
        });
    }


    private void updateJumlahPoinUser(String key,int value){
        HashMap<String, Object> result = new HashMap<>();
        result.put(key, value);

        firebaseDatabase.getReference()
                .child("pengguna")
                .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).updateChildren(result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PenukaranKupon.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void addTransaksi(RiwayatKupon riwayatKupon){
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        firebaseDatabase
                .getReference()
                .child("transaksikupon")
                .child(currentUser)
                .push()
                .setValue(riwayatKupon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgress();
                        Intent intent = new Intent(PenukaranKupon.this, RiwayatActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        btnTukarkan.setEnabled(false);
    }
}

package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.User;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.Toast.LENGTH_SHORT;

public class KondisiActivity extends AppCompatActivity implements View.OnClickListener{

    private View view;
    @BindView(R.id.edt_bagus)
    EditText editBagus;
    @BindView(R.id.edt_berat)
    EditText editBerat;
    @BindView(R.id.edt_ringan)
    EditText editRingan;
    @BindView(R.id.kantong)
    Button kantong;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.kantong_activity)
    ConstraintLayout constraintLayout;
    @BindView(R.id.namabarang)
    TextView namaBarang;
    @BindView(R.id.img_sampah)
    ImageView imageSampah;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kondisi);
        databaseReference = FirebaseDatabase.getInstance().getReference("kantong");

        ButterKnife.bind(this);
        initFirebase();
        kantong.setOnClickListener(this::onClick);
        hideProgress();

        String namaSampah = getIntent().getStringExtra("NamaSampah");
        namaBarang.setText(namaSampah);
        showSampah();


    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.kantong:
                showProgress();
                ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
                LiveData<DataSnapshot> liveData = viewModel.getdataKondisi();
                // Mengambil data yang diberikan oleh pengguna
                String username = firebaseAuth.getCurrentUser().getEmail();
                int jumlahBagus = Integer.parseInt(String.valueOf(editBagus.getText()));
                int jumlahRingan = Integer.parseInt(String.valueOf(editRingan.getText()));
                int jumlahBerat = Integer.parseInt(String.valueOf(editBerat.getText()));

                liveData.observe(this, new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(DataSnapshot dataSnapshot) {
                        // Untuk mengambil idSampah yang diberikan oleh pengguna

                        String idSampah = dataSnapshot
                                .child(getIntent().getStringExtra("JenisSampah"))
                                .child(getIntent().getStringExtra("NamaSampah"))
                                .child("nama")
                                .getValue().toString();

                        String imageUrl =dataSnapshot
                                .child(getIntent().getStringExtra("JenisSampah"))
                                .child(getIntent().getStringExtra("NamaSampah"))
                                .child("gambar")
                                .getValue().toString();

                        // Menampilkan gambar sampah
                        Picasso.get().load(imageUrl).into(imageSampah);

                        //
                        insertToKantong(idSampah,username,jumlahBerat,jumlahRingan, jumlahBagus);

                    }
                });

        }
    }

    private void insertToKantong(String idSampah, String idPengguna, int sampahBerat, int sampahRingan, int sampahBagus){

        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataKondisi();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                int berat = Integer.parseInt(dataSnapshot.child(getIntent().getStringExtra("JenisSampah")).child(getIntent().getStringExtra("NamaSampah")).child("berat").getValue().toString());
                int ringan = Integer.parseInt(dataSnapshot.child(getIntent().getStringExtra("JenisSampah")).child(getIntent().getStringExtra("NamaSampah")).child("ringan").getValue().toString());
                int bagus = Integer.parseInt(dataSnapshot.child(getIntent().getStringExtra("JenisSampah")).child(getIntent().getStringExtra("NamaSampah")).child("bagus").getValue().toString());

                int jumlah = sampahBagus+sampahBerat+sampahRingan;
                int totalPoint = berat*sampahBerat + ringan * sampahRingan + bagus*sampahBagus;

                if (totalPoint!=0){
                    String namaSampah = getIntent().getStringExtra("JenisSampah");

                    Kantong kantong = new Kantong(idPengguna, idSampah, jumlah, totalPoint,namaSampah);
                    databaseReference.child("kantong")
                            .child(idPengguna.replaceAll("\\.", "_"))
                            .child("data")
                            .push()
                            .setValue(kantong);
                    hideProgress();
                    clearFields();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KondisiActivity.this);
                    alertDialogBuilder.setTitle("Permintaan");
                    alertDialogBuilder.setMessage("Sampah Anda telah dimasukkan kedalam Kantong");

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();

                        }
                    });
                    alertDialogBuilder.show();
                }else {
                    Toast.makeText(KondisiActivity.this,"Silahkan isi jumlah yang diinginkan",LENGTH_SHORT).show();
                    hideProgress();
                }

            }
        });

    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        editBagus.setEnabled(true);
        editBerat.setEnabled(true);
        editRingan.setEnabled(true);

    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        editBagus.setEnabled(false);
        editBerat.setEnabled(false);
        editRingan.setEnabled(false);

    }

    private void clearFields() {
        editBagus.setText("0");
        editBerat.setText("0");
        editRingan.setText("0");
    }

    private void showSampah(){
        showProgress();
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataKondisi();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                // Untuk mengambil idSampah yang diberikan oleh pengguna

                String imageUrl =dataSnapshot
                        .child(getIntent().getStringExtra("JenisSampah"))
                        .child(getIntent().getStringExtra("NamaSampah"))
                        .child("gambar")
                        .getValue().toString();

                // Menampilkan gambar sampah
                Picasso.get().load(imageUrl).into(imageSampah);
                hideProgress();
            }
        });
    }

}

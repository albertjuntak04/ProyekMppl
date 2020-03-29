package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.projectmppl.R;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
//        Bundle bundle = new Bundle();
//        bundle.putString("totalPoint", "aku");
//        MetodeAntarFragment metodeAntarFragment = new MetodeAntarFragment();
//        metodeAntarFragment.setArguments(bundle);

        hideProgress();


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
                // Mengambil data yang diberikan oleh pengguna
                String username = firebaseAuth.getCurrentUser().getEmail();
                int jumlahBagus = Integer.parseInt(String.valueOf(editBagus.getText()));
                int jumlahRingan = Integer.parseInt(String.valueOf(editRingan.getText()));
                int jumlahBerat = Integer.parseInt(String.valueOf(editBerat.getText()));

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Untuk mengambil idSampah yang diberikan oleh pengguna

                        String idSampah = dataSnapshot.child("sampah")
                                .child(getIntent().getStringExtra("JenisSampah"))
                                .child(getIntent().getStringExtra("NamaSampah"))
                                .child("nama")
                                .getValue().toString();

                        //
                        insertToKantong(idSampah,username,jumlahBerat,jumlahRingan, jumlahBagus);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        }
    }

    private void insertToKantong(String idSampah, String idPengguna, int sampahBerat, int sampahRingan, int sampahBagus){
        String id = databaseReference.push().getKey();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Kantong kantong;
        DatabaseReference ref = database.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int berat = Integer.parseInt(dataSnapshot.child("sampah").child(getIntent().getStringExtra("JenisSampah")).child(getIntent().getStringExtra("NamaSampah")).child("berat").getValue().toString());
                int ringan = Integer.parseInt(dataSnapshot.child("sampah").child(getIntent().getStringExtra("JenisSampah")).child(getIntent().getStringExtra("NamaSampah")).child("ringan").getValue().toString());
                int bagus = Integer.parseInt(dataSnapshot.child("sampah").child(getIntent().getStringExtra("JenisSampah")).child(getIntent().getStringExtra("NamaSampah")).child("bagus").getValue().toString());

                int jumlah = sampahBagus+sampahBerat+sampahRingan;
                int totalPoint = berat*sampahBerat + ringan * sampahRingan + bagus*sampahBagus;

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
                alertDialogBuilder.setMessage("Sampah Anda telah dimasukkan kedalam Kantong!");

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                alertDialogBuilder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        editBagus.setText("");
        editBerat.setText("");
        editRingan.setText("");
    }

}

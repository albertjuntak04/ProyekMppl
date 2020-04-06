package com.example.projectmppl.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.LoginRegister;
import com.example.projectmppl.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.keluar)
    Button keluar;

    @BindView(R.id.edit_nama)
    TextView editNama;
    @BindView(R.id.btn_edit_nama)
    ImageButton btnNama;
    @BindView(R.id.edit_email)
    TextView editEmail;
    @BindView(R.id.btn_edit_email)
    ImageButton btnEmail;
    @BindView(R.id.edit_noHp)
    TextView editNoHp;
    @BindView(R.id.pekerjaan)
    TextView pekerjaan;
    @BindView(R.id.jenis_kelamin)
    TextView jenisKelamin;
    @BindView(R.id.edit_password)
    TextView editPassword;

    FloatingActionButton fab;

    private View view;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        initFirebase();
        loadDataFirebase();

        btnNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickEditNama();
            }
        });


        keluar.setOnClickListener(this::onClick);
        return view;
    }
    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void loadDataFirebase() {
        firebaseDatabase.getReference()
                .child("pengguna")
                .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userr = dataSnapshot.getValue(User.class);
                        editNama.setText(userr.getNama());
                        editEmail.setText(userr.getEmail());
                        editNoHp.setText(userr.getNoHP());
                        pekerjaan.setText(userr.getPekerjaan());
                        jenisKelamin.setText(userr.getJenisKelamin());
                        editPassword.setText(userr.getPassword());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.keluar:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginRegister.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }

    }

    public void buttonClickEditNama() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_name, null);
        final EditText etUsername = alertLayout.findViewById(R.id.edittext);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Edit Nama");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String name = etUsername.getText().toString();
//                String nama = editNama.getText().toString();
//                String email = editEmail.getText().toString();
//                String noHP = editNoHp.getText().toString();
//                String password = editPassword.getText().toString();
//                String pekerjaan1 = pekerjaan.getText().toString();
//                String jenisKelamin1 = jenisKelamin.getText().toString();
//                User user = new User(nama, email, noHP, pekerjaan1, jenisKelamin1, password);
//                firebaseDatabase.getReference()
//                        .child("pengguna")
//                        .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"))
//                        .setValue(user);
//                etUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

//    public void buttonClickEditEmail() {
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.edit_name, null);
//        final EditText etUsername = alertLayout.findViewById(R.id.edittext);
//        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//        alert.setTitle("Edit Email");
//        alert.setView(alertLayout);
//        alert.setCancelable(false);
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        AlertDialog dialog = alert.create();
//        dialog.show();
//    }



}

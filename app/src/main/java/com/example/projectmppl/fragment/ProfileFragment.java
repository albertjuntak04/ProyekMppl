package com.example.projectmppl.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.LoginRegister;
import com.example.projectmppl.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.keluar)
    Button keluar;

    @BindView(R.id.edit_nama)
    EditText editNama;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_noHp)
    EditText editNoHp;
    @BindView(R.id.pekerjaan)
    TextView pekerjaan;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.btn_simpan)
    Button simpan;

    private ProgressDialog pd;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference mChildReferenceForInputHistory = databaseReference.child("pengguna");

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        initFirebase();
        loadDataFirebase();

        pd = new ProgressDialog(getActivity());

        simpan.setOnClickListener(v -> {
            if (editNama.isInputMethodTarget()) {
                ClickEditNama("nama");
            }
            else if (editEmail.isInputMethodTarget()) {
                ClickEditEmail("email");
            }
            else if (editNoHp.isInputMethodTarget()) {
                ClickEditNoHP("noHP");
            }
            else if (editPassword.isInputMethodTarget()) {
                ClickEditPassword("password");
            }
        });


        keluar.setOnClickListener(this);
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
                        editPassword.setText(userr.getPassword());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.keluar) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginRegister.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

//    public void updateProfile () {
//        String DISPLAY_NAME = editNama.getText().toString();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
//                .setDisplayName(DISPLAY_NAME)
//                .build();
//
//        firebaseUser.updateProfile(request)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getActivity(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void ClickEditNama(String key) {
        String value = editNama.getText().toString().trim();
        if (!TextUtils.isEmpty(value)){
            pd.show();
            HashMap<String, Object> result = new HashMap<>();
            result.put(key, value);

            firebaseDatabase.getReference()
                    .child("pengguna")
                    .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).updateChildren(result)
                    .addOnSuccessListener(aVoid -> pd.dismiss())
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        else {
            Toast.makeText(getActivity(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void ClickEditEmail(String key) {
        String value = editEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(value)){
            pd.show();
            HashMap<String, Object> result = new HashMap<>();
            result.put(key, value);

            firebaseDatabase.getReference()
                    .child("pengguna")
                    .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).updateChildren(result)
                    .addOnSuccessListener(aVoid -> pd.dismiss())
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());

            firebaseAuth.getCurrentUser().updateEmail(value);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            String valuee = editEmail.getText().toString().trim();

            if (valuee != null && !valuee.equals("")) {

                // (1) Creatw a new child node (temporaryUsername)
                databaseReference.child("pengguna").child(valuee.replaceAll("\\.", "_")).push().setValue("");

                // (2) Copy the values from the exisiting node (username) to the new node (temporaryUsername)
                DatabaseReference usersInputHistorySourceNode = FirebaseDatabase.getInstance().getReference().child("pengguna").child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"));
                final DatabaseReference usersInputHistoryTargetNode = FirebaseDatabase.getInstance().getReference().child("pengguna").child(valuee.replaceAll("\\.", "_"));
                ValueEventListener valueEventListenerForUsersInputHistory = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usersInputHistoryTargetNode.setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                // (3) Remove the existing node (username)
                                mChildReferenceForInputHistory.child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).removeValue();
                                Log.d("User Input History copy", "Success!");
                            } else {
                                Log.d("User Input History copy", "Copy failed!");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                usersInputHistorySourceNode.addListenerForSingleValueEvent(valueEventListenerForUsersInputHistory);

            }

            DatabaseReference usersInputHistorySourceNodee = FirebaseDatabase.getInstance().getReference().child("kantong").child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).child("data");

            if (usersInputHistorySourceNodee != null && !usersInputHistorySourceNodee.equals("")) {

                // (1) Creatw a new child node (temporaryUsername)
                databaseReference.child("kantong").child(valuee.replaceAll("\\.", "_")).child("data").push().setValue("");

                // (2) Copy the values from the exisiting node (username) to the new node (temporaryUsername)

                final DatabaseReference usersInputHistoryTargetNode = FirebaseDatabase.getInstance().getReference().child("kantong").child(valuee.replaceAll("\\.", "_")).child("data");
                ValueEventListener valueEventListenerForUsersInputHistory = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usersInputHistoryTargetNode.setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                // (3) Remove the existing node (username)

                                FirebaseDatabase.getInstance().getReference().child("kantong").child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot12) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot12.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Log.d("User Input History copy", "Success!");
                            } else {
                                Log.d("User Input History copy", "Copy failed!");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                usersInputHistorySourceNodee.addListenerForSingleValueEvent(valueEventListenerForUsersInputHistory);

            }

            DatabaseReference usersInputHistorySourceNodeee = FirebaseDatabase.getInstance().getReference().child("transaksipenukaransampah").child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"));

            if (usersInputHistorySourceNodeee != null && !usersInputHistorySourceNodeee.equals("")) {

                // (1) Creatw a new child node (temporaryUsername)
                databaseReference.child("transaksipenukaransampah").child(valuee.replaceAll("\\.", "_")).push().setValue("");

                // (2) Copy the values from the exisiting node (username) to the new node (temporaryUsername)

                final DatabaseReference usersInputHistoryTargetNode = FirebaseDatabase.getInstance().getReference().child("transaksipenukaransampah").child(valuee.replaceAll("\\.", "_"));
                ValueEventListener valueEventListenerForUsersInputHistory = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usersInputHistoryTargetNode.setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                // (3) Remove the existing node (username)

                                FirebaseDatabase.getInstance().getReference().child("transaksipenukaransampah").child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot1.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Log.d("User Input History copy", "Success!");
                            } else {
                                Log.d("User Input History copy", "Copy failed!");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                usersInputHistorySourceNodeee.addListenerForSingleValueEvent(valueEventListenerForUsersInputHistory);

            }



        }
        else {
            Toast.makeText(getActivity(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }


//    public void updateEmail(String key){
//        String emaill = editEmail.getText().toString().trim();
//        HashMap<String, Object> result = new HashMap<>();
//        result.put(key, emaill);
//        firebaseDatabase.getReference()
//                .child("pengguna")
//                .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).updateChildren(result);
//    }

    private void ClickEditNoHP(String key) {
        String value = editNoHp.getText().toString().trim();
        if (!TextUtils.isEmpty(value)){
            pd.show();
            HashMap<String, Object> result = new HashMap<>();
            result.put(key, value);

            firebaseDatabase.getReference()
                    .child("pengguna")
                    .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).updateChildren(result)
                    .addOnSuccessListener(aVoid -> pd.dismiss())
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        else {
            Toast.makeText(getActivity(), "No HP tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void ClickEditPassword(String key) {
        final String value = editPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(value)){
            pd.show();
            HashMap<String, Object> result = new HashMap<>();
            result.put(key, value);

            firebaseDatabase.getReference()
                    .child("pengguna")
                    .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_")).updateChildren(result)
                    .addOnSuccessListener(aVoid -> {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Profil berhasil di edit", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());

            firebaseAuth.getCurrentUser().updatePassword(value);
        }
        else {
            Toast.makeText(getActivity(), "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }



}

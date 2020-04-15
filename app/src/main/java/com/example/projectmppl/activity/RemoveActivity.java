package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListNonOrganikAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class RemoveActivity extends AppCompatActivity implements ListNonOrganikAdapter.OnRemovedNonOrganikListener {

    private ListNonOrganikAdapter listNonOrganikAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        listNonOrganikAdapter  = new ListNonOrganikAdapter();
        listNonOrganikAdapter.setDeleteNonOrganikClickedListener(this);
    }

    @Override
    public void RemoveNonOrganikClicked(String key, int position) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("kantong");
        databaseReference2.child(currentUser)
                .child("data")
                .child("nonOrganik")
                .child(key)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        listNonOrganikAdapter.notifyDataSetChanged();
                        listNonOrganikAdapter.removeItem(position);
//                        putList(idSampah,idSampahNonOrganik,listPakaian,totalPoint,listKey);
                    }
                });
    }
}

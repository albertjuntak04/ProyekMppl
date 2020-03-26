package com.example.projectmppl.activity.jenissampah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListSampahAdapter;
import com.example.projectmppl.data.HandphoneData;
import com.example.projectmppl.model.Sampah;

import java.util.ArrayList;

public class HandphoneActivity extends AppCompatActivity {
    private RecyclerView rvHandphone;
    private ArrayList<Sampah> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handphone);

        rvHandphone = findViewById(R.id.rv_handphone);
        rvHandphone.setHasFixedSize(true);

        list.addAll(HandphoneData.getListData());
        showRecyclerView();
    }

    private void showRecyclerView() {
        rvHandphone.setLayoutManager(new LinearLayoutManager(this));
        ListSampahAdapter listHandphoneAdapter = new ListSampahAdapter(list,this);
        rvHandphone.setAdapter(listHandphoneAdapter);
    }

}

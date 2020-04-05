package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.fragment.HomeFragment;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.fragment.metode.MetodeJemputFragment;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class KantongActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "KantongActivity";
    public static String KEY_ACTIVITY = "DaftarSampah";
    @BindView(R.id.recycleview)
    RecyclerView recyclerViewData;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Kantong> listData;
    private ArrayList<String> listKey;
    private ArrayList<String> idSampah;
    private int totalPoint;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private ListKantongAdapter kantongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantong);
        ButterKnife.bind(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        String value = getIntent().getStringExtra("removeData");



        initFirebase();

        loadDataFirebase(new MetodeAntarFragment());
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewData.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewData.setHasFixedSize(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_antar:
                fragment = new MetodeAntarFragment();
                break;
            case R.id.action_jemput:
                fragment = new MetodeJemputFragment();
                break;
        }

        return loadDataFirebase(fragment);
    }

    // method untuk load fragment yang sesuai
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_content_dashboard, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void putList(ArrayList<String> list, int totalPoint, Fragment fragment,ArrayList<String> listKey) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_ACTIVITY, list);
        bundle.putInt("totalPoint", totalPoint);
        bundle.putStringArrayList("listKey", listKey);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_content_dashboard, fragment)
                .commit();
    }

    private boolean loadDataFirebase(Fragment fragment) {
        showProgress();
        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataSnapshotLiveData();
        totalPoint = 0;
        listKey = new ArrayList<>();
        listData = new ArrayList<>();
        idSampah = new ArrayList<>();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    hideProgress();
                    for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                        Kantong kantong = dataItem.getValue(Kantong.class);
                        listKey.add(dataItem.getKey());
                        listData.add(kantong);
                        idSampah.add(kantong.getIdSampah());
                        totalPoint = totalPoint+kantong.getJumlahPoint();
                    }
                    putList(idSampah,totalPoint,fragment,listKey);

                    kantongAdapter = new ListKantongAdapter(listData);


                    final String sender=getIntent().getExtras().getString("removeData");

                    //IF ITS THE FRAGMENT THEN RECEIVE DATA
                    if(!sender.equals("removeData"))
                    {
                        recyclerViewData.setAdapter(kantongAdapter);

                    }else{
                       listData.clear();
                       kantongAdapter.notifyDataSetChanged();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KantongActivity.this);
                        alertDialogBuilder.setTitle("Permintaan");
                        alertDialogBuilder.setMessage("Sampah Anda sedang diproses. Terimakasih");
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(KantongActivity.this, MainActivity.class);
                                startActivity(intent);

                            }
                        });
                        alertDialogBuilder.show();
                    }

                    }

                }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.fragment.KantongFragment;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.fragment.metode.MetodeJemputFragment;
import com.example.projectmppl.model.Kantong;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class KantongActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, KantongFragment.FragmentKantongListener, MetodeAntarFragment.MetodeAntarFragmentListener{


    private static final String TAG = "KantongActivity";
    public static String KEY_ACTIVITY = "DaftarSampah";

    private final String SIMPLE_FRAGMENT_TAG = "myfragmenttag";
//    @BindView(R.id.recycleview)
//    RecyclerView recyclerViewData;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Kantong> listData;
    private ArrayList<String> listKey;
    private ArrayList<String> idSampah;
    private int totalPoint;
//    @BindView(R.id.progress)
//    ProgressBar progressBar;
    private ListKantongAdapter kantongAdapter;
    private MetodeJemputFragment metodeJemputFragment;
    private KantongFragment kantongFragment;
    private MetodeAntarFragment metodeAntarFragment;


//    private MySimpleFragment fragmentSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantong);
        ButterKnife.bind(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        metodeAntarFragment = new MetodeAntarFragment();
        metodeJemputFragment = new MetodeJemputFragment();
        kantongFragment = new KantongFragment();
        loadKantongFragment(new KantongFragment());
//
//        if (savedInstanceState != null) { // saved instance state, fragment may exist
//            // look up the instance that already exists by tag
//            kantongFragment = (KantongFragment)
//                    getSupportFragmentManager().findFragmentByTag(SIMPLE_FRAGMENT_TAG);
//        } else if (kantongFragment == null) {
//            // only create fragment if they haven't been instantiated already
//            kantongFragment = new KantongFragment();
//        }

        if (!kantongFragment.isInLayout()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_content_dashboard, kantongFragment, SIMPLE_FRAGMENT_TAG)
                    .commit();
        }



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_content_dashboard, metodeAntarFragment)
                .commit();


        String receive = getIntent().getStringExtra("saveData");

        if (receive.equals("removeData")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

//        loadFragment(new MetodeAntarFragment());



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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

        return loadFragment(fragment);
    }

    // method untuk load fragment yang sesuai
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("removeData", "remove");
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_content_dashboard, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadKantongFragment(Fragment fragment){
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("removeData", "remove");
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_content_kantong, fragment)
                    .commit();
        }
    }



    @Override
    public void onInputKantongFragmentSent(ArrayList<String> input, int totalPoint, ArrayList<String> listKey) {
        metodeAntarFragment.sendData(input,totalPoint,listKey);
    }

    @Override
    public void onInputKantongFragmentSent(String removeData) {
//        kantongFragment.loadDataFirebase(removeData);
        kantongFragment.removeData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}

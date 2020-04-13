package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.adapter.ListKantongAdapter;
import com.example.projectmppl.fragment.KantongFragment;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.fragment.metode.MetodeJemputFragment;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class KantongActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, KantongFragment.FragmentElektronikListener, MetodeAntarFragment.MetodeAntarFragmentListener {

    private KantongFragment kantongFragment;
    private MetodeAntarFragment metodeAntarFragment = new MetodeAntarFragment();
    private String receive = "remove";

    private ArrayList<Kantong> inputKantong;
    private ArrayList<KantongNonOrganik>kantongNonOrganiks;
    private ArrayList<Kantong>inputPakaian;
    private int totalPoint;
    private ArrayList<String> listKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantong);
        ButterKnife.bind(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        kantongFragment = new KantongFragment();
        loadKantongFragment(new KantongFragment());
        loadFragment(metodeAntarFragment);
        receive = getIntent().getStringExtra("saveData");


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





    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragmentAntar = new MetodeAntarFragment();
        Fragment fragmentJemput= new MetodeJemputFragment();
        switch (item.getItemId()) {
            case R.id.action_antar:
                fragmentAntar = new MetodeAntarFragment(inputKantong,kantongNonOrganiks,inputPakaian,totalPoint,listKey);
                return loadFragment(fragmentAntar);
            case R.id.action_jemput:
                fragmentJemput = new MetodeJemputFragment(inputKantong,kantongNonOrganiks,inputPakaian,totalPoint,listKey);
                return loadFragment(fragmentJemput);

        }

        return true;

    }

    // method untuk load fragment yang sesuai
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
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
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_content_kantong, fragment,"MY_FRAGMENT")
                    .commit();
        }
    }




    @Override
    public void onInputKantongFragmentSent(ArrayList<Kantong> input,ArrayList<KantongNonOrganik>kantongNonOrganiks,ArrayList<Kantong>inputPakaian, int totalPoint, ArrayList<String> listKey) {
            metodeAntarFragment.sendData(input, kantongNonOrganiks, inputPakaian, totalPoint, listKey);
            this.inputKantong = input;
            this.kantongNonOrganiks = kantongNonOrganiks;
            this.inputPakaian = inputPakaian;
            this.totalPoint = totalPoint;
            this.listKey  = listKey;

    }




    @Override
    public void onInputKantongFragmentSent(String removeData) {
        kantongFragment.removeData();
    }



}

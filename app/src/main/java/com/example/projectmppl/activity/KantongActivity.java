package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectmppl.R;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.fragment.metode.MetodeJemputFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.ButterKnife;

public class KantongActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantong);
        ButterKnife.bind(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout_content_dashboard, new MetodeAntarFragment(), MetodeAntarFragment.class.getSimpleName())
                .commit();

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_content_dashboard, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

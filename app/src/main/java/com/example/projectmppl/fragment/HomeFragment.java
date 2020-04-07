package com.example.projectmppl.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.DaftarKupon;
import com.example.projectmppl.activity.jenissampah.ElektronikActivity;
import com.example.projectmppl.activity.KantongActivity;
import com.example.projectmppl.activity.jenissampah.NonOrganikActivity;
import com.example.projectmppl.activity.jenissampah.PakaianActivity;
import com.example.projectmppl.fragment.metode.MetodeAntarFragment;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener  {
    private View view;
    @BindView(R.id.kupon)
    CardView cardView;
    @BindView(R.id.poin)
    CardView cardPoin;
    @BindView(R.id.img_nonorganik)
    ImageView nonOrganik;
    @BindView(R.id.img_elektronik)
    ImageView elektronik;
    @BindView(R.id.img_pakaian)
    ImageView pakaian;
    @BindView(R.id.dashboard_iv_kantong)
    ImageView kantong;
    @BindView(R.id.dashboard_tv_garbagechange)
    TextView textView;
    @BindView(R.id.dashboard_tv_name)
    TextView tvName;
    @BindView(R.id.total_poin)
    TextView totalPoin;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        cardView.setOnClickListener(this::onClick);
        cardPoin.setOnClickListener(this::onClick);
        nonOrganik.setOnClickListener(this::onClick);
        elektronik.setOnClickListener(this::onClick);
        pakaian.setOnClickListener(this::onClick);
        kantong.setOnClickListener(this::onClick);

        ViewModelFirebase viewModel = ViewModelProviders.of(this).get(ViewModelFirebase.class);
        LiveData<DataSnapshot> liveData = viewModel.getdataUser();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "_");
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                // Untuk menampilkan dataUser
                String name = dataSnapshot.child(currentUser).child("nama").getValue().toString();
                String poin = dataSnapshot.child(currentUser).child("poin").getValue().toString();
                tvName.setText(name);
                totalPoin.setText(poin);


            }
        });

        return view;

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.kupon:
                Intent intent = new Intent(getActivity(), DaftarKupon.class);
                startActivity(intent);
                break;
            case R.id.poin:
                Intent intent1 = new Intent(getActivity(), DaftarKupon.class);
                startActivity(intent1);
                break;
            case R.id.img_nonorganik:
                Intent nonOrganik = new Intent(getActivity(), NonOrganikActivity.class);
                startActivity(nonOrganik);
                break;
            case R.id.img_elektronik:
                Intent elektronik = new Intent(getActivity(), ElektronikActivity.class);
                startActivity(elektronik);
                break;
            case R.id.img_pakaian:
                Intent pakaian = new Intent(getActivity(), PakaianActivity.class);
                startActivity(pakaian);
                break;
            case R.id.dashboard_iv_kantong:
                Intent kantong = new Intent(getActivity(), KantongActivity.class);
                kantong.putExtra("saveData","remove");
                startActivity(kantong);
        }

    }



}

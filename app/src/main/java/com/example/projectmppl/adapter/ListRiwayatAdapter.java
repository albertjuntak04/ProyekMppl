package com.example.projectmppl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.example.projectmppl.model.Transaksi;
import com.example.projectmppl.ui.ViewModelFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRiwayatAdapter extends RecyclerView.Adapter<ListRiwayatAdapter.ViewHolder> {
    private List<Transaksi> listTransaksi;
    private List<Kantong> listKantong;
    private Kantong kantong = new Kantong();
    private ArrayList<String> daftarElektronik;
    private ArrayList<String> daftarNonOrganik;
    private int jumlahElektronik = 0;
    private int totalBerat = 0;
    private List<KantongNonOrganik> kantongNonOrganikList;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;


    public ListRiwayatAdapter(){

    }

    public ListRiwayatAdapter(List<Transaksi> listTransaksi,Context context){
        this.listTransaksi = listTransaksi;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, null);
        return new ListRiwayatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi transaksi = listTransaksi.get(position);
        daftarElektronik = new ArrayList<>();
        daftarNonOrganik = new ArrayList<>();
        jumlahElektronik = 0;
        totalBerat = 0;
        holder.tanggalPemesanan.setText(String.valueOf(transaksi.getTaggalRequest()));
        holder.metode.setText(String.valueOf(transaksi.getMetode()));
        holder.elektronik.setText(String.valueOf(transaksi.getKantongElektronik()));
        holder.totalPoin.setText(String.valueOf(transaksi.getTotalPoin()));
        holder.status.setText(String.valueOf(transaksi.getStatus()));

        if (String.valueOf(transaksi.getStatus()).equals("Diproses")){
            holder.status.setTextColor(Color.GREEN);
        }else {
            holder.status.setTextColor(Color.RED);
        }

        if (transaksi.getKantongElektronik() == null){
            holder.elektronik.setText(String.valueOf("-"));
            holder.jumlahElektronik.setText(String.valueOf(0));
        }else{
            for (int index = 0;index<transaksi.getKantongElektronik().size();index++ ){
                Kantong kantong1 = transaksi.getKantongElektronik().get(index);
                daftarElektronik.add(String.valueOf(kantong1.getIdSampah()));
                jumlahElektronik = jumlahElektronik + kantong1.getJumlah();
            }
            holder.elektronik.setText(String.valueOf(daftarElektronik));
            holder.jumlahElektronik.setText(String.valueOf(jumlahElektronik));
        }


        if (transaksi.getKantongNonOrganiks() == null){
            holder.nonOrganik.setText(String.valueOf("-"));
            holder.jumlahOrganik.setText(String.valueOf(0));
        }else {
            for (int index = 0;index<transaksi.getKantongNonOrganiks().size();index++ ){
            KantongNonOrganik kantongNonOrganik = transaksi.getKantongNonOrganiks().get(index);
            daftarNonOrganik.add(String.valueOf(kantongNonOrganik.getIdSampah()));
            totalBerat = totalBerat + kantongNonOrganik.getJumlah();
        }

            holder.nonOrganik.setText(String.valueOf(daftarNonOrganik));
            holder.jumlahOrganik.setText(String.valueOf(totalBerat));
        }



    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tgl_pemesanan)
        TextView tanggalPemesanan;
        @BindView(R.id.list_nonorganik)
        TextView nonOrganik;
        @BindView(R.id.jumlah_organik)
        TextView jumlahOrganik;
        @BindView(R.id.list_elektronik)
        TextView elektronik;
        @BindView(R.id.jumlah_elektronik)
        TextView jumlahElektronik;
        @BindView(R.id.metode)
        TextView metode;
        @BindView(R.id.total_poin)
        TextView totalPoin;
        @BindView(R.id.hapus)
        Button btnHapus;
        @BindView(R.id.status)
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }
}

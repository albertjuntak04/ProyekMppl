package com.example.projectmppl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Transaksi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRiwayatAdapter extends RecyclerView.Adapter<ListRiwayatAdapter.ViewHolder> {
    private List<Transaksi> listTransaksi;

    public ListRiwayatAdapter(){

    }

    public ListRiwayatAdapter(List<Transaksi> listTransaksi){
        this.listTransaksi = listTransaksi;
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
        holder.tanggalPemesanan.setText(String.valueOf(transaksi.getTaggalRequest()));
        holder.elektronik.setText(String.valueOf(transaksi.getIdSampah()));
        holder.metode.setText(String.valueOf(transaksi.getMetode()));
        holder.totalPoin.setText(String.valueOf(transaksi.getTotalPoin()));
        holder.jumlahElektronik.setText(String.valueOf(transaksi.getIdSampah().size()));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

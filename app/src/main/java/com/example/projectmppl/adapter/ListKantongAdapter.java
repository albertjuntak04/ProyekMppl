package com.example.projectmppl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Kantong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListKantongAdapter extends RecyclerView.Adapter<ListKantongAdapter.ViewHolder> {
    private List<Kantong> listKantong;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public ListKantongAdapter(){

    }

    public ListKantongAdapter(List<Kantong> listKantong){
        this.listKantong = listKantong;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sampah, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kantong kantong = listKantong.get(position);
        holder.jumlah.setText(String.valueOf(kantong.getJumlah()));
        holder.total.setText(String.valueOf(kantong.getJumlahPoint()));
        holder.jenisSampah.setText(String.valueOf(kantong.getIdSampah()));

    }

    @Override
    public int getItemCount() {
        return listKantong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.total_berat)
        TextView jumlah;
        @BindView(R.id.total_poin)
        TextView total;
        @BindView(R.id.jenis_sampah)
        TextView jenisSampah;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



    public void removeItem(int position) {
        listKantong.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listKantong.size());
    }

}

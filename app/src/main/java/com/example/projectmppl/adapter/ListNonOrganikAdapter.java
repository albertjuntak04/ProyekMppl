package com.example.projectmppl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListNonOrganikAdapter extends RecyclerView.Adapter<ListNonOrganikAdapter.ViewHolder> {
    private List<KantongNonOrganik> listKantong;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public ListNonOrganikAdapter(){

    }

    public ListNonOrganikAdapter(List<KantongNonOrganik> listKantong){
        this.listKantong = listKantong;
    }



    @NonNull
    @Override
    public ListNonOrganikAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sampah, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNonOrganikAdapter.ViewHolder holder, int position) {
        KantongNonOrganik kantong = listKantong.get(position);
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
        public ViewHolder(@NonNull View itemView) {
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

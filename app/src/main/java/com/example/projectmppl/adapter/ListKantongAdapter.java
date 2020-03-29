package com.example.projectmppl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.Kantong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListKantongAdapter extends RecyclerView.Adapter<ListKantongAdapter.ViewHolder> {
    private List<Kantong> listKantong;
    private List<String> listJenisSampah;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public ListKantongAdapter(List<Kantong> listKantong){
        this.listKantong = listKantong;
//        this.listJenisSampah = listJenisSampah;
//        this.listnama = nama;
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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("sampah").child(kantong.getJenisSampah()).child(kantong.getIdSampah()).child("nama").getValue().toString();
                holder.jenisSampah.setText(nama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}

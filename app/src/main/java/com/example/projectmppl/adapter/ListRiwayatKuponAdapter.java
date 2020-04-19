package com.example.projectmppl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.model.RiwayatKupon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRiwayatKuponAdapter extends RecyclerView.Adapter<ListRiwayatKuponAdapter.ViewHolder> {
    private ArrayList<RiwayatKupon> riwayatKupons;
    private ArrayList<String> listKey;
    private Context context;

    public ListRiwayatKuponAdapter(ArrayList<RiwayatKupon>riwayatKupons, Context context, ArrayList<String>listKey){
        this.riwayatKupons = riwayatKupons;
        this.context = context;
        this.listKey = listKey;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_kupon, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.status.setText(riwayatKupons.get(position).getStatus());
       holder.tglPenukaran.setText(riwayatKupons.get(position).getTglPemesanan());
       holder.tglPemakaian.setText(riwayatKupons.get(position).getTglPemakaian());
       String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replaceAll("\\.", "_");
       FirebaseDatabase
                .getInstance()
                .getReference()
                .child("kupon")
                .child(riwayatKupons.get(position).getIdKupon())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if (dataSnapshot!=null){
                          try {
                              String jenisKupon = dataSnapshot.child("jeniskupon").getValue().toString();
                              String jumlahpoin = dataSnapshot.child("jumlahpoin").getValue().toString();
                              String idKupon = dataSnapshot.child("idkupon").getValue().toString();
                              holder.jnsKupon.setText(jenisKupon);
                              holder.poin.setText(jumlahpoin);
                              holder.idKupon.setText(idKupon);

                          }catch (Exception e){

                          }
                      }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

       holder.btnHapus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("transaksikupon");
               databaseReference2
                       .child(currentUser)
                       .child(listKey.get(position))
                       .removeValue()
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               itemRemove(position);
                           }
                       });
           }
       });


    }

    @Override
    public int getItemCount() {
        return riwayatKupons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.tgl_penukaran)
        TextView tglPenukaran;
        @BindView(R.id.tgl_pemakaian)
        TextView tglPemakaian;
        @BindView(R.id.poin)
        TextView poin;
        @BindView(R.id.idkupon)
        TextView idKupon;
        @BindView(R.id.jns_kupon)
        TextView jnsKupon;
        @BindView(R.id.hapus)
        Button btnHapus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void itemRemove(int position){
        riwayatKupons.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,riwayatKupons.size());
        notifyDataSetChanged();

    }
}

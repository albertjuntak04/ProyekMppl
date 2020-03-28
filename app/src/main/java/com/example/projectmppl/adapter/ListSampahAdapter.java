package com.example.projectmppl.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KondisiActivity;
import com.example.projectmppl.model.Sampah;

import java.util.ArrayList;

public class ListSampahAdapter extends RecyclerView.Adapter<ListSampahAdapter.ListViewHolder> {
    private ArrayList<Sampah> listPakaian;
    private Context context;

    public ListSampahAdapter(ArrayList<Sampah> list, Context context){
        this.listPakaian = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daftar_sampah, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Sampah pakaian = listPakaian.get(position);
        holder.namePakaian.setText(pakaian.getName());
        holder.namePakaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KondisiActivity.class);
                intent.putExtra("NamaSampah",listPakaian.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView namePakaian;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            namePakaian = itemView.findViewById(R.id.txt_nama_sampah);
        }
    }
}

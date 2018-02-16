package com.example.android.beritafiras.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.activity.DetailAdminActivity;
import com.example.android.beritafiras.config.MyConstant;
import com.example.android.beritafiras.models.BeritaItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Firas Luthfi on 2/13/2018.
 */

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.MyViewHolder> {

    // Buat Global variable untuk manampung context
    Context context;
    List<BeritaItem> berita;

    public AdapterAdmin(Context context, List<BeritaItem> data_berita) {
        // Inisialisasi
        this.context = context;
        this.berita = data_berita;
    }

    @Override
    public AdapterAdmin.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.berita_item, parent, false);

        // Hubungkan dengan MyViewHolder
        AdapterAdmin.MyViewHolder holder = new AdapterAdmin.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterAdmin.MyViewHolder holder, final int position) {
        // Set widget
        holder.tvJudul.setText(berita.get(position).getTitle());
        holder.tvTglTerbit.setText(berita.get(position).getPublishDate());
        holder.tvPenulis.setText("Oleh : " + berita.get(position).getPublisher());

        // Set image ke widget dengna menggunakan Library Piccasso
        // krena imagenya dari internet
        Picasso.with(context).load(MyConstant.THUMBNAILS_URL + berita.get(position).getFoto()).into(holder.ivGambarBerita);

        // Event klik ketika item list nya di klik
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai activity Detail
                Intent varIntent = new Intent(context, DetailAdminActivity.class);
                // sisipkan data ke intent
                varIntent.putExtra("ID", berita.get(position).getIdBerita());
                varIntent.putExtra("TITLE", berita.get(position).getTitle());
                varIntent.putExtra("TGL", berita.get(position).getPublishDate());
                varIntent.putExtra("PUBLISHER", berita.get(position).getPublisher());
                varIntent.putExtra("FOTO", MyConstant.FOTO_URL+berita.get(position).getFoto());
                varIntent.putExtra("CONTENT", berita.get(position).getContent());

                // method startActivity cma bisa di pake di activity/fragment
                // jadi harus masuk ke context dulu
                context.startActivity(varIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return berita.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Deklarasi widget
        ImageView ivGambarBerita;
        TextView tvJudul, tvTglTerbit, tvPenulis;
        public MyViewHolder(View itemView) {
            super(itemView);
            // inisialisasi widget
            ivGambarBerita = (ImageView) itemView.findViewById(R.id.ivPosterBerita);
            tvJudul = (TextView) itemView.findViewById(R.id.tvJudulBerita);
            tvTglTerbit = (TextView) itemView.findViewById(R.id.tvTglTerbit);
            tvPenulis = (TextView) itemView.findViewById(R.id.tvPenulis);
        }
    }
}

package com.example.app_cotizacion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.app_cotizacion.R;
import com.example.app_cotizacion.model.LogsQB;

import java.util.List;

public class LogsQBAdapter extends RecyclerView.Adapter<LogsQBAdapter.ViewHolder> {
    private List<LogsQB> logsQBList;

    public LogsQBAdapter(List<LogsQB> logsQBList) {
        this.logsQBList = logsQBList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logs_array_element, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogsQBAdapter.ViewHolder holder, int position) {
        LogsQB logsQB = logsQBList.get(position);
        holder.amountTv.setText("Cantidad: "+String.valueOf(logsQB.getAmount()));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.none_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(holder.itemView.getContext())
                .load(logsQB.getImg())
                .apply(requestOptions)
                .into(holder.imgTv);

        holder.nameTv.setText(logsQB.getNameM());
        holder.priceTv.setText("Precio: $"+String.valueOf(logsQB.getPrice()));
        holder.totalXmaterialTv.setText("Valor total por material: $"+String.valueOf(logsQB.getTotalXmaterial()));
    }

    @Override
    public int getItemCount() {
        return logsQBList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amountTv, nameTv, priceTv, totalXmaterialTv;
        public ImageView imgTv;
        public ViewHolder(View itemView) {
            super(itemView);
            amountTv = itemView.findViewById(R.id.materialAmount);
            imgTv = itemView.findViewById(R.id.materialImg);
            nameTv = itemView.findViewById(R.id.materialName);
            priceTv = itemView.findViewById(R.id.materialPrice);
            totalXmaterialTv = itemView.findViewById(R.id.materialTotal);
        }
    }
}

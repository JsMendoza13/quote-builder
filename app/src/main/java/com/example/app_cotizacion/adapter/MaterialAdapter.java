package com.example.app_cotizacion.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_cotizacion.R;
import com.example.app_cotizacion.model.Material;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {
    private List<Material> materialList;
    private ArrayList<Double> materialAmountList = new ArrayList<>();

    public ArrayList<Double> getMaterialAmountList() {
        return materialAmountList;
    }
    public MaterialAdapter (List<Material> materialList) {
        this.materialList = materialList;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_card, parent, false);
        materialAmountList.clear();
        for (int i = 0; i < materialList.size(); i++) {
            materialAmountList.add(0.0);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Material item = materialList.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView materialImg;
        TextView materialName, materialPrice, materialStatus;
        EditText materialAmount;
        MaterialCardView materialCardView;
        RecyclerView mRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.material_cv);
            mRecycler = itemView.findViewById(R.id.containerRV);
            materialImg = itemView.findViewById(R.id.materialImg);
            materialName = itemView.findViewById(R.id.materialName);
            materialPrice = itemView.findViewById(R.id.materialPrice);
            materialStatus = itemView.findViewById(R.id.materialStatus);
            materialAmount = itemView.findViewById(R.id.materialAmount);
        }

        public void bind(Material item, int position) {
            Glide.with(itemView.getContext())
                    .load(item.getMaterialImg())
                    .placeholder(R.drawable.none_picture)
                    .into(materialImg);
            materialName.setText(item.getMaterialName());
            materialPrice.setText(String.valueOf(item.getMaterialPrice()));
            materialStatus.setText(item.getMaterialStatus());
            int backgroundColor = item.isSelected() ? ContextCompat.getColor(itemView.getContext(), R.color.light_gray)
                    : ContextCompat.getColor(itemView.getContext(), R.color.white);
            materialCardView.setCardBackgroundColor(backgroundColor);
            materialCardView.setOnClickListener(v -> {
                item.setSelected(!item.isSelected());
                bind(item, position);
            });
            materialAmount.setText(String.valueOf(materialAmountList.get(position)));
            materialAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    calculateTotal();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String edAmount = materialAmount.getText().toString();
                    if (!TextUtils.isEmpty(edAmount)) {
                        double amountD = Double.parseDouble(edAmount);
                        materialAmountList.set(getAdapterPosition(), amountD);
                        calculateTotal();
                    }
                }
            });
        }
    }
    public double calculateTotal() {
        double total = 0.0;
        if (materialAmountList.size() == materialList.size()) {
            for (int i = 0; i < materialAmountList.size(); i++) {
                double amount = materialAmountList.get(i);
                double multiplication = amount * materialList.get(i).getMaterialPrice();
                total += multiplication;
            }
        }
        return total;
    }
}


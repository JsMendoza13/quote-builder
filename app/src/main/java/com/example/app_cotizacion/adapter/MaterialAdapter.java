package com.example.app_cotizacion.adapter;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_cotizacion.R;
import com.example.app_cotizacion.model.Material;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MaterialAdapter extends FirestoreRecyclerAdapter<Material, MaterialAdapter.ViewHolder> {

    String sprice, samount;
    double price, amount, totalUnit, acmTotal;

    public MaterialAdapter(@NonNull FirestoreRecyclerOptions<Material> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Material model) {
        String imageUrl = model.getMaterialImg();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.none_picture)
                .into(holder.materialImg);
        holder.materialName.setText(model.getMaterialName());
        holder.materialPrice.setText(model.getMaterialPrice());
        holder.materialStatus.setText(model.getMaterialStatus());

        DocumentSnapshot materialSnapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String materialId = materialSnapshot.getId();
        DocumentReference materialRef = db.collection("materials").document(materialId);
        db.collection("materials").whereEqualTo("isSelected", false);

        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            materialRef.update("isSelected", isChecked);

            if (isChecked) {
                holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), R.color.light_gray));
            } else {
                holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), android.R.color.white));
            }
        });

        holder.materialAmount.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                sprice = model.getMaterialPrice();
                samount = holder.materialAmount.getText().toString();
                price = Double.parseDouble(sprice);
                amount = Double.parseDouble(samount);
                totalUnit = price * amount;
                return false;
            }
        });

        for (int i = 0; i < getItemCount(); i++) {
            Material material = getItem(i);
            acmTotal += totalUnit;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_card, parent, false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView materialImg;
        TextView materialName, materialPrice, materialStatus;
        EditText materialAmount;
        CheckBox check;
        MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.checkbox);
            materialCardView = itemView.findViewById(R.id.material_cv);
            materialImg = itemView.findViewById(R.id.materialImg);
            materialName = itemView.findViewById(R.id.materialName);
            materialPrice = itemView.findViewById(R.id.materialPrice);
            materialStatus = itemView.findViewById(R.id.materialStatus);
            materialAmount = itemView.findViewById(R.id.materialAmount);
        }
    }
}


package com.example.app_cotizacion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class MaterialAdapter extends FirestoreRecyclerAdapter<Material, MaterialAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
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

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_card, parent, false);

        CheckBox checkbox = v.findViewById(R.id.checkbox);
        MaterialCardView materialCardView = v.findViewById(R.id.material_cv);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Actualizar la vista de la tarjeta cuando se marca el CheckBox
                    materialCardView.setChecked(true);
                    materialCardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.light_gray));
                } else {
                    // Actualizar la vista de la tarjeta cuando se desmarca el CheckBox
                    materialCardView.setChecked(false);
                    materialCardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), android.R.color.white));
                }
            }
        });

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView materialImg;
        TextView materialName, materialPrice, materialStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            materialImg = itemView.findViewById(R.id.materialImg);
            materialName = itemView.findViewById(R.id.materialName);
            materialPrice = itemView.findViewById(R.id.materialPrice);
            materialStatus = itemView.findViewById(R.id.materialStatus);
        }
    }
}
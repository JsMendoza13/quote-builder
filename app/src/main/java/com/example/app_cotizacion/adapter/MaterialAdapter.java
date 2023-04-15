package com.example.app_cotizacion.adapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_cotizacion.R;
import com.example.app_cotizacion.model.Material;
import com.example.app_cotizacion.total;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class MaterialAdapter extends FirestoreRecyclerAdapter<Material, MaterialAdapter.ViewHolder> {

    public String samount;
    public double amount;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentSnapshot materialSnapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
        String materialId = materialSnapshot.getId();
        DocumentReference materialRef = db.collection("materials").document(materialId);

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                materialRef.update("isSelected", isChecked);
                if (isChecked) {
                    holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), R.color.light_gray));
                } else {
                    holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), android.R.color.white));
                }
            }
        });


        holder.materialAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String matAmount = holder.materialAmount.getText().toString();
                    if (matAmount.isEmpty()) {
                        CollectionReference materialsRef = db.collection("materials");
                        Query queryAmount = materialsRef.whereNotEqualTo("materialAmount", 0);
                        queryAmount.get().addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                materialRef.update("materialAmount", 0);
                            }
                        });
                    } else {
                        samount = holder.materialAmount.getText().toString();
                        amount = Double.parseDouble(samount);
                        materialRef.update("materialAmount", amount);
                    };
                }
            }
        });

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


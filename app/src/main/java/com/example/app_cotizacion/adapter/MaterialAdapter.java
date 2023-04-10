package com.example.app_cotizacion.adapter;

import static com.bumptech.glide.util.Util.getSnapshot;

import android.os.Handler;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends FirestoreRecyclerAdapter<Material, MaterialAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param
     */
    public DocumentSnapshot getDocumentSnapshot(int position) {
        return getItem(position).getDocumentSnapshot();
    }
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

//        model.setDocumentSnapshot(getSnapshots().getSnapshot(position));
//        DocumentSnapshot materialSnapshot = model.getDocumentSnapshot();
//
//        // agregar un listener para actualizar el valor isSelected en Firestore cuando se hace clic en la tarjeta
//        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                String materialId = materialSnapshot.getId();
//                DocumentReference materialRef = db.collection("materials").document(materialId);
//
//                materialRef.update("isSelected", isChecked);
//
//                if (isChecked) {
//                    // Actualizar la vista de la tarjeta cuando se marca el CheckBox
//                    holder.materialId.setText(materialId);
//                    holder.check.setChecked(true);
//                    holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), R.color.light_gray));
//                } else {
//                    // Actualizar la vista de la tarjeta cuando se desmarca el CheckBox
//                    holder.materialId.setText("");
//                    holder.check.setChecked(false);
//                    holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), android.R.color.white));
//                }
//            }
//        });

//        model.setDocumentSnapshot(getSnapshots().getSnapshot(position));
//        DocumentSnapshot materialSnapshot = model.getDocumentSnapshot();

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentSnapshot materialSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                    String materialId = materialSnapshot.getId();
                    DocumentReference materialRef = db.collection("materials").document(materialId);

                    materialRef.update("isSelected", isChecked);
                    holder.check.setChecked(isChecked);

                if (isChecked) {
                    holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), R.color.light_gray));
                } else {
                    holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(buttonView.getContext(), android.R.color.white));
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView materialImg;
        TextView materialName, materialPrice, materialStatus;
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
        }
    }
}


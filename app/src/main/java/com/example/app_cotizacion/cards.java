package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class cards extends Fragment {
    RecyclerView mRecycler;
    MaterialCardView materialCard;
    EditText materialCant;
    MaterialAdapter mAdapter;
    CheckBox check;
    FirebaseFirestore db;
    Material material= new Material();
    private View view;

    public cards() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cards, container, false);
        db = FirebaseFirestore.getInstance();
        mRecycler = view.findViewById(R.id.containerRV);
        check = view.findViewById(R.id.checkbox);

        materialCard = view.findViewById(R.id.material_cv);
        materialCant = view.findViewById(R.id.materialCant);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = db.collection("materials");

        FirestoreRecyclerOptions<Material> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Material>().setQuery(query, Material.class).build();

        mAdapter = new MaterialAdapter(firestoreRecyclerOptions);
        mRecycler.setAdapter(mAdapter);

        String materialPrice = material.getMaterialPrice();
        double price = Double.parseDouble(materialPrice);
        String materialC = materialCant.getText().toString();
        double cantidad = Double.parseDouble(materialC);

        double acmTotal = 0.0;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            Material material = mAdapter.getItem(i);
            if (material.isSelected()) {
                double total = price * cantidad;
                acmTotal += total;
            }
        }

        // Asignamos el valor del total acumulado a la vista
//        TextView totalTextView = view.findViewById(R.id.totalTextView);
//        totalTextView.setText(String.format(Locale.getDefault(), "%.2f", acmTotal));

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.stopListening();
    }
}
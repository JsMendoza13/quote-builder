package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    Button calcular, rehacer;
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
        calcular = view.findViewById(R.id.calcular);

        materialCard = view.findViewById(R.id.material_cv);
        materialCant = view.findViewById(R.id.materialCant);
        rehacer = view.findViewById(R.id.rehacer);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = db.collection("materials");

        FirestoreRecyclerOptions<Material> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Material>().setQuery(query, Material.class).build();

        mAdapter = new MaterialAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                total fragmentTotal = new total();
                fragmentTransaction.replace(R.id.container, fragmentTotal);
                fragmentTransaction.commit();

                double acmTotal = 0.0;
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    Material material = mAdapter.getItem(i);
                    double price = Double.parseDouble(material.getMaterialPrice());
                    if (!materialCant.getText().toString().isEmpty()) {
                        double cantidad = Double.parseDouble(materialCant.getText().toString());
                        if (check.isChecked()) {
                            double total = price * cantidad;
                            acmTotal += total;
                        }
                    }
                }



                Bundle bundle = new Bundle();
                bundle.putDouble("total", acmTotal);

                fragmentTotal.setArguments(bundle);
            }
        });

        rehacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isChecked()) {
                    check.setChecked(false);
                }
                materialCant.setText("");
            }
        });

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
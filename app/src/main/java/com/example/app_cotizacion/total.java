package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;

public class total extends Fragment {
    View view;
    TextView total;
    Button nuevo;
    private double acmTotal;

    public total() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_total, container, false);

        acmTotal = getArguments().getDouble("total");
        total = view.findViewById(R.id.total);
        nuevo = view.findViewById(R.id.nuevo);

        total.setText(String.valueOf(acmTotal));

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference materialsRef = db.collection("materials");
                Query query2 = materialsRef.whereEqualTo("isSelected", true);
                query2.get().addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Update the "isSelected" field to true
                        materialsRef.document(document.getId()).update("isSelected", false);
                    }
                });

                Query query_materialAmount = materialsRef.whereNotEqualTo("materialAmount", 0);
                query_materialAmount.get().addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        materialsRef.document(document.getId()).update("materialAmount", 0);
                    }
                });

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                cards fragmentCards = new cards();
                fragmentTransaction.replace(R.id.container, fragmentCards);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
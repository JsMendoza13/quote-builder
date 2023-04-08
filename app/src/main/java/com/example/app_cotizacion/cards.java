package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class cards extends Fragment {
    RecyclerView mRecycler;
    MaterialAdapter mAdapter;
    FirebaseFirestore mFirestore;
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
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = view.findViewById(R.id.containerRV);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = mFirestore.collection("materials");

        FirestoreRecyclerOptions<Material> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Material>().setQuery(query, Material.class).build();

        mAdapter = new MaterialAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
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
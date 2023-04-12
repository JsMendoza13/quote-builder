package com.example.app_cotizacion;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class cards extends Fragment {
    private RecyclerView mRecycler;
    private FirebaseFirestore db;
    private MaterialAdapter mAdapter;
    private Button calculate, reload;
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
        mRecycler = view.findViewById(R.id.containerRV);
        calculate = view.findViewById(R.id.calculate);
        reload = view.findViewById(R.id.reload);

        db = FirebaseFirestore.getInstance();
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = db.collection("materials");

        FirestoreRecyclerOptions<Material> options = new FirestoreRecyclerOptions
                .Builder<Material>()
                .setQuery(query, Material.class).build();

        mAdapter = new MaterialAdapter(options);
        mRecycler.setAdapter(mAdapter);

        calculate.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            total fragmentTotal = new total();
            fragmentTransaction.replace(R.id.container, fragmentTotal);
            fragmentTransaction.commit();

//            double acmTotal = 0.0;
//            for (int i = 0; i < mAdapter.getItemCount(); i++) {
//                Material material = mAdapter.getItem(i);
//                double price = Double.parseDouble(material.getMaterialPrice());
//                if (!materialCant.getText().toString().isEmpty()) {
//                    double cantidad = Double.parseDouble(materialCant.getText().toString());
//                    if (check.isChecked()) {
//                        double total = price * cantidad;
//                        acmTotal += total;
//                    }
//                }
//            }

//            Bundle bundle = new Bundle();
//            bundle.putDouble("total", acmTotal);
//
//            fragmentTotal.setArguments(bundle);
        });

        //Popup confirm-----------------------------------------------------------------------

        View popupView = getLayoutInflater().inflate(R.layout.confirm_popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);

        Button yes = popupView.findViewById(R.id.yes);
        Button no = popupView.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                cards fragmentCards = new cards();
                fragmentTransaction.replace(R.id.container, fragmentCards);
                fragmentTransaction.commit();
                Toast.makeText(getContext(), "Cargando...", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        reload.setOnClickListener(v -> {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
        db.collection("materials").whereEqualTo("isSelected", false);
    }

}
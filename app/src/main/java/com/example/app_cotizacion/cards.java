package com.example.app_cotizacion;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class cards extends Fragment {
    private RecyclerView mRecycler;
    private FirebaseFirestore db;
    private List<Material> materialList = new ArrayList<>();
    private MaterialAdapter mAdapter = new MaterialAdapter(materialList);
    private Button calculate, reload;
    private View view;
    private ArrayList<Double> materialAmountList;
    private ProgressBar progressBar;

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
        view = inflater.inflate(R.layout.fragment_cards, container, false);
        mRecycler = view.findViewById(R.id.containerRV);
        calculate = view.findViewById(R.id.calculate);
        reload = view.findViewById(R.id.reload);
        progressBar = view.findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();

//        progressBar

        String hexColor = "#e5a200";
        int color = Color.parseColor(hexColor);
        progressBar.getIndeterminateDrawable().setColorFilter( color, android.graphics.PorterDuff.Mode.MULTIPLY);

        db.collection("materials").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e != null) {
                            Log.w(TAG, "Error al cargar los materiales.", e);
                        }
                    }
                });

//        List materials

        List<Material> materialList = new ArrayList<>();
        db.collection("materials").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String materialImg = documentSnapshot.getString("materialImg");
                String materialName = documentSnapshot.getString("materialName");
                double materialPrice = documentSnapshot.getDouble("materialPrice");
                String materialStatus = documentSnapshot.getString("materialStatus");

                Material item = new Material(materialImg, materialName, materialPrice, materialStatus);
                materialList.add(item);
            }

            mAdapter = new MaterialAdapter(materialList);
            mRecycler.setAdapter(mAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecycler.setLayoutManager(layoutManager);
            mRecycler.setHasFixedSize(true);
        });

        //Calculate---------------------------------------------

        calculate.setOnClickListener(v -> {
        materialAmountList = mAdapter.getMaterialAmountList();
        double plus = 0;
        for (int i = 0; i<materialAmountList.size(); i++){
            plus += materialAmountList.get(i);
        }
        if (plus == 0.0 ){
            Toast.makeText(getContext(), "Selecciona un material y agrega un valor diferente a 0", Toast.LENGTH_SHORT).show();
        }else {
            double totalD = mAdapter.calculateTotal();
            System.out.println("ArrayAmount: " + mAdapter.getMaterialAmountList());
            System.out.println("Total: " + totalD);
            System.out.println("Elementos: " + mAdapter.getItemCount());
            total fragmentTotal = new total();
            Bundle bundle = new Bundle();
            bundle.putDouble("total", totalD);
            fragmentTotal.setArguments(bundle);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragmentTotal);
            fragmentTransaction.commit();
        }
        });

        //Popup confirm-----------------------------------------------------------------------

        View popupView = getLayoutInflater().inflate(R.layout.confirm_popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);

        Button yes = popupView.findViewById(R.id.yes);
        Button no = popupView.findViewById(R.id.no);

        yes.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            cards fragmentCards = new cards();
            fragmentTransaction.replace(R.id.container, fragmentCards);
            fragmentTransaction.commit();

            popupWindow.dismiss();
        });

        no.setOnClickListener(v -> popupWindow.dismiss());

        reload.setOnClickListener(v -> popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0));

        return view;
    }
}
package com.example.app_cotizacion;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
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
import com.google.protobuf.LazyStringArrayList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class cards extends Fragment {
    private RecyclerView mRecycler;
    private FirebaseFirestore db;
    private List<Material> materialList = new ArrayList<>();
    private MaterialAdapter mAdapter = new MaterialAdapter(materialList);
    private Button calculate, reload, view_more, close;
    private View view;
    private ArrayList<Double> materialAmountList;
    private ProgressBar progressBar;
    private TextView val_total;
    private ArrayList<String> materialNameArray = new ArrayList<>();
    private ArrayList<Double> materialPriceArray = new ArrayList<>();
    private double totalD, materialPrice;
    private String materialName;
    private  boolean[] selectedItems;
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<Double> priceArray = new ArrayList<>();
    public ArrayList<String> getMaterialNameArray() { return materialNameArray; }
    public ArrayList<Double> getMaterialPriceArray() { return materialPriceArray; }

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

        db.collection("materials").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String materialImg = documentSnapshot.getString("materialImg");
                materialName = documentSnapshot.getString("materialName");
                materialPrice = documentSnapshot.getDouble("materialPrice");
                String materialStatus = documentSnapshot.getString("materialStatus");

                Material item = new Material(materialImg, materialName, materialPrice, materialStatus);
                nameArray.add(materialName);
                priceArray.add(materialPrice);
                materialList.add(item);
            }

            mAdapter = new MaterialAdapter(materialList);
            mRecycler.setAdapter(mAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecycler.setLayoutManager(layoutManager);
            mRecycler.setHasFixedSize(true);
        });

        //Popup confirm reload-----------------------------------------------------------------------

        View popupView = getLayoutInflater().inflate(R.layout.confirm_popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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

//        popup total

        View popupViewT = getLayoutInflater().inflate(R.layout.total_popup, null);

        PopupWindow popupWindowT = new PopupWindow(popupViewT,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindowT.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);


        val_total = popupViewT.findViewById(R.id.val_total);
        view_more = popupViewT.findViewById(R.id.view_more);
        close = popupViewT.findViewById(R.id.close);

        view_more.setOnClickListener(v -> {
            materialNameArray.clear();
            materialPriceArray.clear();
            for (int i = 0; i < selectedItems.length; i++) {
                materialNameArray.add("");
                materialPriceArray.add(0.0);
            }
            System.out.println("SelectedItems "+ Arrays.toString(mAdapter.getMaterialSelectedList().toArray()));
            for (int i = 0; i < selectedItems.length; i++) {
                if (materialAmountList.get(i) != 0.0) {
                    materialNameArray.set(i, nameArray.get(i));
                    materialPriceArray.set(i, priceArray.get(i));
                }
            }
            System.out.println("Table");
            System.out.println("Nombres: " + getMaterialNameArray());
            System.out.println("Cantidades "+mAdapter.getMaterialAmountList());
            System.out.println("Precio "+getMaterialPriceArray());
            System.out.println("total "+mAdapter.getMaterialTotalPrice());

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("names", getMaterialNameArray());
            double[] amountArray = new double[mAdapter.getMaterialAmountList().size()];
            double[] priceArray = new double[getMaterialPriceArray().size()];
            double[] totalArray = new double[mAdapter.getMaterialTotalPrice().size()];
            for (int i = 0; i < amountArray.length; i++) {
                amountArray[i] = mAdapter.getMaterialAmountList().get(i);
                priceArray[i] = getMaterialPriceArray().get(i);
                totalArray[i] = mAdapter.getMaterialTotalPrice().get(i);
            }
            bundle.putDouble("total", totalD);
            bundle.putDoubleArray("amounts", amountArray);
            bundle.putDoubleArray("prices", priceArray);
            bundle.putDoubleArray("totals", totalArray);
            total fragmentTotal = new total();
            fragmentTotal.setArguments(bundle);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragmentTotal);
            fragmentTransaction.commit();

            popupWindowT.dismiss();
        });

        close.setOnClickListener(v -> popupWindowT.dismiss());

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
                totalD = mAdapter.calculateTotal();
                System.out.println("ArrayAmount: " + mAdapter.getMaterialAmountList());
                System.out.println("Total: " + totalD);
                System.out.println("Elementos: " + mAdapter.getItemCount());

                val_total.setText("$"+String.valueOf(totalD));
//                ------------------------------------------------------
                selectedItems = new boolean [mAdapter.getMaterialSelectedList().size()];
                for (int i = 0; i < selectedItems.length; i++) {
                    selectedItems[i] = mAdapter.getMaterialSelectedList().get(i);
                }

                popupWindowT.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        return view;
    }
}
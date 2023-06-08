package com.example.app_cotizacion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class total extends Fragment {
    private View view;
    private TextView total, confirm_text;
    private Button newQuoteBuild, saveQuoteBuild;
    private TableLayout table;
    private cards cards = new cards();
    private ArrayList<String> materialNameList = new ArrayList<>();
    private double[] materialAmountArray;
    private double[] materialPriceArray;
    private double[] materialTotalPriceA;
    private String totalAll;
    private DecimalFormat  doubleFormat = new DecimalFormat("#.##");
    List<String> nameList = new ArrayList<>();
    List<Double> amountList = new ArrayList<>(), priceList = new ArrayList<>(), totalList = new ArrayList<>();
    private List<Material> materialList = new ArrayList<>();
    private MaterialAdapter adapter = new MaterialAdapter(materialList);

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

        total = view.findViewById(R.id.total);
        newQuoteBuild = view.findViewById(R.id.newQuoteBuild);
        table = view.findViewById(R.id.table);
        saveQuoteBuild = view.findViewById(R.id.SaveQuoteBuild);

//        TableLayout--------------------------------------------

       Bundle bundle = getArguments();
       if (bundle != null) {
           materialNameList = bundle.getStringArrayList("names");
           materialAmountArray = bundle.getDoubleArray("amounts");
           materialPriceArray = bundle.getDoubleArray("prices");
           materialTotalPriceA = bundle.getDoubleArray("totals");
           totalAll = bundle.getString("total");
       }

        System.out.println("Names "+materialNameList);
        System.out.println("amount "+ Arrays.toString(materialAmountArray));
        System.out.println("price "+Arrays.toString(materialPriceArray));
        System.out.println("total "+Arrays.toString(materialTotalPriceA));

        total.setText("$"+totalAll);
        System.out.println("totalAll "+totalAll);

        for (int i = 0; i < materialNameList.size(); i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            TextView name = new TextView(getContext()), amount = new TextView(getContext()), price = new TextView(getContext()), total_price = new TextView(getContext());
            name.setTextColor(Color.WHITE);
            amount.setTextColor(Color.WHITE);
            price.setTextColor(Color.WHITE);
            total_price.setTextColor(Color.WHITE);
            name.setPadding(15, 4, 10,0);
            amount.setPadding(15, 4, 10,0);
            price.setPadding(15, 4, 10,0);
            total_price.setPadding(15, 4, 10,0);

            if (!(materialNameList.get(i) == "" || materialAmountArray[i] == 0.0 || materialPriceArray[i] == 0.0 || materialTotalPriceA[i] == 0.0)) {
                name.setText(String.valueOf(materialNameList.get(i)));
                tableRow.addView(name);
                amount.setText(doubleFormat.format(materialAmountArray[i]));
                tableRow.addView(amount);
                price.setText("$"+doubleFormat.format(materialPriceArray[i]));
                tableRow.addView(price);
                total_price.setText("$"+doubleFormat.format(materialTotalPriceA[i]));
                tableRow.addView(total_price);
                table.addView(tableRow);

                nameList.add(materialNameList.get(i));
                amountList.add(materialAmountArray[i]);
                priceList.add(materialPriceArray[i]);
                totalList.add(materialTotalPriceA[i]);
            }
        }

        //Popup confirm new quote build-----------------------------------------------------------------------

        View popupView2 = getLayoutInflater().inflate(R.layout.confirm_popup, null);

        PopupWindow popupWindow2 = new PopupWindow(popupView2,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow2.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);

        Button yes = popupView2.findViewById(R.id.yes);
        Button no = popupView2.findViewById(R.id.no);
        confirm_text = popupView2.findViewById(R.id.confirm_text);

        confirm_text.setText("¿Realizar nueva cotización?");

        yes.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            cards fragmentCards = new cards();
            fragmentTransaction.replace(R.id.container, fragmentCards);
            fragmentTransaction.commit();

            popupWindow2.dismiss();
        });

        no.setOnClickListener(v -> popupWindow2.dismiss());

        newQuoteBuild.setOnClickListener(v -> popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0));

//        SaveQB

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        LayoutInflater inflater1 = getActivity().getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.popup_save_qb, null);
        EditText saveName = view1.findViewById(R.id.nameQB);
        Button save = view1.findViewById(R.id.saveQB);
        Button cancel = view1.findViewById(R.id.cancel);
        alert.setView(view1);
        AlertDialog alertDialog = alert.create();

        cancel.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            alertDialog.dismiss();
        });

        save.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String userID = firebaseAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference saveQBdocRef = db.collection("saveQB").document(userID);

            String edSaveName = saveName.getText().toString();

            if (!edSaveName.isEmpty()) {
                Map<String, Object> logs = new HashMap<>();
                List<Map<String, Object>> logsArray = new ArrayList<>();

                for (int i = 0; i < nameList.size(); i++) {
                    Map<String, Object> log = new HashMap<>();
                    log.put("name", nameList.get(i));
                    log.put("amount", amountList.get(i));
                    log.put("price", priceList.get(i));
                    log.put("totalXmaterial", totalList.get(i));
                    logsArray.add(log);
                }
                System.out.println("nameList"+nameList);
                System.out.println("amountList"+amountList);
                System.out.println("priceList"+priceList);
                System.out.println("totalxmaterial"+totalList);

                logs.put("total", totalAll);
                logs.put(edSaveName, logsArray);

                saveQBdocRef.collection("logs").add(logs)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Guardado correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error al guardar.", Toast.LENGTH_SHORT).show();
                            }
                        });
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                alertDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Debe agregar un nombre para continuar.", Toast.LENGTH_SHORT).show();
            }
        });

        saveQuoteBuild.setOnClickListener(v -> {
            alertDialog.show();
        });

        return view;
    }

}
package com.example.app_cotizacion;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class total extends Fragment {
    private View view;
    private TextView total, confirm_text;
    private Button newQuoteBuild;
    private TableLayout table;
    private cards cards = new cards();
    private ArrayList<String> materialNameList = new ArrayList<>();
    private double[] materialAmountArray;
    private double[] materialPriceArray;
    private double[] materialTotalPriceA;
    private double totalAll;
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

//        TableLayout--------------------------------------------

       Bundle bundle = getArguments();
       if (bundle != null) {
           materialNameList = bundle.getStringArrayList("names");
           materialAmountArray = bundle.getDoubleArray("amounts");
           materialPriceArray = bundle.getDoubleArray("prices");
           materialTotalPriceA = bundle.getDoubleArray("totals");
           totalAll = bundle.getDouble("total");
       }

        System.out.println("Names "+materialNameList);
        System.out.println("amount "+ Arrays.toString(materialAmountArray));
        System.out.println("price "+Arrays.toString(materialPriceArray));
        System.out.println("total "+Arrays.toString(materialTotalPriceA));

        total.setText("$"+String.valueOf(totalAll));
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

            if (!(materialNameList.get(i) == "" && materialAmountArray[i] == 0.0 && materialPriceArray[i] == 0.0 && materialTotalPriceA[i] == 0.0)) {
                name.setText(String.valueOf(materialNameList.get(i)));
                tableRow.addView(name);
                amount.setText(String.valueOf(materialAmountArray[i]));
                tableRow.addView(amount);
                price.setText("$"+String.valueOf(materialPriceArray[i]));
                tableRow.addView(price);
                total_price.setText("$"+String.valueOf(materialTotalPriceA[i]));
                tableRow.addView(total_price);
                table.addView(tableRow);
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

        return view;
    }

}
package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Locale;

public class total extends Fragment {
    View view;
    TextView total, confirm_text;
    Button newQuoteBuild;
    private List<Material> materialList;
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

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("total")) {
            double totalD = arguments.getDouble("total");
            total.setText(String.valueOf(totalD));
        }

        //Popup confirm-----------------------------------------------------------------------

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
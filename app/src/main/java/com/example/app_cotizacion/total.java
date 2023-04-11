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

        if (getArguments() != null) {
            acmTotal = getArguments().getDouble("total");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_total, container, false);

        total = view.findViewById(R.id.total);
        nuevo = view.findViewById(R.id.nuevo);

        total.setText(String.format(Locale.getDefault(), "%.2f", acmTotal));

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
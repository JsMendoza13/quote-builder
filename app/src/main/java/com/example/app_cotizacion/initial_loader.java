package com.example.app_cotizacion;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
public class initial_loader extends Fragment {

    public initial_loader() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String hexColor = "#e8caaf";
        int color = Color.parseColor(hexColor);
        ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);

        // Inflate the layout for this fragment
        View vieww = inflater.inflate(R.layout.fragment_initial_loader, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ProgressBar v = (ProgressBar) vieww.findViewById(R.id.progress); v.getIndeterminateDrawable().setColorFilter( color, android.graphics.PorterDuff.Mode.MULTIPLY);

        return vieww;
    }
}
package com.example.app_cotizacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;


public class initial_loader_Activity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_loader);

        getSupportActionBar().hide();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(initial_loader_Activity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },2000);


        String hexColor = "#e5a200";
        int color = Color.parseColor(hexColor);

        // Inflate the layout for this fragment


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ProgressBar v = findViewById(R.id.progress);
        v.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);


    }

}
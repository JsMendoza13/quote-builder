package com.example.app_cotizacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.annotation.Repeatable;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                app_introduction fragmentApp_introduction = new app_introduction();
                fragmentTransaction.replace(R.id.container, fragmentApp_introduction);
                fragmentTransaction.commit();
            }
        },2000);
    }

    @Override
    public void onDestroy() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference materialsRef = db.collection("materials");
        Query query2 = materialsRef.whereEqualTo("isSelected", true);
        query2.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                // Update the "isSelected" field to true
                materialsRef.document(document.getId()).update("isSelected", false);
            }
        });
        super.onDestroy();
    }
}
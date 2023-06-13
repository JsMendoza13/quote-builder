package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_cotizacion.adapter.LogCadapter;
import com.example.app_cotizacion.adapter.LogsQBAdapter;
import com.example.app_cotizacion.model.LogContainer;
import com.example.app_cotizacion.model.LogsQB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class logs_view extends Fragment {
    private RecyclerView recyclerViewContainer;
    private View view;

    private LogCadapter logCadapter;
    private List<LogContainer> logContainers;


    public logs_view() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_logs_view, container, false);
        View viewRecyclerLA = inflater.inflate(R.layout.logs_qb_cards, container, false);


        recyclerViewContainer = view.findViewById(R.id.containerRV2);
        recyclerViewContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        logContainers = new ArrayList<>();
        logCadapter = new LogCadapter(logContainers);
        recyclerViewContainer.setAdapter(logCadapter);

        recyclerViewContainer.setItemAnimator(new DefaultItemAnimator());

        fetchDataContainer();

        return view;
    }

    public void fetchDataContainer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        db.collection("saveQB")
                .document(userID)
                .collection("logs")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String nameC = documentSnapshot.getString("logName");
                            String totalC = documentSnapshot.getString("total");

                            LogContainer item = new LogContainer(nameC, totalC);
                            logContainers.add(item);
                        }
                        logCadapter.notifyDataSetChanged();
                    }
                });
    }
}
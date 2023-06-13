package com.example.app_cotizacion.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_cotizacion.R;
import com.example.app_cotizacion.model.LogContainer;
import com.example.app_cotizacion.model.LogsQB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogCadapter extends RecyclerView.Adapter<LogCadapter.LogCviewHolder> {
    private List<LogContainer> logContainers;
    private LogsQBAdapter logsQBAdapter;
    private List<LogsQB> logsQBList;
    private RecyclerView recyclerView;
    private View itemView;

    public LogCadapter(List<LogContainer> logContainers) {
        this.logContainers = logContainers;
    }

    @NonNull
    @Override
    public LogCviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logs_qb_cards, parent, false);
        recyclerView = itemView.findViewById(R.id.recyclerViewLA);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        logsQBList = new ArrayList<>();
        logsQBAdapter = new LogsQBAdapter(logsQBList);
        recyclerView.setAdapter(logsQBAdapter);

        fetchDataFromFirestore();
        return new LogCviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogCadapter.LogCviewHolder holder, int position) {
        LogContainer logContainer = logContainers.get(position);
        holder.nameC.setText(logContainer.getNameC());
        holder.totalC.setText("$"+logContainer.getTotalC());
    }

    @Override
    public int getItemCount() {
        return logContainers.size();
    }

    public static class LogCviewHolder extends RecyclerView.ViewHolder {
        public TextView nameC, totalC;
        public LogCviewHolder(View itemView){
            super(itemView);
            nameC = itemView.findViewById(R.id.logName);
            totalC = itemView.findViewById(R.id.Total);
        }
    }

    public void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        db.collection("saveQB")
                .document(userID)
                .collection("logs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<Map<String, Object>> logsArray = (List<Map<String, Object>>) document.get("logsArray");
                                if (logsArray != null) {
                                    for (Map<String, Object> entry : logsArray) {
                                        double amount = (Double) entry.get("amount");
                                        String img = (String) entry.get("img");
                                        String name = (String) entry.get("name");
                                        double price = (Double) entry.get("price");
                                        double totalXmaterial = (Double) entry.get("totalXmaterial");

                                        LogsQB logsQB = new LogsQB(amount, img, name, price, totalXmaterial);
                                        logsQBList.add(logsQB);
                                    }
                                } else {
                                    Toast.makeText(itemView.getContext(), "No hay registros disponibles.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            logsQBAdapter.notifyDataSetChanged();
                        } else {
                            System.out.println("Error al cargar los registros.");
                        }
                    }
                });
    }
}

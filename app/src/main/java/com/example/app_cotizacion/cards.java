package com.example.app_cotizacion;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.app_cotizacion.adapter.MaterialAdapter;
import com.example.app_cotizacion.model.Material;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cards extends Fragment {
    private RecyclerView mRecycler;
    private FirebaseFirestore db;
    private List<Material> materialList = new ArrayList<>();
    private MaterialAdapter mAdapter = new MaterialAdapter(materialList);
    private Button calculate, reload, view_more, close ;
    private View view;
    private ArrayList<Double> materialAmountList;
    private ProgressBar progressBar;
    private TextView val_total;
    private ArrayList<String> materialNameArray = new ArrayList<>();
    private ArrayList<Double> materialPriceArray = new ArrayList<>();
    private double materialPrice;
    private String materialName, totalS;
    private  boolean[] selectedItems;
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<Double> priceArray = new ArrayList<>();
    public ArrayList<String> getMaterialNameArray() { return materialNameArray; }
    public ArrayList<Double> getMaterialPriceArray() { return materialPriceArray; }
    private ImageView back, profile;


    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;


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
        back = view.findViewById(R.id.back);
        profile = view.findViewById(R.id.profile);


        View popupViewProfile = getLayoutInflater().inflate(R.layout.profile_popup,  null);

        PopupWindow popupWindowProfile = new PopupWindow(popupViewProfile,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindowProfile.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView view_profile = popupViewProfile.findViewById(R.id.view_profile);
        TextView view_quote = popupViewProfile.findViewById(R.id.view_quote);
        TextView logout = popupViewProfile.findViewById(R.id.logout);
        Button close_profile = popupViewProfile.findViewById(R.id.close_profile);


        view_quote.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            view_quote fragment_viewQ = new view_quote();
            fragmentTransaction.replace(R.id.container, fragment_viewQ);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        close_profile.setOnClickListener(view1 -> {
            popupWindowProfile.dismiss();
        });
        profile.setOnClickListener(view1 -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);

            popupWindowProfile.showAtLocation(view, Gravity.CENTER, 0, 0);


        });



        back.setOnClickListener(view1 -> {goBack();});

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // PopUp Data User
        View popupProfile = getLayoutInflater().inflate(R.layout.profile_data_popup, null);

        PopupWindow popupWindowProfileUser = new PopupWindow(popupProfile,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindowProfileUser.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        Button close_user = popupProfile.findViewById(R.id.BtnClose_user);


        close_user.setOnClickListener(view1 -> {

            popupWindowProfileUser.dismiss();
            popupWindowProfile.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        view_profile.setOnClickListener(view1 ->  {
            popupWindowProfile.dismiss();
            popupWindowProfileUser.showAtLocation(view, Gravity.CENTER, 0, 0);

            TextView name_user = popupProfile.findViewById(R.id.name_user);
            TextView email_user = popupProfile.findViewById(R.id.email_user);
            TextView answer_user = popupProfile.findViewById(R.id.answerLastname);
            TextView answer_phone = popupProfile.findViewById(R.id.answerPhone);

            FirebaseUser user = mAuth.getCurrentUser();
            String userEmail = user.getUid();
            DocumentReference documentReference = db.collection("Users").document(userEmail);

            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    name_user.setText(documentSnapshot.getString("Name"));
                    email_user.setText(documentSnapshot.getString("Email"));
                    answer_user.setText(documentSnapshot.getString("LastName"));
                    answer_phone.setText(documentSnapshot.getString("Phone"));
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Existe un error para cargar los datos...", Toast.LENGTH_SHORT).show();
            });

        });

        //confirm logout

        View pvConfirmLogout = getLayoutInflater().inflate(R.layout.confirm_popup, null);
        PopupWindow pwConfirmLogout = new PopupWindow(pvConfirmLogout,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        pwConfirmLogout.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        TextView confirmTxt = pvConfirmLogout.findViewById(R.id.confirm_text);
        Button clYes = pvConfirmLogout.findViewById(R.id.yes);
        Button clNo = pvConfirmLogout.findViewById(R.id.no);

        confirmTxt.setText("¿Desea cerrar sesión?");

        clYes.setOnClickListener(v -> {
            mAuth.signOut();
            pwConfirmLogout.dismiss();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            login fragment_login = new login();
            fragmentTransaction.replace(R.id.container, fragment_login);
            fragmentTransaction.commit();
        });

        clNo.setOnClickListener(v ->{
            pwConfirmLogout.dismiss();
            popupWindowProfile.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        //Sign Out

        logout.setOnClickListener(view1 -> {
            popupWindowProfile.dismiss();
            pwConfirmLogout.showAtLocation(view, Gravity.CENTER, 0, 0);
        });



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
            bundle.putString("total", totalS);
            bundle.putDoubleArray("amounts", amountArray);
            bundle.putDoubleArray("prices", priceArray);
            bundle.putDoubleArray("totals", totalArray);
            total fragmentTotal = new total();
            fragmentTotal.setArguments(bundle);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container2, fragmentTotal);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


            popupWindowT.dismiss();
        });

        close.setOnClickListener(v -> popupWindowT.dismiss());


        //Calculate---------------------------------------------

        calculate.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            materialAmountList = mAdapter.getMaterialAmountList();
            double plus = 0;
            for (int i = 0; i<materialAmountList.size(); i++){
                plus += materialAmountList.get(i);
            }
            if (plus == 0.0 ){
                Toast.makeText(getContext(), "Selecciona un material y agrega un valor diferente a 0", Toast.LENGTH_SHORT).show();
            }else {
                totalS = mAdapter.calculateTotal();
                System.out.println("ArrayAmount: " + mAdapter.getMaterialAmountList());
                System.out.println("Total: " + totalS);
                System.out.println("Elementos: " + mAdapter.getItemCount());

                val_total.setText("$"+totalS);
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
    private void goBack() {
        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }
}
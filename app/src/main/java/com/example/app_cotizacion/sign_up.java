package com.example.app_cotizacion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_up extends DialogFragment {

    EditText inputNombre, inputApellido, inputTelefono, inputCorreo, inputPass, inputpassTwo;
    Button btnRegistro;

    private FirebaseAuth mAuth;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();

        inputNombre = view.findViewById(R.id.editNombre);
        inputApellido = view.findViewById(R.id.editApellido);
        inputTelefono = view.findViewById(R.id.editTelefono);
        inputCorreo = view.findViewById(R.id.editCorreo);
        inputPass = view.findViewById(R.id.editPass);
        inputpassTwo = view.findViewById(R.id.editPass2);
        btnRegistro = view.findViewById(R.id.btnRegistro);

//        popUp

        View popupView = getLayoutInflater().inflate(R.layout.confirm_register, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnContinue = popupView.findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            app_introduction fragment_login = new app_introduction();
            fragmentTransaction.replace(R.id.container, fragment_login);
            fragmentTransaction.commit();
            popupWindow.dismiss();
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = inputNombre.getText().toString().trim();
                String lastUser = inputApellido.getText().toString().trim();
                String phoneUser = inputTelefono.getText().toString().trim();
                String emailUser = inputCorreo.getText().toString().trim();
                String passUser = inputPass.getText().toString().trim();
                String passUserTwo = inputpassTwo.getText().toString().trim();

                if (nameUser.isEmpty()) {
                    showError(inputNombre, "Ingrese su nombre");
                    inputNombre.requestFocus();
                } else if (lastUser.isEmpty()) {
                    showError(inputApellido, "Ingrese su apellido");
                    inputApellido.requestFocus();
                } else if (phoneUser.isEmpty() || phoneUser.length() < 7) {
                    showError(inputTelefono, "Ingrese un telefono correcto");
                    inputTelefono.requestFocus();
                } else if (emailUser.isEmpty() || !emailUser.contains("@") || !emailUser.contains(".com")) {
                    showError(inputCorreo, "Ingrese un correo valido");
                    inputCorreo.requestFocus();
                } else if (passUser.isEmpty() || passUser.length() < 5 )  {
                    showError(inputPass, "Ingrese una contraseña valida");
                    inputPass.requestFocus();
                } else if (passUserTwo.isEmpty() || !passUserTwo.equals(passUser)) {
                    showError(inputpassTwo, "Contraseña incorrecta, No coinciden ");
                    inputpassTwo.requestFocus();

                } else {
                    mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                            } else {
                                Toast.makeText(getContext(), "No se pudo registrar :( ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}


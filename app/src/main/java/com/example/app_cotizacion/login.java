package com.example.app_cotizacion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends Fragment {
    View view;

    EditText inputEmail, inputPass;

    Button register, login;

    FirebaseAuth mAuth;

    public login() {
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
        view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();

        inputEmail = view.findViewById(R.id.editCorreo);
        inputPass = view.findViewById(R.id.editPass);
        login = view.findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = inputEmail.getText().toString().trim();
                String userPassword = inputPass.getText().toString().trim();

                if (userEmail.isEmpty() || !userEmail.contains("@") || !userEmail.contains(".com")) {
                    showError(inputEmail, "Ingrese un correo valido");
                    inputEmail.requestFocus();
                } else if  (userPassword.isEmpty() || userPassword.length() < 5 )  {
                    showError(inputPass, "Ingrese una contraseña valida");
                    inputPass.requestFocus();
                }else {
                    //Inicio de sesión
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Ir al introduction despues de iniciar
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                app_introduction fragment_app_introduction = new app_introduction();
                                fragmentTransaction.replace(R.id.container, fragment_app_introduction);
                                fragmentTransaction.commit();
                            }else {
                                Toast.makeText(getContext(), "No se pudo iniciar sesión... verifique correo/contraseña. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        register = view.findViewById(R.id.register);
        register.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            sign_up fragment_sign_up = new sign_up();
            fragmentTransaction.replace(R.id.container, fragment_sign_up);
            fragmentTransaction.commit();
        });
        return view;
    }


    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    //Este metodo es para que el usuario si ya esta logueado, No vuelva a iniciar sesión
    @Override
    public void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            app_introduction fragment_app_introduction = new app_introduction();
            fragmentTransaction.replace(R.id.container, fragment_app_introduction);
            fragmentTransaction.commit();
        }
        super.onStart();
    }


}
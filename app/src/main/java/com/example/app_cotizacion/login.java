package com.example.app_cotizacion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.app_cotizacion.app_introduction;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends Fragment {
    private static final int RC_SIGN_IN = 1;
    String TAG = "GoogleSignInLogin";
    View view;

    EditText inputEmail, inputPass;
    Button register, login;
    ImageButton BtnGoogle;

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        inputEmail = view.findViewById(R.id.editCorreo);
        inputPass = view.findViewById(R.id.editPass);
        login = view.findViewById(R.id.btnLogin);
        BtnGoogle = view.findViewById(R.id.BtnGoogleSign);

        mAuth = FirebaseAuth.getInstance();

        // Configurar Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Crear un GoogleSignInClient con las opciones especificadas por gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = inputEmail.getText().toString().trim();
                String userPassword = inputPass.getText().toString().trim();

                if (userEmail.isEmpty() || !userEmail.contains("@") || !userEmail.contains(".com")) {
                    showError(inputEmail, "Ingrese un correo valido");
                    inputEmail.requestFocus();
                } else if (userPassword.isEmpty() || userPassword.length() < 5) {
                    showError(inputPass, "Ingrese una contraseña valida");
                    inputPass.requestFocus();
                } else {
                    // Inicio de sesión
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Ir al introduction después de iniciar sesión
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                app_introduction fragment_app_introduction = new app_introduction();
                                fragmentTransaction.replace(R.id.container, fragment_app_introduction);
                                fragmentTransaction.commit();
                            } else {
                                Toast.makeText(getContext(), "No se pudo iniciar sesión... verifique correo/contraseña. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // Función del botón de inicio de sesión con Google
        BtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
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

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success");
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        app_introduction fragment_app_introduction = new app_introduction();
                                        fragmentTransaction.replace(R.id.container, fragment_app_introduction);
                                        fragmentTransaction.commit();
                                    } else {
                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        Toast.makeText(getContext(), "No se pudo iniciar sesión con Google. ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            } catch (ApiException e) {
                e.printStackTrace();
                Log.w(TAG, "signInWithCredential:failure", e);
                Toast.makeText(getContext(), "No se pudo iniciar sesión con Google. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Este método muestra un error en un EditText
    private void showError(EditText input, String errorMessage) {
        input.setError(errorMessage);
        input.requestFocus();
    }

    // Este método verifica si el usuario ya ha iniciado sesión y lo redirige a la pantalla de introducción
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            app_introduction fragment_app_introduction = new app_introduction();
            fragmentTransaction.replace(R.id.container, fragment_app_introduction);
            fragmentTransaction.commit();
        }
    }
}

package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class sign_up extends AppCompatActivity {

    //Agregado
    private static final int RC_SING_IN = 9001;
    EditText mSignupemail,mSigupPassword;
    Button mSignupButton;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient clienteGoogle;

    /////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView login = findViewById(R.id.textLogIn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });


        //AGREGADO ()
        //getSupportActionBar().hide();

        mSignupemail = findViewById(R.id.textMailSignUp);
        mSigupPassword = findViewById(R.id.textPasswordSignUp);
        mSignupButton = findViewById(R.id.signButton);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        clienteGoogle = GoogleSignIn.getClient(this,gso);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = mSignupemail.getText().toString().trim();
                String password = mSigupPassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();

                }
                else if(password.length()<7)
                {
                    Toast.makeText(getApplicationContext(), "La contraseña debe contener 7 caracteres como minimo", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Registro del usuario en firebase

                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Error al registrarse", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }



            }
        });



    }

    //Send email verification
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Correo de verificacion enviado, Verifique e Ingrese de nuevo", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(sign_up.this, MainActivity.class));
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error al enviar correo de verificacion", Toast.LENGTH_SHORT).show();
        }
    }





    //Autenticacion con google
    public void registroGoogle(View v){
        Intent intent = clienteGoogle.getSignInIntent();
        startActivityForResult(intent,RC_SING_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SING_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                Log.d("TOKENAUTENTICACION","ERROR AL CONECTARSE");
                FirebaseAuthWhithGoogle(cuenta.getIdToken());
                Toast.makeText(this, cuenta.getId(), Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void FirebaseAuthWhithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(sign_up.this, "USUARIO CREADO", Toast.LENGTH_SHORT).show();
                        FirebaseUser usuario = firebaseAuth.getCurrentUser();
                        //respuesta.setText("Bienvenido "+usuario.getEmail());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sign_up.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }






}





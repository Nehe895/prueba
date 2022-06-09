package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    //Agregado
    private static final int RC_SING_IN = 9001;

    EditText mLoginemail,mLoginPassword;
    TextView mForgetPassword;
    Button mLogbutton;
    ImageButton googleButton;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient clienteGoogle;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView btnSignUp = findViewById(R.id.textSignUp);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, sign_up.class);
                startActivity(intent);
            }
        });


        //AGREGADO ()
        //getSupportActionBar().hide();

        mLoginemail = findViewById(R.id.textMail);
        mLoginPassword = findViewById(R.id.passText);
        mForgetPassword = findViewById(R.id.forgetPassword);
        mLogbutton = findViewById(R.id.logButton);
        googleButton = findViewById(R.id.googleButton);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //Regresa al pantalla principal si ya se encuentra iniciado con una cuenta
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,forgotPassword.class));
        }

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, forgotPassword.class);
                startActivity(intent);
            }
        });

        //Log In usuario
        mLogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mLoginemail.getText().toString().trim();
                String password = mLoginPassword.getText().toString().trim();

                if(mail.isEmpty()||password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Rellene todas las casillas", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Log in del usuario


                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                checkmailverificaction();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"La cuenta no existe o datos incorrectos",Toast.LENGTH_SHORT).show();

                            }


                        }
                    });


                }
            }
        });






        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


    }














    private void checkmailverificaction()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(getApplicationContext(), "Inicio Exitoso", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,forgotPassword.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Verifique primero su email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}






package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    EditText mForgotpassword;
    Button mPasswordrecoverbutton;
    TextView mGobacktologin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //getSupportActionBar().hide();

        mForgotpassword = findViewById(R.id.forgotpassword);
        mPasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton);
        mGobacktologin = findViewById(R.id.gobacklogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mGobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotPassword.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });


        mPasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mForgotpassword.getText().toString().trim();
                if (mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Ingrese su correo", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Envio del correo para recuperacion del correo

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Correo enviado, puede recuperar su correo usando el mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotPassword.this,MainActivity.class));

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Email incorrecto o cuenta no existente", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}
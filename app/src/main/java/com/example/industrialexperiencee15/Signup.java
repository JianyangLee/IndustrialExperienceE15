package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    private EditText email,password;
    private Button btnRegister;
    FirebaseAuth mFirebaseAuth;
    ProgressBar pgbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.register);
        pgbar = findViewById(R.id.progress);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                pgbar.setVisibility(View.VISIBLE);
                Toast.makeText(Signup.this,"Please wait",Toast.LENGTH_LONG).show();

                if (emailValue.isEmpty()){
                    email.setError("Please enter email, it cannot be empty!");
                    pgbar.setVisibility(View.GONE);
                    email.requestFocus();
                }
                else if (passwordValue.isEmpty()){
                    password.setError("Please enter password, it cannot be empty!");
                    pgbar.setVisibility(View.GONE);
                    password.requestFocus();
                }
                else if (emailValue.isEmpty() && passwordValue.isEmpty()){
                    Toast.makeText(Signup.this,"Both fields need to be filled",Toast.LENGTH_LONG).show();
                    pgbar.setVisibility(View.GONE);
                }
                else if (!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                pgbar.setVisibility(View.GONE);
                                Toast.makeText(Signup.this,"Unsuccessful register, your password is too short (more than 6) or you cannot have space in username, try again!",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent goToOnBoardS1 = new Intent(Signup.this, OnBoard_Welcome_S1.class);
                                Signup.this.startActivity(goToOnBoardS1);
                            }
                        }
                    });
                }
                else{
                    pgbar.setVisibility(View.GONE);
                    Toast.makeText(Signup.this,"Error occurred, check your network connection.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
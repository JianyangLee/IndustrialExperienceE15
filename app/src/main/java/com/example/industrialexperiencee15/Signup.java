package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    private EditText email,password;
    private Button btnRegister;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                if (emailValue.isEmpty()){
                    email.setError("Please enter email, it cannot be empty!");
                    email.requestFocus();
                }
                else if (passwordValue.isEmpty()){
                    password.setError("Please enter password, it cannot be empty!");
                    password.requestFocus();
                }
                else if (emailValue.isEmpty() && passwordValue.isEmpty()){
                    Toast.makeText(Signup.this,"Both fields need to be filled",Toast.LENGTH_LONG).show();
                }
                else if (!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(Signup.this,"Register unsuccessful or your password is too short, try again!",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent goToHome = new Intent(Signup.this, Dashboard.class);
                                Signup.this.startActivity(goToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Signup.this,"Error occurred, check your network connection.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
package com.example.industrialexperiencee15;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private VideoView videoBG;
    private EditText emailId,passwordId;
    private Button logIn, SignUpButton;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        passwordId = findViewById(R.id.password);
        logIn = findViewById(R.id.button1);
        SignUpButton = (Button) findViewById(R.id.button2);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                if ( mFirebaseUser != null){
                    Toast.makeText(MainActivity.this,"You have logged in.",Toast.LENGTH_LONG).show();
                    Intent goToHome = new Intent(MainActivity.this, Dashboard.class);
                    MainActivity.this.startActivity(goToHome);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please log in.",Toast.LENGTH_LONG).show();
                }
            }
        };

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailValue = emailId.getText().toString();
                String passwordValue = passwordId.getText().toString();

                if (emailValue.isEmpty()){
                    emailId.setError("Please enter email, it cannot be empty!");
                    emailId.requestFocus();
                }
                else if (passwordValue.isEmpty()){
                    passwordId.setError("Please enter password, it cannot be empty!");
                    passwordId.requestFocus();
                }
                else if (emailValue.isEmpty() && passwordValue.isEmpty()){
                    Toast.makeText(MainActivity.this,"Both fields need to be filled",Toast.LENGTH_LONG).show();
                }
                else if (!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if ( !task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Login error, try again.",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent goDashboard = new Intent(MainActivity.this, Dashboard.class);
                                MainActivity.this.startActivity(goDashboard);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Error occurred, check your network connection.",Toast.LENGTH_LONG).show();
                }
            }
        });

        //final Button Go = (Button) findViewById(R.id.button1);//
        videoBG = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.v1);




        videoBG.setVideoURI(uri);

        videoBG.start();

        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });


//
//        Go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent test = new Intent(MainActivity.this, Dashboard.class);
//                MainActivity.this.startActivity(test);
//            }
//        });//

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, Signup.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        videoBG.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        videoBG.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        videoBG.stopPlayback();
    }





}

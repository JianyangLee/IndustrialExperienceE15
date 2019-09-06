package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class OnBoard_Welcome_S1 extends AppCompatActivity {


    private Button btnLetsGetStarted;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__welcome__s1);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set up the Field Values
        btnLetsGetStarted =(Button)findViewById(R.id.Lets_get_Started);


        //If the user gets Started in his application
        btnLetsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                InsertUser insertUser = new InsertUser();
                insertUser.execute(userID);

                Intent intent = new Intent(OnBoard_Welcome_S1.this, OnBoard_Goal_S2.class);
                startActivity(intent);
            }
        });
    }

    private class InsertUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            User user = new User(params[0]);
            RestService.createUser(user);
            return "Create user successfully";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(OnBoard_Welcome_S1.this,response,Toast.LENGTH_LONG).show();
        }
    }
}

package com.example.industrialexperiencee15;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OnBoard_Welcome_S1 extends AppCompatActivity {


    private Button btnLetsGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__welcome__s1);

        // Set up the Field Values
        btnLetsGetStarted =(Button)findViewById(R.id.Lets_get_Started);


        //If the user gets Started in his application
        btnLetsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                Intent intent = new Intent(OnBoard_Welcome_S1.this, OnBoard_Goal_S2.class);
                startActivity(intent);
            }
        });
    }





}

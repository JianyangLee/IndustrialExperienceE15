package com.example.industrialexperiencee15;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OnBoard_Goal_S2 extends AppCompatActivity {

    private Button btnNextToActivityLevel;
    private Button btnPrevoiusToWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__goal__s2);

        // Set up the Field Values

        btnPrevoiusToWelcome =(Button)findViewById(R.id.S2PreviousButton);
        btnNextToActivityLevel =(Button)findViewById(R.id.S2Nextbutton);

        //If the user gets Started in his application
        btnPrevoiusToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                Intent intent = new Intent(OnBoard_Goal_S2.this, OnBoard_Welcome_S1.class);
                startActivity(intent);
            }
        });


        //If the user gets Started in his application
        btnNextToActivityLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                Intent intent = new Intent(OnBoard_Goal_S2.this, OnBoard_ActivityLevel_S3.class);
                startActivity(intent);
            }
        });



    }
}

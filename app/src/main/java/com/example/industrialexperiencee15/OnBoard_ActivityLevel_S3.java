package com.example.industrialexperiencee15;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OnBoard_ActivityLevel_S3 extends AppCompatActivity {
    private Button btnNextToActivityLevel;
    private Button btnPrevoiusToGoal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__level__s3);


        // Set up the Field Values

        btnPrevoiusToGoal =(Button)findViewById(R.id.S3PreviousButton);
        btnNextToActivityLevel =(Button)findViewById(R.id.S3Nextbutton);

        //If the user gets Started in his application
        btnPrevoiusToGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                Intent intent = new Intent(OnBoard_ActivityLevel_S3.this, OnBoard_Goal_S2.class);
                startActivity(intent);
            }
        });


//        //If the user gets Started in his application
//        btnNextToActivityLevel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Redirect
//                Intent intent = new Intent(.this, OnBoard_ActivityLevel_S3.class);
//                startActivity(intent);
//            }
//        });



    }
}

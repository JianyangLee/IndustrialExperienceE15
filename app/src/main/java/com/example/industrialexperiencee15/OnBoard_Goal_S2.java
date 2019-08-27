package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OnBoard_Goal_S2 extends AppCompatActivity {

    private Button btnNextToActivityLevel;
    private Button btnPrevoiusToWelcome;
    //Radio Buttons
    private RadioGroup radioGoalGroup;
    private RadioButton goalOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__goal__s2);

        // Set up the Field Values
        btnPrevoiusToWelcome = (Button) findViewById(R.id.S2PreviousButton);
        btnNextToActivityLevel = (Button) findViewById(R.id.S2Nextbutton);
        radioGoalGroup = (RadioGroup) findViewById(R.id.radio_goal);

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
                //Radio Button Gender
                int buttonSelected = radioGoalGroup.getCheckedRadioButtonId();
                goalOfUser = (RadioButton) findViewById(buttonSelected);
                //Redirect
                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                //Storing the User Input
                if (getString(R.string.weightLossOption).equals(goalOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isAimingToLoseWeight", true);
                }else{
                    userSharedPreferenceEditor.putBoolean("isAimingToLoseWeight", false);
                }

                if (getString(R.string.weightMaintainOption).equals(goalOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isAimingToMaintainWeight", true);
                }else {
                    userSharedPreferenceEditor.putBoolean("isAimingToMaintainWeight", false);
                      }

                 if (getString(R.string.weightGainOption).equals(goalOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isAimingToGainWeight", true);
                }else{
                     userSharedPreferenceEditor.putBoolean("isAimingToGainWeight", false);
                 }

                userSharedPreferenceEditor.apply();
                Intent intent = new Intent(OnBoard_Goal_S2.this, OnBoard_ActivityLevel_S3.class);
                startActivity(intent);
            }
        });


    }
}

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

public class OnBoard_ActivityLevel_S3 extends AppCompatActivity {
    private Button btnNextToUserInfo;
    private Button btnPrevoiusToGoal;

    //Radio Buttons
    private RadioGroup radiouserActivityGroup;
    private RadioButton activityLevelOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__level__s3);

        radiouserActivityGroup = (RadioGroup) findViewById(R.id.radio_actvityLevel);

        // Set up the Field Values
        btnPrevoiusToGoal = (Button) findViewById(R.id.S3PreviousButton);
        btnNextToUserInfo = (Button) findViewById(R.id.S3Nextbutton);

        //If the user gets Started in his application
        btnPrevoiusToGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                Intent intent = new Intent(OnBoard_ActivityLevel_S3.this, OnBoard_Goal_S2.class);
                startActivity(intent);
            }
        });


        //If the user gets Started in his application
        btnNextToUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Radio Button Gender
                int buttonSelected = radiouserActivityGroup.getCheckedRadioButtonId();
                activityLevelOfUser = (RadioButton) findViewById(buttonSelected);
                //Redirect
                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                //Storing the User Input
                if (getString(R.string.ActivityNotVeryActive).equals(activityLevelOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isUserNotVeryActive", true);
                } else {
                    userSharedPreferenceEditor.putBoolean("isUserNotVeryActive", false);
                }

                if (getString(R.string.ActivityLightlyActive).equals(activityLevelOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isUserLightlyActive", true);
                } else {
                    userSharedPreferenceEditor.putBoolean("isUserLightlyActive", false);
                }

                if (getString(R.string.ActivityActive).equals(activityLevelOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isUserActive", true);
                } else {
                    userSharedPreferenceEditor.putBoolean("isUserActive", false);
                }

                if (getString(R.string.ActivityVeryActive).equals(activityLevelOfUser.getText())) {
                    userSharedPreferenceEditor.putBoolean("isUserVeryActive", true);
                } else {
                    userSharedPreferenceEditor.putBoolean("isUserVeryActive", false);
                }
                userSharedPreferenceEditor.apply();

                //Redirect
                Intent intent = new Intent(OnBoard_ActivityLevel_S3.this, OnBoard_UserInfo_S4.class);
                startActivity(intent);
            }
        });


    }
}

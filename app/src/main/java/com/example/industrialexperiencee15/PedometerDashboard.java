package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PedometerDashboard extends AppCompatActivity {
    TextView totalStepsTakenByUser;
    Button BtnAddToTheWorkout;
    Button BtnBackToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_dashboard);

        totalStepsTakenByUser = (TextView) findViewById(R.id.tv_StepsCounted);
        BtnAddToTheWorkout = (Button) findViewById(R.id.btnAddtoWorkout);
        BtnBackToDashboard = (Button) findViewById(R.id.backToDashboard);

        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        Integer userSteps = userSharedPreferenceDetails.getInt("userSteps", 0);

        totalStepsTakenByUser.setText(userSteps.toString());

        BtnBackToDashboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent backToDash = new Intent(PedometerDashboard.this, Dashboard.class);
                PedometerDashboard.this.startActivity(backToDash);

            }
        });

    }
}
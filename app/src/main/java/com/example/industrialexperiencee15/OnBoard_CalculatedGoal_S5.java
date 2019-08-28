package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OnBoard_CalculatedGoal_S5 extends AppCompatActivity {
    private Button btnNextToDashboard;
    private Button btnPrevoiusUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__calculated_goal__s5);

        btnNextToDashboard = (Button) findViewById(R.id.lets_Start_now);

        btnNextToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                userSharedPreferenceEditor.putInt("calorieLimit",2500);
                userSharedPreferenceEditor.putInt("fatGoal", 100);
                userSharedPreferenceEditor.putInt("sugarGoal", 150);
                userSharedPreferenceEditor.apply();


                //Redirect
                Intent intent = new Intent(OnBoard_CalculatedGoal_S5.this, Dashboard.class);
                startActivity(intent);
            }
        });

    }

}

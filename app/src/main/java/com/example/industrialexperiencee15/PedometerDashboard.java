package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PedometerDashboard extends AppCompatActivity {
    TextView totalStepsTakenByUser;
    TextView totalCaloriesBurnedByUser;
    Button BtnAddToTheWorkout;
    Button BtnBackToDashboard;
    Double caloriesBurnedByUser;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_dashboard);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        totalStepsTakenByUser = (TextView) findViewById(R.id.tv_StepsCounted);
        totalCaloriesBurnedByUser = (TextView) findViewById(R.id.displayCaloriesBurnedvalue);
        BtnAddToTheWorkout = (Button) findViewById(R.id.btnAddtoWorkout);
        BtnBackToDashboard = (Button) findViewById(R.id.backToDashboard);

        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        Integer userSteps = userSharedPreferenceDetails.getInt("userSteps", 0);

        totalStepsTakenByUser.setText(userSteps.toString());
        
        // Calorie Calculation
        caloriesBurnedByUser=0.0;
        caloriesBurnedByUser=userSteps*0.05;

        totalCaloriesBurnedByUser.setText(caloriesBurnedByUser.toString());

        BtnAddToTheWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostWorkoutAsync postWorkoutAsync = new PostWorkoutAsync();
                postWorkoutAsync.execute(caloriesBurnedByUser);
            }
        });

        BtnBackToDashboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent backToDash = new Intent(PedometerDashboard.this, Dashboard.class);
                PedometerDashboard.this.startActivity(backToDash);

            }
        });
    }

    private class PostWorkoutAsync extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            String todayDate = currentDate.format(c.getTime());
            Workout workout = new Workout(userID, 145, todayDate,1 ,params[0].intValue());
            RestService.createWorkout(workout);
            return "Added successfully !";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(PedometerDashboard.this,response,Toast.LENGTH_LONG).show();
        }
    }
}
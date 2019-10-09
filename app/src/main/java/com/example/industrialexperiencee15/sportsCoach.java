package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class sportsCoach extends AppCompatActivity {
    private TextView mTextMessage;

    Button recommendSportsToUser;
    Button btnbackTotheExerciseHomePage;
    private EditText calorieGoalEnteredByUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_coach);
        mTextMessage = findViewById(R.id.message);

        //get the values of the fields in the UI
        calorieGoalEnteredByUser = (EditText) findViewById(R.id.weight);
        recommendSportsToUser = (Button) findViewById(R.id.recommend_Me_now);
        btnbackTotheExerciseHomePage = (Button) findViewById(R.id.btnBackToExerciseHome);

        // ------------------- Navigation Bar Code   -------------------
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navView.getMenu();
        for (int i = 0; i <= 3; i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(false);
            menuItem.setCheckable(false);
        }
        // ------------------ End of Navigation bar Code ----------------


        //If the user gets Started in his application
        recommendSportsToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightValue = calorieGoalEnteredByUser.getText().toString();

                if (!(weightValue.matches("^[0-9]{1,3}$"))) {
                    calorieGoalEnteredByUser.setError("Calorie Goal can be Numeric Characters");
                    calorieGoalEnteredByUser.requestFocus();
                } else {
                    SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                    userSharedPreferenceEditor.putInt("CalorieBurnByExerciseGaol", Integer.valueOf(calorieGoalEnteredByUser.getText().toString()));
                    userSharedPreferenceEditor.apply();
//                    sportsDiscussion = (EditText) findViewById(R.id.mostCountSportsRecommendPrompt);
//                    sportsName.setText("Hii");
                    Intent backToSportsHome = new Intent(sportsCoach.this, sportsCoach2.class);
                    sportsCoach.this.startActivity(backToSportsHome);

                }
            }
        });

        btnbackTotheExerciseHomePage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent backToSportsHome = new Intent(sportsCoach.this, personalCoachHome.class);
                sportsCoach.this.startActivity(backToSportsHome);
            }
        });

    }


    private boolean validateWeight(String weightValue, EditText weight) {
        //Ensuring the field is within the limit of the Database Field
        if (!(weightValue.matches("^[0-9]{1,3}$"))) {
            weight.setError("Calorie Goal can be Numeric Characters");
            weight.requestFocus();
            return false;
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(sportsCoach.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(sportsCoach.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(sportsCoach.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(sportsCoach.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };


}

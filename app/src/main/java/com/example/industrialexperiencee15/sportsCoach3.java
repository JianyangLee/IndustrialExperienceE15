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

public class sportsCoach3 extends AppCompatActivity {
    private TextView mTextMessage;

    Button recommendSportsToUser;
    Button btnbackTotheExerciseHomePage;
    private EditText timeForSportsGoalEnteredByUser;
    Button btntoNewSportsRecommendations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_coach3);
        mTextMessage = findViewById(R.id.message);

        //get the values of the fields in the UI
        timeForSportsGoalEnteredByUser = (EditText) findViewById(R.id.TimeGoalByUser);
        recommendSportsToUser = (Button) findViewById(R.id.recommend_me_now_new_Sportss);


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
                String weightValue = timeForSportsGoalEnteredByUser.getText().toString();

                if (!(weightValue.matches("^[0-9]{1,3}$"))) {
                    timeForSportsGoalEnteredByUser.setError("Time (Minutes) can be 1-3 Numeric Characters");
                    timeForSportsGoalEnteredByUser.requestFocus();
                } else {
                    SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                    userSharedPreferenceEditor.putFloat("CalorieBurnByExerciseTimeGoal", Integer.valueOf(timeForSportsGoalEnteredByUser.getText().toString()));
                    userSharedPreferenceEditor.apply();
//                    sportsDiscussion = (EditText) findViewById(R.id.mostCountSportsRecommendPrompt);
//                    sportsName.setText("Hii");
                    Intent backToSportsHome = new Intent(sportsCoach3.this, sportsCoach4.class);
                    sportsCoach3.this.startActivity(backToSportsHome);

                }
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
                    Intent intent_Tracker = new Intent(sportsCoach3.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(sportsCoach3.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(sportsCoach3.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(sportsCoach3.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };


}

package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class sportsCoach extends AppCompatActivity {
    private TextView mTextMessage;
    String userID;
    Button recommendSportsToUser;
    Button btnAddToTheWorkout;
    ArrayList<String> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_coach);
        mTextMessage = findViewById(R.id.message);
        exerciseList = new ArrayList<>();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recommendSportsToUser = (Button) findViewById(R.id.recommend_Me_now);
        btnAddToTheWorkout = (Button) findViewById(R.id.btnBackToExerciseHome);
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
                //Redirect
                recommendSportsForSpecificUser();
                setContentView(R.layout.activity_user_reports_home);
            }
        });

        btnAddToTheWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent backToSportsHome = new Intent(sportsCoach.this, personalCoachHome.class);
                sportsCoach.this.startActivity(backToSportsHome);
            }
        });

    }

    private void recommendSportsForSpecificUser() {
        String sportsPlayedByUserResponse = RestService.getAllWorkoutById(userID);
        if (sportsPlayedByUserResponse != null && !sportsPlayedByUserResponse.isEmpty()) {
            try {
                JSONObject jsnobject = new JSONObject(sportsPlayedByUserResponse);
                JSONArray jsonArr = jsnobject.getJSONArray("data");
                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String energy = obj.getString("ENERGY_BURNED");
                        exerciseList.add(energy);
                    } catch (Exception e) {
                        Log.e("test", "get test");
                    }
                }

            } catch (Exception e) {

            }
        } else {

        }
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

package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class sportsCoach2 extends AppCompatActivity {

    ArrayList<userWorkoutPojo> exerciseList;
    ArrayList<userWorkoutPojo> userExerciseBehaviourList;
    String userID;
    private TextView sportsName;
    private TextView sportsName2;
    private TextView sportsName1Duration;
    private TextView sportsName2Duration;
    private EditText sportsDiscussion;
    private Integer userCalrieGoalToBeBurnedForTheDay;
    ArrayList<userWorkoutPojo> exerciseListFromAsync = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_coach2);
        exerciseList = new ArrayList<>();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Get the value from Pref
        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userCalrieGoalToBeBurnedForTheDay = userSharedPreferenceDetails.getInt("CalorieBurnByExerciseGaol", 0);
        getUserActivitesAndPattern();
        writeSuggestionsToScreen();

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

    }

    private void getUserActivitesAndPattern() {
        getWorkOutFromDatabse getWorkOut = new getWorkOutFromDatabse();
        getWorkOut.execute();
        findUserpatternOfExercises();
    }

    private void writeSuggestionsToScreen() {

        sportsName = (TextView) findViewById(R.id.mostCountSportsPrompt);
        sportsName2 = (TextView) findViewById(R.id.mostCountSportsRecommendPrompt);
        sportsName1Duration = (TextView) findViewById(R.id.mostDurationOfSportsPrompt);
        sportsName2Duration = (TextView) findViewById(R.id.mostDurationOfSportsValuePrompt);
        Integer i = 0;
        for (userWorkoutPojo eachWorkout : userExerciseBehaviourList) {
            if (i == 0) {
                sportsName.setText(eachWorkout.exerciseID.toString());
                sportsName1Duration.setText(eachWorkout.duration.toString()+" Minutes");
                i++;
            }
            if (i == 1) {
                sportsName2.setText(eachWorkout.exerciseID.toString());
                sportsName2Duration.setText(eachWorkout.duration.toString()+" Minutes");
            }
        }

    }

    private class getWorkOutFromDatabse extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String sportsPlayedByUserResponse = RestService.getAllWorkoutById(userID);
            if (sportsPlayedByUserResponse != null && !sportsPlayedByUserResponse.isEmpty()) {
                try {
                    JSONObject jsnobject = new JSONObject(sportsPlayedByUserResponse);
                    JSONArray jsonArr = jsnobject.getJSONArray("data");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        try {
                            boolean isMatchFound = false;
                            JSONObject obj = jsonArr.getJSONObject(i);
                            String excerciseID = obj.getString("EXERCISEID");
                            Integer durationOfExercise = Integer.parseInt(obj.getString("DURATION"));
                            for (userWorkoutPojo eachExerciseList : exerciseList) {
                                if (excerciseID.equals(eachExerciseList.getExerciseID())) {
                                    eachExerciseList.setDuration(eachExerciseList.getDuration() + durationOfExercise);
                                    eachExerciseList.setCountOfExercise(eachExerciseList.getCountOfExercise() + 1);
                                    isMatchFound = true;
                                }
                            }
                            if (!isMatchFound) {
                                userWorkoutPojo newExercise = new userWorkoutPojo();
                                newExercise.setExerciseID(excerciseID);
                                newExercise.setDuration(durationOfExercise);
                                newExercise.setCountOfExercise(1);
                                exerciseList.add(newExercise);
                                isMatchFound = false;
                            }
                        } catch (Exception e) {
                            Log.e("test", "get test");
                        }
                    }

                } catch (Exception e) {

                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {

        }
    }

    private void findUserpatternOfExercises() {
        Integer maxCount = 0;
        Integer maxDuration = 0;
        userExerciseBehaviourList = new ArrayList<>();

        for (userWorkoutPojo eachExerciseList : exerciseList) {
            if (maxCount <= eachExerciseList.getCountOfExercise()) {
                maxCount = eachExerciseList.getCountOfExercise();
            }
            if (maxDuration <= eachExerciseList.getDuration()) {
                maxDuration = eachExerciseList.getDuration();
            }
        }

        //Getting the Behaviour of the user Exercise Pattern
        for (userWorkoutPojo eachExerciseList : exerciseList) {

            if (maxCount == eachExerciseList.getCountOfExercise()) {
                eachExerciseList.setMostFrequent(true);
                userExerciseBehaviourList.add(eachExerciseList);
            }
            if (maxDuration == eachExerciseList.getDuration() && maxCount != eachExerciseList.getCountOfExercise()) {
                eachExerciseList.setMostDuration(true);
                userExerciseBehaviourList.add(eachExerciseList);
            }

        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(sportsCoach2.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(sportsCoach2.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(sportsCoach2.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(sportsCoach2.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };

}

package com.example.industrialexperiencee15;

        import android.content.Intent;
        import android.os.AsyncTask;
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
        import java.util.Calendar;
        import java.util.Date;

public class foodCoach extends AppCompatActivity {
    private TextView mTextMessage;
    String userID;
    String timeOftheDay;
    Button recommendFoodToUser;
    Button btnAddToTheWorkout;
    ArrayList<Consumption> consumptionList;
    ArrayList<Consumption> userFoodBehaviourList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_coach);
        mTextMessage = findViewById(R.id.message);
        consumptionList = new ArrayList<>();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recommendFoodToUser = (Button) findViewById(R.id.recommend_Me_now);
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
        recommendFoodToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                getWorkOutFromDatabse getWorkOut = new getWorkOutFromDatabse();
                getWorkOut.execute();
                findUserpatternOfExercises();

            }
        });

        btnAddToTheWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent backToSportsHome = new Intent(foodCoach.this, personalCoachHome.class);
                foodCoach.this.startActivity(backToSportsHome);
            }
        });

    }

    private class getWorkOutFromDatabse extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String sportsPlayedByUserResponse = RestService.getAllConsumptionyUId(userID);
            consumptionList = new ArrayList<>();
            if (sportsPlayedByUserResponse != null && !sportsPlayedByUserResponse.isEmpty()) {
                try {
                    JSONObject jsnobject = new JSONObject(sportsPlayedByUserResponse);
                    JSONArray jsonArr = jsnobject.getJSONArray("data");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        try {
                            boolean isMatchFound = false;
                            JSONObject obj = jsonArr.getJSONObject(i);
                            String foodName = obj.getString("NAME");
                            Integer foodAmount = Integer.parseInt(obj.getString("AMOUNT"));
                            Double foodFat = obj.getDouble("FAT");
                            Double foodSugar = obj.getDouble("SUGAR");
                            Double foodCalorie = obj.getDouble("CALORIE");

                            for (Consumption eachFoodList : consumptionList) {
                                if (foodName.equalsIgnoreCase(eachFoodList.getName())) {
                                    eachFoodList.setAmount(eachFoodList.getAmount()+foodAmount);
                                    eachFoodList.setCalorie(eachFoodList.getCalorie()+foodCalorie);
                                    eachFoodList.setFat(eachFoodList.getFat()+foodFat);
                                    eachFoodList.setSugar(eachFoodList.getSugar()+foodSugar);
                                    eachFoodList.setCountOfOccurance(eachFoodList.getCountOfOccurance()+1);
                                    isMatchFound = true;
                                }
                            }
                            if (!isMatchFound) {
                                Consumption newConsumption = new Consumption();
                                newConsumption.setSugar(foodSugar);
                                newConsumption.setFat(foodFat);
                                newConsumption.setCalorie(foodCalorie);
                                newConsumption.setAmount(foodAmount);
                                newConsumption.setName(foodName);
                                newConsumption.setCountOfOccurance(1);
                                consumptionList.add(newConsumption);
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
private void getBasicsInforForFoodRecommendation(){

    Date currentTime = Calendar.getInstance().getTime();
    Integer hourOftheDay=currentTime.getHours();
    if(6<=hourOftheDay && hourOftheDay <=11){
//Brekkie
        timeOftheDay="BreakFast";
    }
    if(12<=hourOftheDay && hourOftheDay <16){
//Lunch
        timeOftheDay="Lunch";
    }
    if(16<=hourOftheDay && hourOftheDay < 19){
//Snack
        timeOftheDay="Snack";
    }
    if(19<hourOftheDay && hourOftheDay < 24){
//Dinner
        timeOftheDay="Dinner";
    }
    if(00<hourOftheDay && hourOftheDay < 6){
//Snack
        timeOftheDay="Snack";
    }
}
    private void findUserpatternOfExercises() {
        Integer maxNumberOfIntake = 0;
        Integer maxAmountOfIntake = 0;
        userFoodBehaviourList = new ArrayList<>();

        for (Consumption eachConsumptionList : consumptionList) {
            if (maxNumberOfIntake <= eachConsumptionList.getCountOfOccurance()) {
                maxNumberOfIntake = eachConsumptionList.getCountOfOccurance();
            }
            if (maxAmountOfIntake <= eachConsumptionList.getAmount()) {
                maxAmountOfIntake = eachConsumptionList.getAmount();
            }
        }

        //Getting the Behaviour of the user Exercise Pattern
        for (Consumption eachConsumptionList : consumptionList) {

            if (maxNumberOfIntake == eachConsumptionList.getCountOfOccurance()) {
                eachConsumptionList.setMostFrequent(true);
                userFoodBehaviourList.add(eachConsumptionList);
            }
            if (maxAmountOfIntake == eachConsumptionList.getAmount() && maxNumberOfIntake != eachConsumptionList.getCountOfOccurance()) {
                eachConsumptionList.setMostCaloriesConsumed(true);
                userFoodBehaviourList.add(eachConsumptionList);
            }

        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(foodCoach.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(foodCoach.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(foodCoach.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(foodCoach.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };


}

package com.example.industrialexperiencee15;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
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

        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QuerySnapshot;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

public class foodCoach extends AppCompatActivity {
    private TextView mTextMessage;
    private TextView timeofDayTextView;
    String userID;
    String timeOftheDay;
    Button recommendFoodToUser;
    String timeZoneDisplay;
    Button btnToDashboard;
    ArrayList<Consumption> consumptionList;
    ArrayList<Consumption> userFoodBehaviourList;
    private BigDecimal sugarFinal;
    private BigDecimal fatFinal;
    private Integer burnedFinal;
    private Integer ReaminingCaloriesForTheDay;
    BigDecimal calFinal;
    double calInShow;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    private TextView timeZoneOfDay;
    private TextView caloriesLeftForDay;
    private Integer RemainingCaloriesDividingFactor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RemainingCaloriesDividingFactor=1;
        ReaminingCaloriesForTheDay=0;
        userFoodBehaviourList=new ArrayList<>();
        getBasicsInforForFoodRecommendation();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_coach);

        getConsumptionFromDatabse getWorkOut = new getConsumptionFromDatabse();
        getWorkOut.execute();


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

        mTextMessage = findViewById(R.id.message);
        consumptionList = new ArrayList<>();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



        btnToDashboard = (Button) findViewById(R.id.Btn_To_Dashboard_app);

        timeofDayTextView = (TextView) findViewById(R.id.textViewforTimeOfTheDay);

        caloriesLeftForDay = (TextView) findViewById(R.id.textViewCaloriesLeftForTheDay);




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


         btnToDashboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent backToSportsHome = new Intent(foodCoach.this, Dashboard.class);
                foodCoach.this.startActivity(backToSportsHome);
            }
        });

        writeRecommendationInScreen();
    }
    private void writeRecommendationInScreen (){

        timeofDayTextView.setText(timeZoneDisplay);
        StringBuffer ListOfFoods= new StringBuffer();
        ListOfFoods.append("Please try any of the Following \n \n");
        for (Consumption foodBehavious:userFoodBehaviourList){
            ListOfFoods.append( foodBehavious.getName());
            ListOfFoods.append("\n");

        }
        caloriesLeftForDay.setText(ListOfFoods.toString());
    }

    private class getConsumptionFromDatabse extends AsyncTask<Void, Void, String> {

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
            findUserpatternOfExercises();
            writeRecommendationInScreen();
        }
    }
private void getBasicsInforForFoodRecommendation(){

    Date currentTime = Calendar.getInstance().getTime();
    Integer hourOftheDay=currentTime.getHours();
    if(6<=hourOftheDay && hourOftheDay <=11){
//Brekkie
        timeOftheDay="BreakFast";
        RemainingCaloriesDividingFactor=4;
        timeZoneDisplay="Its Brekkie Time !";

    }
    if(12<=hourOftheDay && hourOftheDay <16){
//Lunch
        timeOftheDay="Lunch";
        RemainingCaloriesDividingFactor=3;
        timeZoneDisplay="Its Lunch Time !";
    }
    if(16<=hourOftheDay && hourOftheDay < 19){
//Snack
        timeOftheDay="Snack";
        RemainingCaloriesDividingFactor=2;
        timeZoneDisplay="Its Snacks Time !";
    }
    if(19<hourOftheDay && hourOftheDay < 24){
//Dinner
        timeOftheDay="Dinner";
        RemainingCaloriesDividingFactor=1;
        timeZoneDisplay="Its Dinner Time !";
    }
    if(00<hourOftheDay && hourOftheDay < 6){
//Snack
        timeOftheDay="Snack";
        RemainingCaloriesDividingFactor=1;
        timeZoneDisplay="Its Midnight Snack Time !";
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

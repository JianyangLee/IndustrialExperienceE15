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
import java.util.Calendar;
import java.util.List;

public class InBoundCaloriesHome extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    TextView sugarCon, fatCon, calShow, remainedTextView, goalToday, takenToday, burnedToday;
    private BigDecimal sugarFinal;
    private BigDecimal fatFinal;
    private Integer burnedFinal;
    BigDecimal calFinal;
    String userID;
    double calInShow;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_bound_calories_home);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

        burnedFinal = 0;
        sugarCon = (TextView) findViewById(R.id.sugarAmountUserSettings);
        fatCon = (TextView) findViewById(R.id.fatAmountUserSettings);
        calShow = (TextView) findViewById(R.id.calShow);
        remainedTextView = (TextView) findViewById(R.id.remained);
        goalToday = (TextView) findViewById(R.id.goalToday);
        takenToday = (TextView) findViewById(R.id.takenToday);
        burnedToday = (TextView) findViewById(R.id.burnedToday);

        InBoundCaloriesHome.DBInAsyncTask dbReadin = new InBoundCaloriesHome.DBInAsyncTask();
        dbReadin.execute();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navView.getMenu();
        for(int i=0;i<=3;i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(false);
            menuItem.setCheckable(false);
        }

        final Button foodTracker = (Button) findViewById(R.id.btnTileEnterUserFood);
        final Button trackUserFood = (Button) findViewById(R.id.btnTileViewUserFood);

        foodTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newUserGoal = new Intent(InBoundCaloriesHome.this, Tracker.class);
                InBoundCaloriesHome.this.startActivity(newUserGoal);
            }
        });
        trackUserFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lineGraph = new Intent(InBoundCaloriesHome.this, TrackFood.class);
                InBoundCaloriesHome.this.startActivity(lineGraph);
            }
        });


    }



    private class DBInAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            db.collection("consumption").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                    String todayDate = currentDate.format(c.getTime());
                    double sugar = 0;
                    double fat = 0;
                    double cal = 0;

//                    SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
//                    int userCalorieLimitForTheDay =userSharedPreferenceDetails.getInt("calorieLimit",0);
                    //int userFatLimitForTheDay =userSharedPreferenceDetails.getInt("fatGoal",0);
                    // int userProteinLimitForTheDay =userSharedPreferenceDetails.getInt("proteinGoal",0);

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Consumption con = d.toObject(Consumption.class);
                            if (todayDate.equals(con.getCon_date()) && userID.equals(con.getUID())) {
                                sugar = sugar + con.getSugar();
                                fat = fat + con.getFat();
                                cal = cal + con.getCalorie();
                            }
                        }
                    }

                    BigDecimal sugarDecimal = new BigDecimal(sugar);
                    BigDecimal fatDecimal = new BigDecimal(fat);
                    BigDecimal calDecimal = new BigDecimal(cal);
                    sugarFinal = sugarDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    fatFinal = fatDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    calFinal = calDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);

                    fatCon.setText(Double.toString(fatFinal.doubleValue()) + "g");
                    sugarCon.setText(Double.toString(sugarFinal.doubleValue()) + "g");
                    InBoundCaloriesHome.ShowAsyncTask show = new InBoundCaloriesHome.ShowAsyncTask();
                    show.execute(calFinal.doubleValue());
                }
            });

            return "";
        }


        @Override
        protected void onPostExecute(String response) {


        }
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(InBoundCaloriesHome.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(InBoundCaloriesHome.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(InBoundCaloriesHome.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(InBoundCaloriesHome.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };

    private class ShowAsyncTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            String todayDate = currentDate.format(c.getTime());
            calInShow = params[0];

            String returnValue = RestService.findByUseridAndDate(userID, todayDate);
            try {
                JSONObject jsnobject = new JSONObject(returnValue);
                JSONArray jsonArr = jsnobject.getJSONArray("data");
                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        int energy = obj.getInt("ENERGY_BURNED");
                        burnedFinal = burnedFinal + energy;
                    } catch (Exception e) {
                        Log.e("test", "get test");
                    }
                }

            } catch (Exception e) {

            }


            db.collection("calculation").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Calculation calculation = d.toObject(Calculation.class);
                            if (userID.equals(calculation.getUID())) {

                                Integer calorieToShowInScreeen = (int) (calculation.getCalorieLimit() - (int) calInShow + burnedFinal);
                                Integer calLimit = (int) (calculation.getCalorieLimit());
                                Integer calTaken = (int) (calInShow);
                                if (calorieToShowInScreeen >= 0) {

                                } else {
                                    calorieToShowInScreeen = calorieToShowInScreeen * -1;
                                    remainedTextView.setText("Over");
                                    calShow.setTextColor(getResources().getColor(R.color.red));
                                    remainedTextView.setTextColor(getResources().getColor(R.color.red));
                                }
                                calShow.setText(calorieToShowInScreeen.toString());
                                goalToday.setText(calLimit.toString() + " Cal");
                                takenToday.setText(calTaken.toString() + " Cal");
                                burnedToday.setText(burnedFinal.toString() + " Cal");


                                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userLeft", Context.MODE_PRIVATE);
                                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                                userSharedPreferenceEditor.putInt("Left", (int) (calculation.getCalorieLimit() - (int) calInShow + burnedFinal));
                                userSharedPreferenceEditor.apply();
                            }
                        }
                    }
                }
            });
            return "";
        }

        @Override
        protected void onPostExecute(String response) {


        }
    }
}

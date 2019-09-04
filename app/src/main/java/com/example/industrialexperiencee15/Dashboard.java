package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class Dashboard extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    TextView  sugarCon, fatCon,calShow, remainedTextView, goalToday, takenToday;
    private BigDecimal sugarFinal;
    private BigDecimal fatFinal;
    BigDecimal calFinal;
    String userID;
    double calInShow;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();



        sugarCon = (TextView) findViewById(R.id.sugarAmount);
        fatCon = (TextView) findViewById(R.id.fatAmount);
        calShow = (TextView) findViewById(R.id.calShow);
        remainedTextView = (TextView) findViewById(R.id.remained);
        goalToday = (TextView) findViewById(R.id.goalToday);
        takenToday = (TextView) findViewById(R.id.takenToday);

        DBInAsyncTask dbReadin = new DBInAsyncTask();
        dbReadin.execute();



        final Button Insert = (Button) findViewById(R.id.btnTile1);
        final Button TrackFood = (Button) findViewById(R.id.btnTile2);
        final Button logout = (Button) findViewById(R.id.logout);
        final Button exercise = (Button) findViewById(R.id.btnTile3);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent out = new Intent(Dashboard.this, MainActivity.class);
                Dashboard.this.startActivity(out);
            }
        });


        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert = new Intent(Dashboard.this, Tracker.class);
//                String test = FirebaseAuth.getInstance().getCurrentUser().getUid();//Get user id from firebase
                Dashboard.this.startActivity(insert);
            }
        });

        TrackFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trackFood = new Intent(Dashboard.this, TrackFood.class);
                Dashboard.this.startActivity(trackFood);
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exercise = new Intent(Dashboard.this, activity_exercise.class);
                Dashboard.this.startActivity(exercise);
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
                    ShowAsyncTask show = new ShowAsyncTask();
                    show.execute(calFinal.doubleValue());
                }
            });

            return "" ;
        }


        @Override
        protected void onPostExecute(String response) {


        }
    }


    private class ShowAsyncTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            calInShow = params[0];
            db.collection("calculation").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Calculation calculation = d.toObject(Calculation.class);
                            if (userID.equals(calculation.getUID())) {

                                Integer calorieToShowInScreeen = (int)(calculation.getCalorieLimit() - (int)calInShow);//need to wait exercise data.
                                Integer calLimit = (int)(calculation.getCalorieLimit());
                                Integer calTaken = (int)(calInShow);
                                if (calorieToShowInScreeen >= 0){

                                }
                                else{
                                    calorieToShowInScreeen = calorieToShowInScreeen * -1;
                                    remainedTextView.setText("Over");
                                    calShow.setTextColor(getResources().getColor(R.color.red));
                                    remainedTextView.setTextColor(getResources().getColor(R.color.red));
                                }
                                calShow.setText(calorieToShowInScreeen.toString());
                                goalToday.setText(calLimit.toString() + " Cal");
                                takenToday.setText(calTaken.toString() + " Cal");
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




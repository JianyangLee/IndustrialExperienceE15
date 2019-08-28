package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    TextView  sugarCon, fatCon,calShow;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();

        sugarCon = (TextView) findViewById(R.id.sugarAmount);
        fatCon = (TextView) findViewById(R.id.fatAmount);
        calShow = (TextView) findViewById(R.id.calShow);

        DBInAsyncTask dbReadin = new DBInAsyncTask();
        dbReadin.execute();



        final Button Insert = (Button) findViewById(R.id.btnTile1);
        //final Button logout = (Button) findViewById(R.id.logout);


//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent out = new Intent(Dashboard.this, MainActivity.class);
//                Dashboard.this.startActivity(out);
//            }
//        });


        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert = new Intent(Dashboard.this, Tracker.class);
                String test = FirebaseAuth.getInstance().getCurrentUser().getUid();//Get user id from firebase
                Dashboard.this.startActivity(insert);
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

                    SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    int userCalorieLimitForTheDay =userSharedPreferenceDetails.getInt("calorieLimit",0);
                    //int userFatLimitForTheDay =userSharedPreferenceDetails.getInt("fatGoal",0);
                    // int userProteinLimitForTheDay =userSharedPreferenceDetails.getInt("proteinGoal",0);

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Consumption con = d.toObject(Consumption.class);
                            if (todayDate.equals(con.getCon_date())) {
                                sugar = sugar + con.getSugar();
                                fat = fat + con.getFat();
                                cal = cal + con.getCalorie();
                            }
                        }
                    }

                    BigDecimal sugarDecimal = new BigDecimal(sugar);
                    BigDecimal fatDecimal = new BigDecimal(fat);
                    BigDecimal calDecimal = new BigDecimal(cal);
                    BigDecimal sugarFinal = sugarDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal fatFinal = fatDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal calFinal = calDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);

                    Integer calorieToShowInScreeen = userCalorieLimitForTheDay - calFinal.intValue();

                    sugarCon.setText(Double.toString(sugarFinal.doubleValue()) + "g");
                    fatCon.setText(Double.toString(fatFinal.doubleValue()) + "g");
                    calShow.setText(calorieToShowInScreeen.toString());
                }
            });

            return "" ;
        }


        @Override
        protected void onPostExecute(String response) {


        }
    }
}




package com.example.industrialexperiencee15;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
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

public class userReportsHome extends AppCompatActivity {


    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    TextView sugarCon, fatCon,calShow, remainedTextView, goalToday, takenToday, burnedToday;
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
        setContentView(R.layout.activity_user_reports_home);

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

        userReportsHome.DBInAsyncTask dbReadin = new userReportsHome.DBInAsyncTask();
        dbReadin.execute();

        final Button userDailyReport = (Button) findViewById(R.id.btnTileDailyUserReport);
        final Button userprogressReport = (Button) findViewById(R.id.btnTileUserProgressReport);

        userDailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newUserGoal = new Intent(userReportsHome.this, DataVisual.class);
                userReportsHome.this.startActivity(newUserGoal);
            }
        });
        userprogressReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent lineGraph = new Intent(userReportsHome.this, Graph.class);
                userReportsHome.this.startActivity(lineGraph);
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
                    userReportsHome.ShowAsyncTask show = new userReportsHome.ShowAsyncTask();
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
            Calendar c = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            String todayDate = currentDate.format(c.getTime());
            calInShow = params[0];

            String returnValue = RestService.findByUseridAndDate(userID,todayDate );
            try {
                JSONObject jsnobject = new JSONObject(returnValue);
                JSONArray jsonArr = jsnobject.getJSONArray("data");
                for(int i = 0; i < jsonArr.length(); i++)
                {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        int energy = obj.getInt("ENERGY_BURNED");
                        burnedFinal = burnedFinal + energy;
                    }
                    catch (Exception e) {
                        Log.e("test","get test");
                    }
                }

            }
            catch(Exception e){

            }


            db.collection("calculation").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Calculation calculation = d.toObject(Calculation.class);
                            if (userID.equals(calculation.getUID())) {

                                Integer calorieToShowInScreeen = (int)(calculation.getCalorieLimit() - (int)calInShow + burnedFinal);
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
                                burnedToday.setText(burnedFinal.toString() + " Cal");


                                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userLeft", Context.MODE_PRIVATE);
                                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                                userSharedPreferenceEditor.putInt("Left",(int)(calculation.getCalorieLimit() - (int)calInShow + burnedFinal));
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

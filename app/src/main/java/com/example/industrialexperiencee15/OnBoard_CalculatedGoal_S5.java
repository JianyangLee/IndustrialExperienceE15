package com.example.industrialexperiencee15;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.math.BigDecimal;

public class OnBoard_CalculatedGoal_S5 extends AppCompatActivity {
    private Button btnNextToDashboard;
    private Button btnPrevoiusUserInfo;

    private Boolean isAimingToLoseWeight;
    private Boolean isAimingToMaintainWeight;
    private Boolean isAimingToGainWeight;
    private Boolean isUserNotVeryActive;
    private Boolean isUserLightlyActive;
    private Boolean isUserActive;
    private Boolean isUserVeryActive;
//    private String firstName;
//    private String lastName;
    private Integer height;
    private Integer weight;
    private Boolean isUserMale;
    private Boolean isUserFeMale;
    private Integer age;
    private Double computedCalorieLimit;
    private Double computedFatGoal;
    private Double coputedSugarGoal;
    private TextView displayCalorieGoal;
    private TextView displayfatGoalvalue;
    private TextView displaySugarGoalvalue;
    private String genderChar;
    private double activityLevelInteger;
    private String userGoal;
    FirebaseFirestore db;
    FirebaseAuth mFirebaseAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__calculated_goal__s5);
        createCalorieGoalForTheUser();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        btnNextToDashboard = (Button) findViewById(R.id.lets_Start_now);



        btnNextToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InsertUserinfo insertUserinfo = new InsertUserinfo();
                insertUserinfo.execute();

//                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
//                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
//                userSharedPreferenceEditor.putInt("calorieLimit", computedCalorieLimit.intValue());
//                userSharedPreferenceEditor.putInt("fatGoal", computedFatGoal.intValue());
//                userSharedPreferenceEditor.putInt("sugarGoal", coputedSugarGoal.intValue());
//                userSharedPreferenceEditor.apply();
                CollectionReference dbCalculation = db.collection("calculation");
                Calculation calculation = new Calculation(userID, (double) computedCalorieLimit.intValue(),(double)computedFatGoal.intValue(), (double)coputedSugarGoal.intValue());

                dbCalculation.add(calculation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(OnBoard_CalculatedGoal_S5.this,"Calculation is done",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OnBoard_CalculatedGoal_S5.this, Dashboard.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OnBoard_CalculatedGoal_S5.this,"Error! Try again!",Toast.LENGTH_LONG).show();
                    }
                });
//                //Redirect

            }
        });

    }


    private void createCalorieGoalForTheUser() {
        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        isAimingToLoseWeight = userSharedPreferenceDetails.getBoolean("isAimingToLoseWeight", false);
        isAimingToMaintainWeight = userSharedPreferenceDetails.getBoolean("isAimingToMaintainWeight", false);
        isAimingToGainWeight = userSharedPreferenceDetails.getBoolean("isAimingToGainWeight", false);
        isUserNotVeryActive = userSharedPreferenceDetails.getBoolean("isUserNotVeryActive", false);
        isUserLightlyActive = userSharedPreferenceDetails.getBoolean("isUserLightlyActive", false);
        isUserActive = userSharedPreferenceDetails.getBoolean("isUserActive", false);
        isUserVeryActive = userSharedPreferenceDetails.getBoolean("isUserVeryActive", false);
//        firstName = userSharedPreferenceDetails.getString("firstName", "");
//        lastName = userSharedPreferenceDetails.getString("lastName", "");
        height = userSharedPreferenceDetails.getInt("height", 0);
        weight = userSharedPreferenceDetails.getInt("weight", 0);
        isUserMale = userSharedPreferenceDetails.getBoolean("isUserMale", false);
        isUserFeMale = userSharedPreferenceDetails.getBoolean("isUserFeMale", false);
        age = userSharedPreferenceDetails.getInt("age", 0);

        int weightModule = 10 * weight;
        double heightModule = 6.25 * height;
        int ageModule = 5 * age;
        coputedSugarGoal = 77.0;

        if (isUserMale) {
            //BMR = (10 × weight in kg) + (6.25 × height in cm) − (5 × age in years) + 5
            computedCalorieLimit = weightModule + heightModule - ageModule + 5;
            genderChar="M";
        } else if (isUserFeMale) {
            // BMR = (10 × weight in kg) + (6.25 × height in cm) − (5 × age in years) − 161
            computedCalorieLimit = weightModule + heightModule - ageModule - 161;
            genderChar="F";
        }
        //Altering the Activity Level as per the Activity Level of the user
        if (isUserNotVeryActive) {
            //1.2
            computedCalorieLimit = computedCalorieLimit * 1.2;
            activityLevelInteger=1.2;
        }
        if (isUserLightlyActive) {
            //1.375
            computedCalorieLimit = computedCalorieLimit * 1.375;
            activityLevelInteger=1.375;
        }
        if (isUserActive) {
            //1.55
            computedCalorieLimit = computedCalorieLimit * 1.55;
            activityLevelInteger=1.55;
        }
        if (isUserVeryActive) {
            //1.725
            computedCalorieLimit = computedCalorieLimit * 1.725;
            activityLevelInteger=1.725;
        }
        //44 grams to 77
        //User Trying to Lose Weight
        if (isAimingToLoseWeight) {
            computedCalorieLimit = computedCalorieLimit * 0.90;
            computedFatGoal = 44.0;
            userGoal="Lose Weight";
        }
        // User Trying to Gain Weight
        if (isAimingToGainWeight) {
            computedCalorieLimit = computedCalorieLimit * 1.10;
            computedFatGoal = 77.0;
            userGoal="Gain Weight";
        }// User Trying to maintain weight
        if (isAimingToMaintainWeight) {
            computedFatGoal = 61.0;
            userGoal="Maintain Weight";
        }

        displayCalorieGoal = (TextView) findViewById(R.id.displayCalorieGoalvalue);
        displayfatGoalvalue = (TextView) findViewById(R.id.displayfatGoalvalue);
        displaySugarGoalvalue = (TextView) findViewById(R.id.displaySugarGoalvalue);
        Integer computedCalorieLimitInteger=computedCalorieLimit.intValue();
        displayCalorieGoal.setText((computedCalorieLimitInteger.toString()));
        displayfatGoalvalue.setText(computedFatGoal.toString());
        displaySugarGoalvalue.setText(coputedSugarGoal.toString());


    }

    private class InsertUserinfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Userinfo userinfo = new Userinfo(userID,height,weight,age,genderChar,activityLevelInteger,computedCalorieLimit.intValue(),userGoal);
            RestService.createUserInfo(userinfo);
            return "Create user information successfully";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(OnBoard_CalculatedGoal_S5.this,response,Toast.LENGTH_LONG).show();
        }
    }
}

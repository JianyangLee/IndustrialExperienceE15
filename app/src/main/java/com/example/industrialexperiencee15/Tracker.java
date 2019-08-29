package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tracker extends AppCompatActivity {
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] foodList;
    EditText foodName;
    EditText amount;
    TextView unit;
    ImageView image;
    TextView hello;
    Button back;;
    FirebaseFirestore db;
    private double currentSugar;
    private double currentFat;
    private double currentCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        currentSugar = 0;
        currentFat = 0;
        currentCal = 0;

        db = FirebaseFirestore.getInstance();

        listView = (ListView) findViewById(R.id.listView);

        foodName = (EditText) findViewById(R.id.foodName);
        amount = (EditText) findViewById(R.id.amount);
        Button addFood = (Button) findViewById(R.id.btnAdd);
        back = (Button) findViewById(R.id.btnBack);
        unit = (TextView) findViewById(R.id.unit);
        image = (ImageView) findViewById(R.id.fisrtView);
        hello = (TextView) findViewById(R.id.hello);

        initialList();

        foodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    initialList();
                    Toast.makeText(Tracker.this,"Search your food and add it.",Toast.LENGTH_LONG).show();
                }
                else{
                    searchItem(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String value = item.toString();
                foodName.setText(value);


                JSONArray jsonArr = loadJSONFromAsset("food.json");

                for(int i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String name = obj.getString("Food");
                        if (name.equals(value)) {
                            unit.setText(obj.getString("unit"));
                        } else {

                        }
                    } catch (Exception e) {
                        Log.e("test", "get test");
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(Tracker.this, Dashboard.class);
                Tracker.this.startActivity(dashboard);
            }
        });


        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amount.getText().toString().equals("") && foodName.getText().toString().equals("") ) {
                    Animation shake = AnimationUtils.loadAnimation(Tracker.this, R.anim.shake);
                    foodName.startAnimation(shake);
                    foodName.setError("Please enter numeric value and it cannot be empty");
                    amount.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }
                if (amount.getText().toString().equals("")) {

                    Animation shake = AnimationUtils.loadAnimation(Tracker.this, R.anim.shake);
                    amount.startAnimation(shake);
                    amount.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }
                if (foodName.getText().toString().equals("")) {

                    Animation shake = AnimationUtils.loadAnimation(Tracker.this, R.anim.shake);
                    foodName.startAnimation(shake);
                    foodName.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }


                double foodSugar = 0;
                double foodFat = 0;
                double foodCal = 0;
                JSONArray jsonArr = loadJSONFromAsset("food.json");
                for(int i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String name = obj.getString("Food");
                        if (name.equals(foodName.getText().toString())) {
                            foodSugar = obj.getDouble("sugar");
                            foodFat = obj.getDouble("Fat");
                            foodCal = obj.getDouble("Energy(KJ)") * 0.239006;
                        } else {

                        }
                    } catch (Exception e) {
                        Log.e("test", "get test");
                    }
                }
                DBInAsyncTask databaseAsync = new DBInAsyncTask();
                databaseAsync.execute(foodSugar,foodFat, foodCal );

            }
        });
    }

//                DBInAsyncTask database = new DBInAsyncTask();
//                database.execute();


//                db.collection("consumption").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Calendar c = Calendar.getInstance();
//                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//                        String todayDate = currentDate.format(c.getTime());
//                        double sugar = 0;
//                        double fat = 0;
//                        double cal = 0;
//                        if (! queryDocumentSnapshots.isEmpty()) {
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//
//                            for (DocumentSnapshot d : list) {
//                                Consumption con = d.toObject(Consumption.class);
//                                if (todayDate.equals(con.getCon_date())) {
//                                    sugar = sugar + (con.getSugar() * con.getAmount());
//                                    fat = fat + (con.getFat() * con.getAmount());
//                                    cal = cal + (con.getCalorie() * con.getAmount());
//                                }
//                            }
//                            //Get sharedPreference
//                            if (sugar > 100){
//                                if (fat > 100){
//                                    if (cal > 100){
//                                        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                        anim.setInterpolator(new LinearInterpolator());
//                                        anim.setRepeatCount(Animation.INFINITE);
//                                        anim.setDuration(700);
//                                        image.setImageResource(R.drawable.sad);
//                                        image.startAnimation(anim);
//                                        anim.setRepeatCount(1);
//                                        vibrate();
//                                        hello.setText("All surpass health level!");
//                                        hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                                    }
//                                    else {
//                                        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                        anim.setInterpolator(new LinearInterpolator());
//                                        anim.setRepeatCount(Animation.INFINITE);
//                                        anim.setDuration(700);
//                                        image.setImageResource(R.drawable.sad);
//                                        image.startAnimation(anim);
//                                        anim.setRepeatCount(1);
//                                        vibrate();
//                                        hello.setText("Sugar and Fat surpass health level!");
//                                        hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                                    }
//                                }else{
//                                    if (cal > 100){
//                                        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                        anim.setInterpolator(new LinearInterpolator());
//                                        anim.setRepeatCount(Animation.INFINITE);
//                                        anim.setDuration(700);
//                                        image.setImageResource(R.drawable.sad);
//                                        image.startAnimation(anim);
//                                        anim.setRepeatCount(1);
//                                        vibrate();
//                                        hello.setText("Sugar and Cal surpass health level!");
//                                        hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                                    }
//                                    else {
//                                        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                        anim.setInterpolator(new LinearInterpolator());
//                                        anim.setRepeatCount(Animation.INFINITE);
//                                        anim.setDuration(700);
//                                        image.setImageResource(R.drawable.sad);
//                                        image.startAnimation(anim);
//                                        anim.setRepeatCount(1);
//                                        vibrate();
//                                        hello.setText("Sugar surpasses health level!");
//                                        hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                                    }
//                                }
//                            }
//                            else if (fat > 100){
//                                if (cal > 100){
//                                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                    anim.setInterpolator(new LinearInterpolator());
//                                    anim.setRepeatCount(Animation.INFINITE);
//                                    anim.setDuration(700);
//                                    image.setImageResource(R.drawable.sad);
//                                    image.startAnimation(anim);
//                                    anim.setRepeatCount(1);
//                                    vibrate();
//                                    hello.setText("Cal and Fat surpass health level!");
//                                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                                }
//                                else{
//                                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                    anim.setInterpolator(new LinearInterpolator());
//                                    anim.setRepeatCount(Animation.INFINITE);
//                                    anim.setDuration(700);
//                                    image.setImageResource(R.drawable.sad);
//                                    image.startAnimation(anim);
//                                    anim.setRepeatCount(1);
//                                    vibrate();
//                                    hello.setText("Fat surpasses health level!");
//                                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                                }
//                            }
//                            else if (cal > 100){
//                                RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//                                anim.setInterpolator(new LinearInterpolator());
//                                anim.setRepeatCount(Animation.INFINITE);
//                                anim.setDuration(700);
//                                image.setImageResource(R.drawable.sad);
//                                image.startAnimation(anim);
//                                anim.setRepeatCount(1);
//                                vibrate();
//                                hello.setText("Calorie surpasses health level!");
//                                hello.setTextColor(getResources().getColor(R.color.colorAccent));
//                            }
//                        }
//                    }
//                });

//                if (!foodName.getText().toString().equals("") && !amount.getText().toString().equals("") && isInteger(amount.getText().toString())){
//                    CollectionReference dbConsumption = db.collection("consumption");
//                    Calendar c = Calendar.getInstance();
//                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//                    String today = currentDate.format(c.getTime());
//                    String name = foodName.getText().toString();
//                    int foodAmount = Integer.parseInt(amount.getText().toString());
//
//                    Consumption consumption = new Consumption(name, today, foodSugar,foodFat, foodCal, foodAmount);
//                    dbConsumption.add(consumption).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Toast.makeText(Tracker.this,"Add food successfully!",Toast.LENGTH_LONG).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Tracker.this,"Error! Try again!",Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }


    private class DBInAsyncTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
               db.collection("consumption").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                        String todayDate = currentDate.format(c.getTime());
                        double sugar = 0;
                        double fat = 0;
                        double cal = 0;
                        if (! queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                Consumption con = d.toObject(Consumption.class);
                                if (todayDate.equals(con.getCon_date())) {
                                    sugar = sugar + (con.getSugar());
                                    fat = fat + (con.getFat());
                                    cal = cal + (con.getCalorie());
                                }
                            }
                        }
                        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                        int userCalorieLimitForTheDay =userSharedPreferenceDetails.getInt("calorieLimit",0);
                        int userFatLimitForTheDay =userSharedPreferenceDetails.getInt("fatGoal",0);
                        int userSugarLimitForTheDay =userSharedPreferenceDetails.getInt("sugarGoal",0);
                            //Get sharedPreference
                        if (sugar > userSugarLimitForTheDay){
                            if (fat > userFatLimitForTheDay){
                                if (cal > userCalorieLimitForTheDay){
                                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                                    anim.setInterpolator(new LinearInterpolator());
                                    anim.setRepeatCount(Animation.INFINITE);
                                    anim.setDuration(700);
                                    image.setImageResource(R.drawable.sad);
                                    image.startAnimation(anim);
                                    anim.setRepeatCount(1);
                                    vibrate();
                                    hello.setText("All surpass health level!");
                                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                                else {
                                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                                    anim.setInterpolator(new LinearInterpolator());
                                    anim.setRepeatCount(Animation.INFINITE);
                                    anim.setDuration(700);
                                    image.setImageResource(R.drawable.sad);
                                    image.startAnimation(anim);
                                    anim.setRepeatCount(1);
                                    vibrate();
                                    hello.setText("Sugar and Fat surpass health level!");
                                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                            }else{
                                if (cal > userCalorieLimitForTheDay){
                                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                                    anim.setInterpolator(new LinearInterpolator());
                                    anim.setRepeatCount(Animation.INFINITE);
                                    anim.setDuration(700);
                                    image.setImageResource(R.drawable.sad);
                                    image.startAnimation(anim);
                                    anim.setRepeatCount(1);
                                    vibrate();
                                    hello.setText("Sugar and Cal surpass health level!");
                                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                                else {
                                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                                    anim.setInterpolator(new LinearInterpolator());
                                    anim.setRepeatCount(Animation.INFINITE);
                                    anim.setDuration(700);
                                    image.setImageResource(R.drawable.sad);
                                    image.startAnimation(anim);
                                    anim.setRepeatCount(1);
                                    vibrate();
                                    hello.setText("Sugar surpasses health level!");
                                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                            }
                        }
                        if (fat > userFatLimitForTheDay){
                            if (cal > userCalorieLimitForTheDay){
                                RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                                anim.setInterpolator(new LinearInterpolator());
                                anim.setRepeatCount(Animation.INFINITE);
                                anim.setDuration(700);
                                image.setImageResource(R.drawable.sad);
                                image.startAnimation(anim);
                                anim.setRepeatCount(1);
                                vibrate();
                                hello.setText("Cal and Fat surpass health level!");
                                hello.setTextColor(getResources().getColor(R.color.colorAccent));
                            }
                            else{
                                RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                                anim.setInterpolator(new LinearInterpolator());
                                anim.setRepeatCount(Animation.INFINITE);
                                anim.setDuration(700);
                                image.setImageResource(R.drawable.sad);
                                image.startAnimation(anim);
                                anim.setRepeatCount(1);
                                vibrate();
                                hello.setText("Fat surpasses health level!");
                                hello.setTextColor(getResources().getColor(R.color.colorAccent));
                            }
                        }
                        if (cal > userCalorieLimitForTheDay){
                            RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                            anim.setInterpolator(new LinearInterpolator());
                            anim.setRepeatCount(Animation.INFINITE);
                            anim.setDuration(700);
                            image.setImageResource(R.drawable.sad);
                            image.startAnimation(anim);
                            anim.setRepeatCount(1);
                            vibrate();
                            hello.setText("Calorie surpasses health level!");
                            hello.setTextColor(getResources().getColor(R.color.colorAccent));
                            }
                        }
                    });

            if (!foodName.getText().toString().equals("") && !amount.getText().toString().equals("") && isInteger(amount.getText().toString())){
                CollectionReference dbConsumption = db.collection("consumption");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                String today = currentDate.format(c.getTime());
                String name = foodName.getText().toString();
                int foodAmount = Integer.parseInt(amount.getText().toString());

                double foodS = (foodAmount/100.0) * params[0];
                double foodF = (foodAmount/100.0) * params[1];
                double foodC = (foodAmount/100.0) * params[2];

                Consumption consumption = new Consumption(name, today, foodF,foodS, foodC, foodAmount);
                dbConsumption.add(consumption).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Tracker.this,"Add food successfully!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Tracker.this,"Error! Try again!",Toast.LENGTH_LONG).show();
                    }
                });
            }
               return "";
        }

        @Override
        protected void onPostExecute(String response) {

        }
    }


    private void searchItem(String textSearch) {
        for (String food:foodList){
            if(!food.contains(textSearch)){
                listItems.remove(food);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void initialList(){

        JSONArray jsonArr = loadJSONFromAsset("food.json");
        foodList = new String[jsonArr.length()];

        for(int i = 0; i < jsonArr.length(); i++)
        {
            try {
                JSONObject obj = jsonArr.getJSONObject(i);
                String name = obj.getString("Food");
                foodList[i] = name;
            }
            catch (Exception e) {
                Log.e("test","get test");
            }
        }


        listItems = new ArrayList<>(Arrays.asList(foodList));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView,listItems);
        listView.setAdapter(adapter);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    private JSONArray loadJSONFromAsset(String source){
        String json = null;
        JSONArray jarray = new JSONArray();
        try {
            InputStream is = getAssets().open(source);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            jarray = new JSONArray(json);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return jarray;

    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void vibrateField(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}


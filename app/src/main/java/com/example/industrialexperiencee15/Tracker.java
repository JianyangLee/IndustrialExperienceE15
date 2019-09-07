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
import android.view.inputmethod.InputMethodManager;
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
    Button addFood;
    private double currentSugar;
    private double currentFat;
    private double currentCal;
    FirebaseAuth mFirebaseAuth;
    String userID;
    Integer burnedFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        currentSugar = 0;
        currentFat = 0;
        currentCal = 0;
        burnedFinal = 0;

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        listView = (ListView) findViewById(R.id.listView);

        foodName = (EditText) findViewById(R.id.foodName);
        amount = (EditText) findViewById(R.id.amount);
        addFood = (Button) findViewById(R.id.btnAdd);
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
                hideKeyboard();
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
                    foodName.setError("Please choose an item");
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
                double foodType = 0;
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
                if (foodCal > 500){
                    foodType = 1; //not recommended.
                }
                else{
                    foodType = 0;//recommended.
                }

                DBInAsyncTask databaseAsync = new DBInAsyncTask();
                databaseAsync.execute(foodSugar,foodFat,foodCal,foodType);

            }
        });
    }

    private class DBInAsyncTask extends AsyncTask<Double, Void, String> {
        double sugar = 0;
        double fat = 0;
        double cal = 0;
        double addedSugar = 0;
        double addedFat = 0;
        double addedCal = 0;
        @Override
        protected String doInBackground(Double... params) {
            addedSugar = params[0];
            addedFat = params[1];
            addedCal = params[2];

            Calendar cd = Calendar.getInstance();
            SimpleDateFormat cdString = new SimpleDateFormat("dd-MM-yyyy");
            String todayDate = cdString.format(cd.getTime());

            String returnValue = RestService.findByUsernameAndPassword(userID,todayDate );
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

            db.collection("consumption").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                        String todayDate = currentDate.format(c.getTime());


                        if (! queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                Consumption con = d.toObject(Consumption.class);
                                if (todayDate.equals(con.getCon_date()) && userID.equals(con.getUID())) {
                                    sugar = sugar + (con.getSugar());
                                    fat = fat + (con.getFat());
                                    cal = cal + (con.getCalorie());
                                }
                            }
                        }

                        db.collection("calculation").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                    for (DocumentSnapshot d : list) {
                                        Calculation calculation = d.toObject(Calculation.class);
                                        if (userID.equals(calculation.getUID())) {

                                           int userCalorieLimitForTheDay = (int)calculation.getCalorieLimit();
                                           int userFatLimitForTheDay = (int)calculation.getFatGoal();
                                           int userSugarLimitForTheDay = (int)calculation.getSugarGoal();


                                            if (sugar + addedSugar> userSugarLimitForTheDay){
                                                if (fat  + addedFat> userFatLimitForTheDay){
                                                    if (cal + addedCal - burnedFinal> userCalorieLimitForTheDay){
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
                                                        addFood.setVisibility(View.GONE);
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
                                                        addFood.setVisibility(View.GONE);
                                                    }
                                                }else{
                                                    if (cal + addedCal - burnedFinal> userCalorieLimitForTheDay){
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
                                                        addFood.setVisibility(View.GONE);
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
                                                        addFood.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                            if (fat + addedFat > userFatLimitForTheDay){
                                                if (cal + addedCal - burnedFinal > userCalorieLimitForTheDay){
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
                                                    addFood.setVisibility(View.GONE);
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
                                                    addFood.setVisibility(View.GONE);
                                                }
                                            }
                                            if (cal + addedCal - burnedFinal> userCalorieLimitForTheDay){
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
                                                addFood.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            }
                        });
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
                String type;
                if (params[3] == 0) {
                   type  = "Recommended";
                }else{
                    type = "Not Recommended";
                }
                if (foodAmount > 500){
                    type = "too much amount, please decrease";
                }
                Consumption consumption = new Consumption(userID, name, today, foodF,foodS, foodC, foodAmount,type);
                dbConsumption.add(consumption).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Tracker.this,"Added food successfully!",Toast.LENGTH_LONG).show();
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

            foodName.setText("");
            amount.setText("");

        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void searchItem(String textSearch) {
        initialList();
        for (String food:foodList){
            if(!food.toLowerCase().contains(textSearch.toLowerCase())){
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


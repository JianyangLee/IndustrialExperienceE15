package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

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
                        String name = obj.getString("name");
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
                Tracker.super.onBackPressed();
            }
        });


        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sugar = 10;//test value
                if (sugar > 5){
                    //Animation for sugar intake. we also need the similar thing is calorie and fat.
                    //If user consumed value >= the standared value, run this code.
                    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.setDuration(700);
                    image.setImageResource(R.drawable.sad);
                    image.startAnimation(anim);
                    anim.setRepeatCount(1);
                    vibrate();
                    hello.setText("Sugar intake surpasses health level!");
                    hello.setTextColor(getResources().getColor(R.color.colorAccent));
                    v.setVisibility(View.GONE);
                    return;
                }//test code
                //On click listener.



                if (!foodName.getText().toString().equals("") && !amount.getText().toString().equals("") && isInteger(amount.getText().toString())){
                    Intent goToDashboard = new Intent(Tracker.this, Dashboard.class);
                    Tracker.this.startActivity(goToDashboard);
                }else if (amount.getText().toString().equals("")) {

                    Animation shake = AnimationUtils.loadAnimation(Tracker.this, R.anim.shake);
                    amount.startAnimation(shake);
                    amount.setError("Please enter numeric value and it cannot be empty");
                    vibrate();
                }
                else {
                    Animation shake = AnimationUtils.loadAnimation(Tracker.this, R.anim.shake);
                    foodName.startAnimation(shake);
                    foodName.setError("Please enter numeric value and it cannot be empty");
                    amount.setError("Please enter numeric value and it cannot be empty");
                    vibrate();
                }
            }
        });

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
                String name = obj.getString("name");
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
}


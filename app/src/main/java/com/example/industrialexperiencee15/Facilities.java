package com.example.industrialexperiencee15;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Facilities extends AppCompatActivity {
    EditText facilitiesName;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] facilitiesList;
    String userID;
    JSONArray jsonArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navView.getMenu();
        for(int i=0;i<=3;i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(false);
            menuItem.setCheckable(false);
        }


        listView = (ListView) findViewById(R.id.facilitiesListView);

        // userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Facilities.GetAllFacilitiesAsync AllFacilitiesAsync = new Facilities.GetAllFacilitiesAsync();
        AllFacilitiesAsync.execute();

        facilitiesName = (EditText) findViewById(R.id.facilityName);
        final Button backToDash = (Button) findViewById(R.id.btnBacktoDashboard);
        final Button btnSearch = (Button) findViewById(R.id.startNavigation);

        facilitiesName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    initialList();
                } else {
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
                facilitiesName.setText(value);
            }
        });


        backToDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToDash = new Intent(Facilities.this, Dashboard.class);
                Facilities.this.startActivity(backToDash);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facilitiesName.getText().toString().equals("")) {
                    Animation shake = AnimationUtils.loadAnimation(Facilities.this, R.anim.shake);
                    facilitiesName.startAnimation(shake);
                    facilitiesName.setError("Please choose an item");
                    vibrateField();
                    return;
                } else {
                    //Place the information in the shared Preferences
                    SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                    userSharedPreferenceEditor.putString("sportsPlayedByUser", facilitiesName.getText().toString());
                    userSharedPreferenceEditor.apply();

                    // Moving to the Next Intent
                    Intent buttonToMaps = new Intent(Facilities.this, Maps_Facilities.class);
                    Facilities.this.startActivity(buttonToMaps);
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(Facilities.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(Facilities.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(Facilities.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(Facilities.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };

    private void initialList() {
        try {
            listItems = new ArrayList<>(Arrays.asList(facilitiesList));
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, listItems);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(Facilities.this, "Please check your network connection.", Toast.LENGTH_LONG).show();
        }
    }

    private void searchItem(String textSearch) {
        initialList();
        try {
            for (String exercise : facilitiesList) {
                if (!exercise.toLowerCase().contains(textSearch.toLowerCase())) {
                    listItems.remove(exercise);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(Facilities.this, "Please check your network connection.", Toast.LENGTH_LONG).show();
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

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    private void vibrateField() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }

    private class GetAllFacilitiesAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String returnValue = RestService.getAllFaciliites();
                JSONObject jsnobject = new JSONObject(returnValue);
                jsonArr = jsnobject.getJSONArray("data");
                facilitiesList = new String[jsonArr.length()];

                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String name = obj.getString("SPORTS_PLAYED");
                        facilitiesList[i] = name;
                    } catch (Exception e) {
                        Log.e("test", "get test");
                    }
                }
            } catch (Exception e) {

            }

            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            initialList();
        }
    }

}
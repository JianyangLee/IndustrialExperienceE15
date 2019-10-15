package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class activity_exercise extends AppCompatActivity {
    EditText exerciseName;
    EditText time;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] exerciseList;
    String userID;
    JSONArray jsonArr;
    ProgressBar pgbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        listView = (ListView) findViewById(R.id.exerciseList);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        exerciseName = (EditText) findViewById(R.id.exerciseName);
        time = (EditText) findViewById(R.id.time);
        pgbar = (ProgressBar) findViewById(R.id.pginexercise);

        final Button backToDash = (Button) findViewById(R.id.btnBackExercise);
        final Button addExercise = (Button) findViewById(R.id.btnAddExercise);



        GetAllExerciseAsync getAllExerciseAsync = new GetAllExerciseAsync();
        getAllExerciseAsync.execute();


        exerciseName.addTextChangedListener(new TextWatcher() {
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
                exerciseName.setText(value);
            }
        });

        backToDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToDash = new Intent(activity_exercise.this, Dashboard.class);
                activity_exercise.this.startActivity(backToDash);
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgbar.setVisibility(View.VISIBLE);
                if(time.getText().toString().equals("") && exerciseName.getText().toString().equals("") ) {
                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    exerciseName.startAnimation(shake);
                    pgbar.setVisibility(View.GONE);
                    exerciseName.setError("Please choose an item");
                    time.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }
                if (time.getText().toString().equals("")) {
                    pgbar.setVisibility(View.GONE);
                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    time.startAnimation(shake);
                    time.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }
                if (exerciseName.getText().toString().equals("")) {
                    pgbar.setVisibility(View.GONE);
                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    exerciseName.startAnimation(shake);
                    time.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }

                if (Integer.parseInt(time.getText().toString()) < 0){
                    pgbar.setVisibility(View.GONE);
                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    time.startAnimation(shake);
                    time.setError("Please enter the absolute value");
                    vibrateField();
                    return;
                }




                Calendar c = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                String todayDate = currentDate.format(c.getTime());
                String userid = userID;
                int exerciseid = 0;
                String date = todayDate;
                int CAL_BURNED_PER_HOUR = 0;
                for(int i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String name = obj.getString("EXERCISE_NAME");
                        if (name.equals(exerciseName.getText().toString())) {
                            exerciseid = obj.getInt("EXERCISEID");
                            CAL_BURNED_PER_HOUR = obj.getInt("CALORIES_PER_HOUR");
                        } else {

                        }
                    } catch (Exception e) {
                        Log.e("test", "get test");
                    }
                }
                int burned = 0;
                burned = (int)((CAL_BURNED_PER_HOUR / 60.0) * Integer.parseInt(time.getText().toString()));
                Workout workout = new Workout(userid, exerciseid, date, Integer.parseInt(time.getText().toString()),burned);
                if (isInteger(time.getText().toString())) {
                    InsertWorkout insertWorkout = new InsertWorkout();
                    insertWorkout.execute(workout);
                }
                else{
                    time.setError("Time should be a numeric value.");
                }

            }
        });
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

    private class GetAllExerciseAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String returnValue = RestService.getAllExercise();
                JSONObject jsnobject = new JSONObject(returnValue);
                jsonArr = jsnobject.getJSONArray("data");
                exerciseList = new String[jsonArr.length()];

                for(int i = 0; i < jsonArr.length(); i++)
                {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String name = obj.getString("EXERCISE_NAME");
                        exerciseList[i] = name;
                    }
                    catch (Exception e) {
                        Log.e("test","get test");
                    }
                }
            }
            catch(Exception e){

            }

            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            initialList();
        }
    }

    private class InsertWorkout extends AsyncTask<Workout, Void, String> {
        @Override
        protected String doInBackground(Workout... params) {
            RestService.createWorkout(params[0]);
            return "Added Successfully!";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(activity_exercise.this,response,Toast.LENGTH_LONG).show();
            initialList();
            exerciseName.setText("");
            time.setText("");
            pgbar.setVisibility(View.GONE);
        }
    }

    private void initialList(){
        try {
            listItems = new ArrayList<>(Arrays.asList(exerciseList));
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, listItems);
            listView.setAdapter(adapter);
        }
        catch(Exception e){
            Toast.makeText(activity_exercise.this,"Please check your network connection.",Toast.LENGTH_LONG).show();
        }
    }

    private void searchItem(String textSearch) {
        initialList();
        try{
        for (String exercise : exerciseList) {
            if (!exercise.toLowerCase().contains(textSearch.toLowerCase())) {
                listItems.remove(exercise);
            }
        }

        adapter.notifyDataSetChanged();
        }
        catch(Exception e){
            Toast.makeText(activity_exercise.this,"Please check your network connection.",Toast.LENGTH_LONG).show();
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
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}

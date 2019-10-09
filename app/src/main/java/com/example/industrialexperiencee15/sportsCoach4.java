package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class sportsCoach4 extends AppCompatActivity {

    ArrayList<userWorkoutPojo> databaseListOfExercises;
    Float timeAvailableForUser;
    ArrayList<userWorkoutPojo> recommenderExerciseList;
    Integer CaloriesToBeBurned;
    Integer userCalorieGoalPerHour;
    Button btnTakeMeThere;
    private String url;
    private Bitmap bitmapForFoodSearch;
    private String keyword;
    private ImageView image;

    private TextView sportsName;

    private TextView sportsName1Duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Page Load
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_coach4);
        recommenderExerciseList = new ArrayList<>();
        databaseListOfExercises = new ArrayList<>();

        //Get the value from Pref
        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        timeAvailableForUser = userSharedPreferenceDetails.getFloat("CalorieBurnByExerciseTimeGoal", 0);
        CaloriesToBeBurned = userSharedPreferenceDetails.getInt("CalorieBurnByExerciseGaol", 0);
        userCalorieGoalPerHour = (Integer) ((CaloriesToBeBurned / timeAvailableForUser.intValue()) * 60);

        btnTakeMeThere = (Button) findViewById(R.id.btn_Take_Me_There);

        // ------------------- Navigation Bar Code   -------------------
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navView.getMenu();
        for (int i = 0; i <= 3; i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(false);
            menuItem.setCheckable(false);
        }
        // ------------------ End of Navigation bar Code ----------------

        GetAllExerciseAsync getall = new GetAllExerciseAsync();
        getall.execute();



        //image Fetching from  the google search results
        image = (ImageView) findViewById(R.id.imageView_GoogleSearch);

        btnTakeMeThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent backToSportsHome = new Intent(sportsCoach4.this, Dashboard.class);
                sportsCoach4.this.startActivity(backToSportsHome);
            }
        });
    }


    private class GetAllExerciseAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String returnValue = RestService.getAllExercise();
                JSONObject jsnobject = new JSONObject(returnValue);
                JSONArray jsonArr = jsnobject.getJSONArray("data");


                for (Integer i = 0; i < jsonArr.length(); i++) {
                    try {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        String name = obj.getString("EXERCISE_NAME");
                        userWorkoutPojo newExercise = new userWorkoutPojo();
                        newExercise.setExerciseName(name);
                        Double caloriesBurnedPerHour = obj.getDouble("CALORIES_PER_HOUR");
                        newExercise.setCaloriesBurnedPerHour(caloriesBurnedPerHour);
                        newExercise.setExerciseID(obj.getString("EXERCISEID"));
                        databaseListOfExercises.add(newExercise);
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
            writeSuggestionsToScreen();


        }
    }

    private void initialList() {
        Integer numberOfMatches = 0;
        Integer varianceFactor = 20;
        for (userWorkoutPojo eachExercise : databaseListOfExercises) {
            while (numberOfMatches <= 1){
                    if (((userCalorieGoalPerHour - varianceFactor) <= eachExercise.getCaloriesBurnedPerHour()) && ((userCalorieGoalPerHour + varianceFactor) >= eachExercise.getCaloriesBurnedPerHour())) {
                        recommenderExerciseList.add(eachExercise);
                        keyword = eachExercise.getExerciseName();
                        numberOfMatches++;
                    }
                }
            varianceFactor++;
            }

        SearchInGoogleAsyncTask searchInGoogleAsyncTask = new SearchInGoogleAsyncTask();
        keyword=recommenderExerciseList.get(0).getExerciseName();
        searchInGoogleAsyncTask.execute(keyword);
        }

    private void writeSuggestionsToScreen() {

        sportsName = (TextView) findViewById(R.id.mostCountSportsPrompt);
        //sportsName1Duration = (TextView) findViewById(R.id.mostCountSportsRecommendPrompt);

        for (userWorkoutPojo eachWorkout : recommenderExerciseList) {
                String[] NameArray = eachWorkout.getExerciseName().split(",");
                sportsName.setText(NameArray[0]);
                //sportsName1Duration.setText(eachWorkout.getCaloriesBurnedPerHour() + " Cal/Hr");
        }

    }

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_food:
                        Intent intent_Tracker = new Intent(sportsCoach4.this, Tracker.class);
                        startActivity(intent_Tracker);
                        return true;
                    case R.id.navigation_dashboard:
                        Intent intent_activity_exercise = new Intent(sportsCoach4.this, Dashboard.class);
                        startActivity(intent_activity_exercise);
                        ;
                        return true;
                    case R.id.navigation_exercise:
                        Intent intent_Dashboard = new Intent(sportsCoach4.this, activity_exercise.class);
                        startActivity(intent_Dashboard);
                        return true;
                    case R.id.navigation_settings:
                        Intent intent_Settings = new Intent(sportsCoach4.this, UserSettingsHome.class);
                        startActivity(intent_Settings);
                        return true;
                }
                return false;
            }
        };

    //Search the keyword using the Google api
    private class SearchInGoogleAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return SearchTheGoogleAPI.search(params[0], new String[]{"num"}, new String[]{"1"});
        }

        @Override
        protected void onPostExecute(String result) {

            TextView textview =(TextView) findViewById(R.id.textView_GoogleSearchResults);
            textview.setText(SearchTheGoogleAPI.getSnippetFromAPIResponse(result));

            SearchImageInGoogleAsyncTask searchImageInGoogleAsyncTask = new SearchImageInGoogleAsyncTask();
            searchImageInGoogleAsyncTask.execute(keyword);
        }
    }

    //search the image for the same keyword
    private class SearchImageInGoogleAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return SearchTheGoogleAPI.searchImage(params[0], new String[]{"num", "fileType", "searchType"}, new String[]{"1", "jpg", "image"});
        }

        @Override
        protected void onPostExecute(String result) {

            url = SearchTheGoogleAPI.getUrl(result);
            GetImageFromUrl getImageFromUrl = new GetImageFromUrl(image);
            getImageFromUrl.execute(url);

            //        SearchInNDBAsyncTask searchNDBAsyncTask = new SearchInNDBAsyncTask();
            //        searchNDBAsyncTask.execute(keyword);
        }
    }

    //Display the url image to bitmapForFoodSearch image
    //ref: https://www.youtube.com/watch?v=Il3uB5u2pSA
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetImageFromUrl(ImageView image) {
            this.imageView = image;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urlDisplay = url[0];
            bitmapForFoodSearch = null;

            try {
                InputStream is = new URL(urlDisplay).openStream();
                bitmapForFoodSearch = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmapForFoodSearch;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image.setImageDrawable(null);
            image.setImageBitmap(bitmap);


        }
    }
    }

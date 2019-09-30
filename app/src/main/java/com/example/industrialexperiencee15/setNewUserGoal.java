package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class setNewUserGoal extends AppCompatActivity {

    private Button btnLetsGetStarted;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_user_goal);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // ------------------- Navigation Bar Code   -------------------
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navView.getMenu();
        for(int i=0;i<=3;i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(false);
            menuItem.setCheckable(false);
        }
        // ------------------ End of Navigation bar Code ----------------

        // Set up the Field Values
        btnLetsGetStarted =(Button)findViewById(R.id.Lets_get_Started_for_New_Goal);


        //If the user gets Started in his application
        btnLetsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                InsertUser insertUser = new InsertUser();
                insertUser.execute(userID);

                Intent intent = new Intent(setNewUserGoal.this, OnBoard_Goal_S2.class);
                startActivity(intent);
            }
        });
    }

    private class InsertUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            User user = new User(params[0]);
            RestService.createUser(user);
            return "Create user successfully";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(setNewUserGoal.this,response,Toast.LENGTH_LONG).show();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(setNewUserGoal.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(setNewUserGoal.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(setNewUserGoal.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(setNewUserGoal.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };
}

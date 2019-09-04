package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class activity_exercise extends AppCompatActivity {
    EditText exerciseName;
    EditText time;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] exerciseList;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        listView = (ListView) findViewById(R.id.exerciseList);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        exerciseName = (EditText) findViewById(R.id.exerciseName);
        time = (EditText) findViewById(R.id.time);

        final Button backToDash = (Button) findViewById(R.id.btnBackExercise);
        final Button addExercise = (Button) findViewById(R.id.btnAddExercise);



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
                if(time.getText().toString().equals("") && exerciseName.getText().toString().equals("") ) {
                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    exerciseName.startAnimation(shake);
                    exerciseName.setError("Please choose an item");
                    time.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }
                if (time.getText().toString().equals("")) {

                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    time.startAnimation(shake);
                    time.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
                }
                if (exerciseName.getText().toString().equals("")) {

                    Animation shake = AnimationUtils.loadAnimation(activity_exercise.this, R.anim.shake);
                    exerciseName.startAnimation(shake);
                    exerciseName.setError("Please enter numeric value and it cannot be empty");
                    vibrateField();
                    return;
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
}

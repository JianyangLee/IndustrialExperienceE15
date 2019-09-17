package com.example.industrialexperiencee15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Pedometer extends AppCompatActivity implements SensorEventListener ,StepListener {
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accelorometerSensor;
    private static final String TEXT_NUM_STEPS = "Steps: ";
    private int numberOfSteps;

    TextView currentStepsTakenByUser;
    Button BtnStartCountingSteps;
    Button BtnStopCountingSteps;

    //Reference: http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.XXksnWkzY2x
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelorometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        currentStepsTakenByUser = (TextView) findViewById(R.id.tv_steps);
        BtnStartCountingSteps = (Button) findViewById(R.id.btn_start);
        BtnStopCountingSteps = (Button) findViewById(R.id.btn_stop);




        BtnStartCountingSteps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numberOfSteps = 0;
                sensorManager.registerListener(Pedometer.this, accelorometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
                showStartStatus();
            }
        });

        BtnStopCountingSteps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sensorManager.unregisterListener(Pedometer.this);

                SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
                Integer userSteps = numberOfSteps;
                userSharedPreferenceEditor.putInt("userSteps", Integer.valueOf(userSteps.toString()));
                userSharedPreferenceEditor.apply();

                Intent backToDash = new Intent(Pedometer.this, PedometerDashboard.class);
                Pedometer.this.startActivity(backToDash);

            }
        });


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numberOfSteps++;
        currentStepsTakenByUser.setText(TEXT_NUM_STEPS + numberOfSteps);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void showStartStatus() {
        Toast.makeText(this, "Steps Counting Started !", Toast.LENGTH_LONG).show();
    }


}

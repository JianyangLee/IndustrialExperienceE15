package com.example.industrialexperiencee15;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Notification extends AppCompatActivity {

    private int notificationId = 1;
    EditText input;
    TimePicker timePicker;
    AlarmManager alarm;
    PendingIntent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        createNotificationChannel();

        final Button setbutton = (Button) findViewById(R.id.setReminder);
        final Button setGoBack = (Button) findViewById(R.id.setGoBack);
        final Button cancel = (Button) findViewById(R.id.cancel);

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

        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = (EditText) findViewById(R.id.scheduleTask);
                timePicker = (TimePicker) findViewById(R.id.timePicker);

                Intent intent = new Intent(Notification.this, AlarmReceiver.class);
                intent.putExtra("notificationId", notificationId);
                intent.putExtra("todo", input.getText().toString());
                alarmIntent = PendingIntent.getBroadcast(Notification.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);

                long alarmStartTime = startTime.getTimeInMillis();

                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);

                Toast.makeText(Notification.this,"Done.",Toast.LENGTH_LONG).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.cancel(alarmIntent);
                Toast.makeText(Notification.this,"Canceled.",Toast.LENGTH_LONG).show();
            }
        });


        setGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToDash = new Intent(Notification.this, Dashboard.class);
                Notification.this.startActivity(backToDash);
            }
        });

    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ReminderChannel";
            String description = "Channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify",name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intent_Tracker = new Intent(Notification.this, Tracker.class);
                    startActivity(intent_Tracker);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_activity_exercise = new Intent(Notification.this, Dashboard.class);
                    startActivity(intent_activity_exercise);
                    ;
                    return true;
                case R.id.navigation_exercise:
                    Intent intent_Dashboard = new Intent(Notification.this, activity_exercise.class);
                    startActivity(intent_Dashboard);
                    return true;
                case R.id.navigation_settings:
                    Intent intent_Settings = new Intent(Notification.this, UserSettingsHome.class);
                    startActivity(intent_Settings);
                    return true;
            }
            return false;
        }
    };
}

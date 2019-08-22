package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final Button Insert = (Button) findViewById(R.id.btnTile1);


        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert = new Intent(Dashboard.this, Tracker.class);
                Dashboard.this.startActivity(insert);
            }
        });

    }
}
//a
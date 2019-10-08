package com.example.industrialexperiencee15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ID extends AppCompatActivity {

    String userID;
    TextView ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ID = (TextView) findViewById(R.id.idCopy);

        ID.setText(userID);
    }
}

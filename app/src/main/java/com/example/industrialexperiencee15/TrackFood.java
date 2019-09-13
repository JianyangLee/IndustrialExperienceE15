package com.example.industrialexperiencee15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class TrackFood extends AppCompatActivity {

    FirebaseFirestore db;
    Button backbtn;
    TextView trackText;
    String testValue;
    String b;
    String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_food);

        db = FirebaseFirestore.getInstance();
        trackText = (TextView) findViewById(R.id.trackText);

        backbtn = (Button) findViewById(R.id.btnBackTrack);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTodash = new Intent(TrackFood.this, Dashboard.class);
                TrackFood.this.startActivity(goTodash);
            }
        });

        ReadingAsyncTask readingasync = new ReadingAsyncTask();
        readingasync.execute();
    }

    private class ReadingAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            db.collection("consumption").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String result = "";
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                    String todayDate = currentDate.format(c.getTime());
                    if (! queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Consumption con = d.toObject(Consumption.class);
                            if (todayDate.equals(con.getCon_date()) && userID.equals(con.getUID())) {
                                result = result + con.getName() + ":  ->  " +con.getType() + "\n" + "\n";
                            }
                        }
                        trackText.setText(result);
                        }
                    else{
                        trackText.setText(result);
                    }
                    }
                }
            );

            return "";
        }

        @Override
        protected void onPostExecute(String response) {

        }
    }
}






//db.collection("consumption").addSnapshotListener(new EventListener<QuerySnapshot>() {
//@Override
//public void onEvent(@Nullable QuerySnapshot DocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//        for (DocumentChange doc: DocumentSnapshots.getDocumentChanges()) {
//        if (doc.getType() == DocumentChange.Type.ADDED) {
//        Consumption cons = doc.getDocument().toObject(Consumption.class);
//
//        a = a + cons.getName();
//        }
//        }
//        b = a;
//        }
//        });
//        b = a;
//        Log.e("b",b);
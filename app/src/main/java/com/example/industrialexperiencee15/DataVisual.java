package com.example.industrialexperiencee15;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataVisual extends AppCompatActivity {
    private static TextView dateView, underText;
    private static Button getButton;
    String userID;
    FirebaseFirestore db;
    Integer[] data = new Integer[3];
    String[] displayName = {"% Calorie consumed", "% Calorie burned", "% Remaining calorie"};

    final Calendar calendar = Calendar.getInstance();
    double calConsumed = 0;
    double calBurned = 0;
    int calLeft = 0;
    private static String Your_visual_tool = "Pie Chart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visual);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        dateView = (TextView) findViewById(R.id.DateView);
        getButton = (Button) findViewById(R.id.btnGet);
        underText = (TextView) findViewById(R.id.underpi);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateView.setText(sdf.format(calendar.getTime()));
            }
        };

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(DataVisual.this,date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        getButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (dateView.getText().toString().equals("")){
                    dateView.setError("Please pick a date befor clicking button");
                    vibrate();
                }
                else {
                    GetAsyncTask getAsyncTask = new GetAsyncTask();
                    getAsyncTask.execute(dateView.getText().toString());
                    underText.setText("This is your daily consumption result with percentage value.");
                    underText.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            }
        });



    }

    private class GetAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String burned = "";
            String returnValue = RestService.getBurned(userID, params[0]);
            try{
                JSONObject jsnobject = new JSONObject(returnValue);
                JSONArray jsonArr = jsnobject.getJSONArray("data");
                JSONObject obj = jsonArr.getJSONObject(0);
                burned = obj.getString("SUM(ENERGY_BURNED)");


            }catch (Exception e){

            }
            if(! burned.equals("null")) {
                calBurned = Double.parseDouble(burned);
            }

            SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userLeft", Context.MODE_PRIVATE);
            calLeft = userSharedPreferenceDetails.getInt("Left",0);
            if (calLeft > 0){

            }else{
                displayName[2] = "% Over calorie";
                calLeft = calLeft * -1;
            }


            db.collection("consumption").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            Consumption con = d.toObject(Consumption.class);
                            if (dateView.getText().toString().equals(con.getCon_date()) && userID.equals(con.getUID())) {
                                calConsumed = calConsumed + con.getCalorie();
                            }
                        }
                    }

                    data[0] = (int)(calConsumed);
                    data[1] = (int)(calBurned);
                    data[2] = calLeft;

                    if (data[0] == 0){
                        displayName[0] = "No data";
                    }
                    if (data[1] == 0){
                        displayName[1] = "No data";
                    }

                    PieChart pieChart = (PieChart) findViewById(R.id.chart);
                    Description description = new Description();
                    description.setText("Your report");
                    pieChart.setDescription(description);
                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                    defaultFormat.setMinimumFractionDigits(1);

                    for (int i = 0; i < data.length; i++) {
                        pieEntries.add(new PieEntry(((float)data[i] / (float)(data[0] + data[1] + data[2])) * 100, displayName[i]));
                    }

                    //create the data set
                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "Pie Chart for total value");
                    pieDataSet.setSliceSpace(2);
                    pieDataSet.setValueTextSize(12);

                    //add colors to dataset
                    ArrayList<Integer> colors = new ArrayList<>();
                    colors.add(Color.BLUE);
                    colors.add(Color.DKGRAY);
                    colors.add(Color.RED);

                    pieDataSet.setColors(colors);

                    //add legend to chart
                    Legend legend = pieChart.getLegend();
                    legend.setForm(Legend.LegendForm.CIRCLE);
                    legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                    //create the data object
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.invalidate();
                    getButton.setVisibility(View.GONE);
                    dateView.setVisibility(View.GONE);
                }
            });

            return "Your report is generated";
        }

        @Override
        protected void onPostExecute(String response) {
            
        }
    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}

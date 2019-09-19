package com.example.industrialexperiencee15;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Graph extends AppCompatActivity {
    String userID;
    YAxis leftAxis;
    YAxis rightAxis;

    List<BarEntry> entriesConsumed = new ArrayList<>();
    List<BarEntry> entriesBurned = new ArrayList<>();

    BarChart graph = null;
    final Calendar calendar = Calendar.getInstance();
    Calendar startDate;
    Calendar endDate;

    FirebaseFirestore db;
    double calConsumed = 0;
    double calBurned = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        final TextView start = (TextView) findViewById(R.id.startDate);
        final TextView end = (TextView) findViewById(R.id.endDate);
        final Button get = (Button) findViewById(R.id.btnGraph);
        final Button back = (Button) findViewById(R.id.btnGraphBackToDash);
        graph =(BarChart) findViewById(R.id.bargraph);


        leftAxis = graph.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        graph.getAxisRight().setEnabled(false);
        rightAxis = graph.getAxisRight();

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                start.setText(sdf.format(calendar.getTime()));
            }
        };


        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                end.setText(sdf.format(calendar.getTime()));
            }
        };


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(Graph.this,date1,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(Graph.this,date2,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToDash = new Intent(Graph.this, Dashboard.class);
                Graph.this.startActivity(backToDash);
            }
        });

        get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (start.getText().toString().equals("") || end.getText().toString().equals("")){
                    Animation shake = AnimationUtils.loadAnimation(Graph.this, R.anim.shake);
                    start.startAnimation(shake);
                    end.startAnimation(shake);
                    start.setError("Plear pick up a start date");
                    end.setError("Please pick up an end date");
                    vibrateField();
                }else {
                    GetAsyncTask getAsyncTask = new GetAsyncTask();
                    String startDate = start.getText().toString();
                    String endDate = end.getText().toString();
                    getAsyncTask.execute(startDate, endDate);
                    get.setVisibility(View.GONE);
                }
            }
        });
    }

    private class GetAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Integer totalConsumed = 0;
            Integer totalBurned = 0;

            final List<String> xValue = new ArrayList<>();
            graph.setDrawBarShadow(false);
            graph.setDrawValueAboveBar(true);
            Description description = new Description();
            description.setText("");
            graph.setDescription(description);
            graph.setMaxVisibleValueCount(50);
            graph.setPinchZoom(false);
            graph.setDrawGridBackground(false);

            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date Start = null;
            Date End = null;
            try {
                Start = formatter.parse(params[0]);//catch exception
                End = formatter.parse(params[1]);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            startDate = Calendar.getInstance();
            startDate.setTime(Start);
            endDate = Calendar.getInstance();
            endDate.setTime(End);

            int i = 1;
            while(! startDate.after(endDate)) {
                Date s = startDate.getTime();

                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                String dateOfTheDay = df.format(s);


                String consumed = "";
                String returnValue1 = RestService.getConOneDay(userID, dateOfTheDay);
                try{
                    JSONObject jsnobject = new JSONObject(returnValue1);
                    JSONArray jsonArr = jsnobject.getJSONArray("data");
                    JSONObject obj = jsonArr.getJSONObject(0);
                    consumed = obj.getString("SUM(CALORIE)");
                }catch (Exception e){

                }


                String burned = "";
                String returnValue2 = RestService.getBurned(userID, dateOfTheDay);
                try{
                    JSONObject jsnobject = new JSONObject(returnValue2);
                    JSONArray jsonArr = jsnobject.getJSONArray("data");
                    JSONObject obj = jsonArr.getJSONObject(0);
                    burned = obj.getString("SUM(ENERGY_BURNED)");
                }catch (Exception e){
                }
                if(! burned.equals("null") && ! consumed.equals("null")) {
                    calBurned = Double.parseDouble(burned);
                    calConsumed = Double.parseDouble(consumed);
                }
                else{
                    calBurned = 0.0;
                    calConsumed = 0.0;
                }

                xValue.add(dateOfTheDay);
                entriesConsumed.add(new BarEntry(i,(int)(calConsumed)));
                entriesBurned.add(new BarEntry(i,(int)(calBurned)));
                i++;
                startDate.add(Calendar.DATE,1);
            }

            BarDataSet datasetConsumed = new BarDataSet(entriesConsumed, "Consumed");
            datasetConsumed.setColor(Color.BLUE);
            BarDataSet datasetBurned = new BarDataSet(entriesBurned, "Burned");
            datasetBurned.setColor(Color.RED);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(datasetConsumed);
            dataSets.add(datasetBurned);

            BarData data = new BarData(dataSets);
            graph.setData(data);


            float groupSpace = 0.6f;
            float barSpace = 0f;
            float barWidth =0.2f;


            graph.getBarData().setBarWidth(barWidth);

            XAxis xAxis = graph.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(entriesConsumed.size());
            xAxis.setTextSize(15);
            xAxis.setDrawGridLines(true);
            xAxis.setLabelRotationAngle(90f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));

            graph.setFitBars(true);
            graph.groupBars(0, groupSpace, barSpace);
            graph.invalidate();

            return "";
        }

        @Override
        protected void onPostExecute(String response) {


        }
    }

    private void vibrateField() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }
}

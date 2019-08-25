package com.example.industrialexperiencee15;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Tracker extends AppCompatActivity {
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] foodList;
    EditText foodName;
    EditText amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        listView = (ListView) findViewById(R.id.listView);

        foodName = (EditText) findViewById(R.id.foodName);
        amount = (EditText) findViewById(R.id.amount);
        Button addFood = (Button) findViewById(R.id.btnAdd);

        initialList();

        foodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    initialList();
                    Toast.makeText(Tracker.this,"Search your food and add it.",Toast.LENGTH_LONG).show();
                }
                else{
                    searchItem(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String value = item.toString();
                foodName.setText(value);
            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(foodName.getText().toString().equals("") && amount.getText().toString().equals("")) && isInteger(amount.getText().toString())){
                    Intent goToDashboard = new Intent(Tracker.this, Dashboard.class);
                    Tracker.this.startActivity(goToDashboard);
                }else {
                    amount.setError("Please enter numeric value and it cannot be empty");
                }
            }
        });

    }

    private void searchItem(String textSearch) {
        for (String food:foodList){
            if(!food.contains(textSearch)){
                listItems.remove(food);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void initialList(){



        foodList = new String[] {"Beef","Egg","Chicken","Apple","Pork","Lamb","Drumstick","Wings","Salad","Chips","Pear"};
        listItems = new ArrayList<>(Arrays.asList(foodList));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView,listItems);
        listView.setAdapter(adapter);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}


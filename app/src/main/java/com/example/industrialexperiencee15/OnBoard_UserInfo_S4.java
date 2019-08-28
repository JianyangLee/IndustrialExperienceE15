package com.example.industrialexperiencee15;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class OnBoard_UserInfo_S4 extends AppCompatActivity {

    private Button btnNextToUserComputedGoal;
    private Button btnPrevoiusToActivityLevel;
    private EditText dateOfBirth;
    private java.sql.Date birthDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText firstName;
    private EditText lastName;
    private EditText height;
    private EditText weight;
    private String userAge;

    //Radio Buttons
    private RadioGroup radioGroup;
    private RadioButton gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board__user_info__s4);


        btnPrevoiusToActivityLevel = (Button) findViewById(R.id.S4PreviousButton);
        btnNextToUserComputedGoal = (Button) findViewById(R.id.S4Nextbutton);
        dateOfBirth = (EditText) findViewById(R.id.dob);
        radioGroup = (RadioGroup) findViewById(R.id.radio_gender);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);

        //Date Picker
        //REF:https://www.youtube.com/watch?v=hwe1abDO2Ag
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        OnBoard_UserInfo_S4.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Since Month Starts from one
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                dateOfBirth.setText(date);
                userAge = getAge(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    java.util.Date utilDate = sdf.parse(date);
                    birthDate = new java.sql.Date(utilDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };


        //If the user gets Started in his application
        btnPrevoiusToActivityLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                Intent intent = new Intent(OnBoard_UserInfo_S4.this, OnBoard_ActivityLevel_S3.class);
                startActivity(intent);
            }
        });

        //On Register Button Click
        btnNextToUserComputedGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegisterFormValues()) {
                    // Calculation Process is continued after all the validations
                    //Radio Button Gender
                    int buttonSelected = radioGroup.getCheckedRadioButtonId();
                    gender = (RadioButton) findViewById(buttonSelected);
                    // Writing Information to the shared Preferences
                    writeUserInformationtoSharedPreferences();
                    //Redirect
                    Intent intent = new Intent(OnBoard_UserInfo_S4.this, OnBoard_CalculatedGoal_S5.class);
                    startActivity(intent);

                }
            }
        });
        //Registered Successfully
    }

    private boolean validateRegisterFormValues() {
        boolean isdataValidInUserRegistrationForm = true;
        if (!validateName(firstName.getText().toString(), firstName)) {
            isdataValidInUserRegistrationForm = false;
        } else if (!validateName(lastName.getText().toString(), lastName)) {
            isdataValidInUserRegistrationForm = false;
        } else if (!validateHeight(height.getText().toString(), height)) {
            isdataValidInUserRegistrationForm = false;
        } else if (!validateWeight(weight.getText().toString(), weight)) {
            isdataValidInUserRegistrationForm = false;
        }
        return isdataValidInUserRegistrationForm;
    }


    private boolean validateName(String name, EditText nameType) {
        //Ensuring the field is within the limit of the Database Field
        if (!(name.matches("^[a-zA-Z]{2,35}$"))) {
            nameType.setError("Name can have only alphabets and must be of 2 to 35 characters.");
            nameType.requestFocus();
            return false;
        }
        return true;
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int today_m = today.get(Calendar.MONTH);
        int dob_m = dob.get(Calendar.MONTH);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (dob_m > today_m) {
            age--;
        } else if (dob_m == today_m) {
            int day_today = today.get(Calendar.DAY_OF_MONTH);
            int day_dob = dob.get(Calendar.DAY_OF_MONTH);
            if (day_dob > day_today) {
                age--;
            }
        }
        return age + "";
    }


    private boolean validateHeight(String heightValue, EditText height) {
        //Ensuring the field is within the limit of the Database Field
        if (!(heightValue.matches("^[0-9]{2,3}$"))) {
            height.setError("Height can be Either 2 or 3 Numeric Characters");
            height.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateWeight(String weightValue, EditText weight) {
        //Ensuring the field is within the limit of the Database Field
        if (!(weightValue.matches("^[0-9]{2,3}$"))) {
            weight.setError("Weight can be Either 2 or 3 Numeric Characters");
            weight.requestFocus();
            return false;
        }
        return true;
    }


    private void writeUserInformationtoSharedPreferences() {
        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor userSharedPreferenceEditor = userSharedPreferenceDetails.edit();
        userSharedPreferenceEditor.putString("firstName", firstName.getText().toString());
        userSharedPreferenceEditor.putString("lastName", lastName.getText().toString());
        userSharedPreferenceEditor.putFloat("height", Float.valueOf(height.getText().toString()));
        userSharedPreferenceEditor.putFloat("weight", Float.valueOf(weight.getText().toString()));
        if (getString(R.string.radio_male).equals(gender.getText())) {
            userSharedPreferenceEditor.putBoolean("isUserMale", true);
        } else {
            userSharedPreferenceEditor.putBoolean("isUserMale", false);
        }
        if (getString(R.string.radio_female).equals(gender.getText())) {
            userSharedPreferenceEditor.putBoolean("isUserFeMale", true);
        } else {
            userSharedPreferenceEditor.putBoolean("isUserFeMale", false);
        }
        userSharedPreferenceEditor.putInt("age", Integer.parseInt(userAge));
        userSharedPreferenceEditor.apply();
    }

}

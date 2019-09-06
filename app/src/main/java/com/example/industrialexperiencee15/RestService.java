package com.example.industrialexperiencee15;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestService {
    private static final String BASE_URL = "";
    private static String goal = "0";



    public static void createUser(User user){
        Log.i("create user","message");
        final String methodPath = "";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        //making HTTP request
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonUser = gson.toJson(user);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(jsonUser.getBytes().length); //add HTTP headers
            Log.i("userJson",jsonUser);
            conn.setRequestProperty("Content-Type", "application/json");
            Log.i("pass",url.toString());
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(jsonUser);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static String getAllExercise(){
        final String methodPath = "";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(BASE_URL + methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "TEXT/PLAIN");
            conn.setRequestProperty("Accept", "TEXT/PLAIN");
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            textResult = inStream.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

}

package com.example.industrialexperiencee15;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestService {
    private static String goal = "0";



    public static void createUser(User user){
        Log.i("create user","message");
        final String methodPath = "https://2q13dirdtf.execute-api.ap-southeast-2.amazonaws.com/insertnewuser";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        //making HTTP request
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonUser = gson.toJson(user);
            url = new URL(methodPath);
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

    public static void createUserInfo(Userinfo userinfo){
        Log.i("create workout","message");
        final String methodPath = "https://jnknlzj91b.execute-api.ap-southeast-2.amazonaws.com/insertUserinf";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        //making HTTP request
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonWorkout = gson.toJson(userinfo);
            url = new URL(methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(jsonWorkout.getBytes().length); //add HTTP headers
            Log.i("jsonWorkout",jsonWorkout);
            conn.setRequestProperty("Content-Type", "application/json");
            Log.i("pass",url.toString());
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(jsonWorkout);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static void createWorkout(Workout workout){
        Log.i("create workout","message");
        final String methodPath = "https://1r6vx6xa47.execute-api.ap-southeast-2.amazonaws.com/insertnewworkout";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        //making HTTP request
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonWorkout = gson.toJson(workout);
            url = new URL(methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(jsonWorkout.getBytes().length); //add HTTP headers
            Log.i("jsonWorkout",jsonWorkout);
            conn.setRequestProperty("Content-Type", "application/json");
            Log.i("pass",url.toString());
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(jsonWorkout);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static String getAllExercise(){
        final String methodPath = "https://22niilhgg9.execute-api.ap-southeast-2.amazonaws.com/allExercise";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String findByUseridAndDate(String userid, String date) {
        final String methodPath = "https://f2mq9ot53k.execute-api.ap-southeast-2.amazonaws.com/getWorkout/" + userid + "/" + date;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getBurned(String userid, String date){
        final String methodPath = "https://vc2jbshco5.execute-api.ap-southeast-2.amazonaws.com/getBurned/" + userid + "/" + date;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getAllFaciliites(){
        final String methodPath = "https://fx4bgiprlk.execute-api.ap-southeast-2.amazonaws.com/allFacilities";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getBySportPlayed(String sport){
        final String methodPath = "https://k61t1jbgbd.execute-api.ap-southeast-2.amazonaws.com/newGet/" + sport;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static void createConsumption(Consumption consumption){
        final String methodPath = "https://m316698ts3.execute-api.ap-southeast-2.amazonaws.com/insertCon";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        //making HTTP request
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonUser = gson.toJson(consumption);
            url = new URL(methodPath);
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

    public static String getConOneDay(String uid, String con_date){
        final String methodPath = "https://wc8gn53s9a.execute-api.ap-southeast-2.amazonaws.com/getConBy/" + uid + "/" + con_date;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getAllWorkoutById(String id){
        final String methodPath = "https://65hu4csdf7.execute-api.ap-southeast-2.amazonaws.com/getWorkoutByUID/" + id;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getAllConsumptionyUId(String id){
        final String methodPath = "https://2a6qwui660.execute-api.ap-southeast-2.amazonaws.com/getAllConByUID/" + id;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getAllConsumptionyUIdAndDate(String id, String date){
        final String methodPath = "https://78fiwcdx0c.execute-api.ap-southeast-2.amazonaws.com/getAllConByUIDAndDate/" + id + "/" + date;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getExerciseByRange(int x, int y){
        final String methodPath = "https://rp5ck2669j.execute-api.ap-southeast-2.amazonaws.com/getExerciseRange/" + x + "/" + y;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //making HTTP request
        try {
            url = new URL(methodPath);
            //open connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //READ the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
        return textResult;
    }
}

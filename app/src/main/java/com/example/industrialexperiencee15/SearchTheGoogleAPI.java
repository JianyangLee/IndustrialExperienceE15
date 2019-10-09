package com.example.industrialexperiencee15;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchTheGoogleAPI {

    private static final String GOOGLE_API_KEY = "AIzaSyCxUGY-W3RTjxRlivUAtPV8SfHaLZ-ucLA";
    private static final String SEARCH_ID_cx = "014405748879047181333:jbt28gwx22y";

    //Search for the keyword and get the Results from API
    public static String search(String keyword, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String textResultFromGoogleAPI = "";
        String query_parameter = "";
        if (params != null && values != null) {
            for (int i = 0; i < params.length; i++) {
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key=" +
                    GOOGLE_API_KEY + "&cx=" + SEARCH_ID_cx + "&q=" + keyword + query_parameter);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine()) {
                textResultFromGoogleAPI += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return textResultFromGoogleAPI;
    }

    //'Snippet' is taken out from teh above Response
    // this give the information about the Keyword that was searched for in the first place
    public static String getSnippetFromAPIResponse(String result) {
        String snippet = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray != null && jsonArray.length() > 0) {
                snippet = jsonArray.getJSONObject(0).getString("snippet");
                snippet = snippet.replaceAll("[...]", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    //Search the image related to the keyword typed and get the Json result
    public static String searchImage(String keyword, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter = "";
        if (params != null && values != null) {
            for (int i = 0; i < params.length; i++) {
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
                query_parameter += "&";
            }
        }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key=" +
                    GOOGLE_API_KEY + "&cx=" + SEARCH_ID_cx + "&q=" + keyword + query_parameter);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }


    //Snip out the image url from the from the JSON results obtained as a result of API call from the above method searchImage()
    public static String getUrl(String result) {
        String imageResponse = null;

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray != null && jsonArray.length() > 0) {
                imageResponse = jsonArray.getJSONObject(0).getString("link");
            }

        } catch (Exception e) {
            e.printStackTrace();
            imageResponse = "NO IMAGE FOUND";
        }
        return imageResponse;
    }


}

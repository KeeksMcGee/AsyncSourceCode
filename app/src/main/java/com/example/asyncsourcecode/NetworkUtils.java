package com.example.asyncsourcecode;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    static String getSourceCodeInfo(Context context, String queryString, String httpSwitch){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String sourceJSONString = null;

        String[] changeUp= context.getResources().getStringArray(R.array.https_array);
        try{
            Uri stacker;
            if(httpSwitch.equals(changeUp[0])){
                stacker = Uri.parse(queryString).buildUpon().scheme(HTTP).build();
            } else{
                stacker = Uri.parse(queryString).buildUpon().scheme(HTTPS).build();
            }

            URL requestURL = new URL(stacker.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) !=null){
                //Add the current line to the string.
                builder.append(line);
            }

            if(builder.length() ==0){
                return null;
            }

            sourceJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.d(LOG_TAG, sourceJSONString);
        return sourceJSONString;
    }
}


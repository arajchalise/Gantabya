package com.gantabya.project;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RouteFinder extends AsyncTask<Void, Void, Void> {
    private String data = "";
    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //URL url = new URL("https://api.mapbox.com/directions/v5/mapbox/walking/27.6782775%2C85.2792614%3B27.6818404%2C85.2733606.json?access_token=pk.eyJ1IjoiYXJhamNoYWxpc2UiLCJhIjoiY2p1bDRqdDdyMXgxdzQ0cWppN2pxYXV2NCJ9.WxFGIEDUdenT6EwfesmY9Q");
            URL url = new URL("https://api.myjson.com/bins/9w3gn");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            data = data+url;
            String line="";
            while (line != null){
                line = bufferedReader.readLine();
                data = data+line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //MapActivity.tv.setText("OK");
    }
}

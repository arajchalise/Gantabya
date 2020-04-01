package com.gantabya.project;

import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void, Void, JSONArray> {
    String data="[";
    public static double Lat = 0, Lang = 0;
    public static String testS;
    @Override
    protected JSONArray doInBackground(Void... Voids) {
        try {
            URL url = new URL("https://api.mapbox.com/geocoding/v5/mapbox.places/"+MainActivity.destination.getText().toString()+".json?limit=1&access_token=pk.eyJ1IjoiYXJhamNoYWxpc2UiLCJhIjoiY2p1bDRuemo4MHhrZTQ0bnFra3F1aWF3ZSJ9.Whte2w6BtkDqheu2kukCeg");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                data = data + line;
            }
            data = data +"]";
            testS = data;
            JSONArray jsonArray = new JSONArray(data);
           Lang = jsonArray.getJSONObject(0).getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
           Lat = jsonArray.getJSONObject(0).getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray aVoid) {
        super.onPostExecute(aVoid);

   }


}

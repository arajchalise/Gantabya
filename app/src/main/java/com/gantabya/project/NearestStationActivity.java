package com.gantabya.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;

public class NearestStationActivity extends AppCompatActivity {
    private DatabaseHelper myDB;
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371000;
    String sRoute, routeName, tempRouteS, tempRouteD;
    String[] sName = new String[10];
    double[] sDis = new double[10];
    String[] dName = new String[10];
    double[] dDis = new double[10];
    String sourceStationName="", destStationName="", tempS="", tempD="";
    double sourceDistance=300, destDistance=300;
    double lat, lang, dLat, dLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_nearest_station);
        myDB = new DatabaseHelper(this);
         Intent intent = getIntent();
         lat = intent.getDoubleExtra("lat", 0);
         lang = intent.getDoubleExtra("lang", 0);
         dLat = intent.getDoubleExtra("dLat", 0);
         dLang = intent.getDoubleExtra("dLang", 0);
        //Toast.makeText(this, lat+" "+lang+",, "+dLat+", "+dLang, Toast.LENGTH_LONG).show();
        Cursor rs = myDB.getAllData("stations");
        int i=0;
        int j=0;
        while(rs.moveToNext()) {
            double slat = rs.getDouble(2);
            double slang = rs.getDouble(3);
            double distance1 = calculateDistanceInMeter(slat, slang, lat, lang);
            double distance2 = calculateDistanceInMeter(slat, slang, dLat, dLang);
            if(distance1 < 300) {
                    sName[i] = rs.getString(1);
                    sDis[i] = distance1;
                    i++;
            }
            if(distance2 < 300) {
                    dName[j] = rs.getString(1);
                    dDis[j] = distance2;
                    j++;
            }

        }
        if(sName[0]==null){
            showAlert();
        } else if (dName[0]== null){
            showAlert();
        } else {
            int k = 0;
            while (sName[k] != null){
                int l = 0;
                while (dName[l] != null){
                    sRoute = myDB.getRouteName(sName[k]);
                    String dRoute = myDB.getRouteName(dName[l]);
                    if(sRoute.equalsIgnoreCase(dRoute)){
                        sourceStationName = sName[k];
                        destStationName = dName[l];
                        routeName = sRoute;
                    }  else {
                        if (sourceDistance > sDis[k]){
                            tempS = sName[k];
                            tempRouteS = sRoute;
                        }
                        if(destDistance > dDis[l]){
                            tempD = dName[l];
                            tempRouteD = dRoute;
                        }
                    }
                    l++;
                }
                k++;
            }
            if(sourceStationName.isEmpty() && destStationName.isEmpty()){
                sourceStationName = tempS;
                destStationName = tempD;
                openDataViewerActivity(sourceStationName, destStationName, tempRouteS, tempRouteD, lat, lang, dLat, dLang);
            } else{
                openNewData(sourceStationName, destStationName, routeName, dLat, dLang, lat, lang);
            }
        }
        //showData("test", testS);
    }

    public double calculateDistanceInMeter(double userLat, double userLng,
                                               double venueLat, double venueLng) {
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (double) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
    }

    public void showData(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void openDataViewerActivity(String str1, String str2, String str3, String str4, double d1, double d2, double d3, double d4){
        Intent intent = new Intent(this, DataViewerActivity.class);
        intent.putExtra("source", str1);
        intent.putExtra("dest", str2);
        intent.putExtra("sourceRoute", str3);
        intent.putExtra("destRoute", str4);

        intent.putExtra("lat", d1);
        intent.putExtra("lang", d2);
        intent.putExtra("dLat", d3);
        intent.putExtra("dLang", d4);
        startActivity(intent);
    }

    private void openNewData(String s1, String s2, String s3, double d1, double d2, double d3, double d4){
        Intent intent = new Intent(this, DataNewActivity.class);
        intent.putExtra("start", s1);
        intent.putExtra("end", s2);
        intent.putExtra("route", s3);
        intent.putExtra("dLat", d1);
        intent.putExtra("dLang", d2);
        intent.putExtra("gLat", d3);
        intent.putExtra("gLang", d4);
        startActivity(intent);
    }
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No Information Found about this place");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openMainActivity();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
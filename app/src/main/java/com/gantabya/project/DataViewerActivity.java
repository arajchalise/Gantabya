package com.gantabya.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DataViewerActivity extends AppCompatActivity
{

    DatabaseHelper myDB;
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371000;
    private double distance, slat, slang, dlat, dlang, ssld, stlat, stlang;
    private String[] tempDest = new String[10];
    private int sid, sourceId;
    TextView textView;
    Button btn;
    private int[] stationsTraversed = new int[100];
    private int[] neighbor = new int[95];
    private int[] sld = new int[100];
    private int routeIdPrev=0, stationId;
     String testS = "";
     String routes;
    int[] a = new int[95];
    int[] b = new int[50];
    double gLat;
    double gLang;
    double pLat;
    double pLang;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_viewer);
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        String dest = intent.getStringExtra("dest");
        String sourceRoute = intent.getStringExtra("sourceRoute");
        String destRoute = intent.getStringExtra("destRoute");
         gLat = intent.getDoubleExtra("lat", 0.0);
         gLang = intent.getDoubleExtra("lang", 0.0);
         pLat = intent.getDoubleExtra("dLat", 0.0);
         pLang = intent.getDoubleExtra("dLang", 0.0);
        //Toast.makeText(DataViewerActivity.this, gLat+" "+gLang+" "+pLat+" "+pLang, Toast.LENGTH_LONG).show();
        textView = findViewById(R.id.textView);
        btn = findViewById(R.id.goMap);
        myDB = new DatabaseHelper(this);
        Graph g  = new Graph();
        Cursor stations = myDB.getAllDataWithRoute();
        Cursor s = myDB.find(source);
        Cursor d = myDB.find(dest);
        while (s.moveToNext()){
            sourceId = s.getInt(0);
            slat = s.getDouble(2);
            slang =s.getDouble(3);
        }
        while (d.moveToNext()){
            sid = d.getInt(0);
            dlat = d.getDouble(2);
            dlang = d.getDouble(3);
        }
        Cursor station1 = myDB.getAllDataWithRoute();
        while (station1.moveToNext()){
            g.addVertex(station1.getString(1));
        }

        while(stations.moveToNext()){
                stationId = stations.getInt(0);
                routeIdPrev = myDB.findRouteId(stationId);
                slat = stations.getDouble(2);
                slang = stations.getDouble(3);
                Cursor station = myDB.getAllDataWithRoute();
                while (station.moveToNext()){
                    int route = station.getInt(5);
                    if(route == routeIdPrev){
                        stlat = station.getDouble(2);
                        stlang = station.getDouble(3);
                        int slds = (int)calculateDistanceInMeter(stlat, stlang, dlat, dlang);
                        if(station.getInt(0)== stationId-1 || station.getInt(0) == stationId+1){
                            g.addEdge(stationId, station.getInt(0));
                        }
                    } else {
                        stlat = station.getDouble(2);
                        stlang = station.getDouble(3);
                        int slds = (int)calculateDistanceInMeter(stlat, stlang, dlat, dlang);
                        int sld = (int)calculateDistanceInMeter(stlat, stlang, slat, slang);
                        if(sld < 300) {
                            g.addEdge(stationId, station.getInt(0));
                        }
                    }
                }
        }
        a = g.dfs(sourceId, sid);
        int i = 1;
        int w = 300, w1=300;
        while (a[i] != 0){
            Cursor cs = myDB.findStation(a[i]);
            while (cs.moveToNext()){
                double lt = cs.getDouble(2);
                double lng = cs.getDouble(3);
                double dt1 = calculateDistanceInMeter(gLat, gLang, lt, lng);
                double dt2 = calculateDistanceInMeter(pLat, pLang, lt ,lng);
                if (dt1 < w && a[i] != sourceId){
                   if (myDB.findRouteName(a[i]).equalsIgnoreCase(myDB.findRouteName(sourceId))){
                   } else if (myDB.findRouteName(a[i]).equalsIgnoreCase(myDB.findRouteName(a[i+1]))){
                       sourceId = a[i];
                       sourceRoute = myDB.findRouteName(sourceId);
                       w = (int) dt1;
                   }

                } else if (dt2 < w1 && a[i] != sid ){
                    if (myDB.findRouteName(a[i]).equalsIgnoreCase(myDB.findRouteName(sourceId))){
                    } else if (myDB.findRouteName(a[i]).equalsIgnoreCase(myDB.findRouteName(a[i-1]))){
                        sid = a[i];
                        destRoute = myDB.findRouteName(sid);
                        w1 = (int) dt2;
                    }
                }

            }
            i++;
        }

        b[1] = sid;
        int k = 1;
        String[] transit1 = new String[5];
        String[] transit2 = new String[5];
        String[] transitRoute = new String[5];
        String transit = "";
        int i1 = 0;
        while (a[k] != sourceId) {
            if (myDB.findRouteName(a[k]).equalsIgnoreCase(destRoute)) {
                b[k] = a[k];
                k++;
            } else {
                 transit = myDB.findRouteName(a[k]);
                if (sourceRoute.equalsIgnoreCase(transit)){
                    b[k] = a[k];
                    transit1[i1] = myDB.findStationName(a[k]);
                    transit2[i1] = myDB.findStationName(a[k-1]);
                    transit = "";
                    i1++;
                    break;
                } else {
                    b[k] = a[k];
                    transitRoute[i1] = transit;
                    transit1[i1] = myDB.findStationName(a[k]);
                    transit2[i1] = myDB.findStationName(a[k-1]);
                    i1++;
                    k++;
                }
            }
        }
        int i2 = i1-1;
        testS = testS+ "From "+myDB.findStationName(sourceId)+ "to "+transit1[i2]+ "via "+sourceRoute+"\n";
        while (i2 != -1){
            testS = testS+"Walk from "+transit1[i2] + " to "+transit2[i2]+"\n";
            if (i2 != 0){
                testS = testS+"From "+transit2[i2]+ " to "+ transit1[i2-1]+" via "+ transitRoute[i2]+"\n";
            }
            i2--;
        }
        testS = testS + "From "+transit2[0]+ " to " +myDB.findStationName(sid)+" via "+destRoute;

        textView.setText(testS);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });

        //showData("Route Information", testS);
       // openMapActivity();
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


public void openMapActivity(){
    Intent intent = new Intent(this, MapActivity.class);
    intent.putExtra("array", a);
    intent.putExtra("gLat", gLat);
    intent.putExtra("gLang", gLang);
    intent.putExtra("sI", sourceId);
    intent.putExtra("pLat", pLat);
    intent.putExtra("pLang", pLang);
    startActivity(intent);
}

}
package com.gantabya.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsManager;

import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    public static TextInputEditText destination;
    double longitude;
    double latitude;
    boolean gps, newtwork;
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    @RequiresApi(api = 28)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DatabaseHelper(this);
        Button btnGo = findViewById(R.id.btnGo);
        destination = findViewById(R.id.textDest);
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        try {
//            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//                gps = true;
//            } else {
//                gps = false;
//            }
//        } catch (Exception ex){}

        try{
            if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                newtwork = true;
            }
            else
                newtwork = false;

        } catch (Exception e){}
        if (!gps){
            showAlert();
        } else if (!newtwork) {
            showNetworkError();
        } else{
//            @SuppressLint("MissingPermission")
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
                Location location = lm.getLastKnownLocation(GPS_PROVIDER);
                if (location != null) {
                    this.longitude = location.getLongitude();
                    this.latitude = location.getLatitude();
                    lm.requestLocationUpdates(GPS_PROVIDER, 100, 10, locationListener);
                }

                btnGo.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        FetchData fetchData = new FetchData();
                        fetchData.execute();
                        int c = myDB.getAllData("stations").getCount();
                        if (c == 0){
                            Toast.makeText(MainActivity.this, "Zero", Toast.LENGTH_SHORT).show();
                        }
                        if(FetchData.Lat != 0 && FetchData.Lang !=0){
                            //Toast.makeText(MainActivity.this, FetchData.Lat+", "+FetchData.Lang+", "+latitude+", "+longitude, Toast.LENGTH_LONG).show();
                            openDVG(latitude, longitude, FetchData.Lat, FetchData.Lang);

                        } else{
                            //Toast.makeText(MainActivity.this, FetchData.Lat+", "+FetchData.Lang, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else{

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                restartApp();

            }


        }


    }

    private void openDVG(double latitude, double longitude, double lat, double lang) {
        Intent intent = new Intent(this, NearestStationActivity.class);
        intent.putExtra("dLat", lat);
        intent.putExtra("dLang", lang);
        intent.putExtra("lat", latitude);
        intent.putExtra("lang", longitude);
        startActivity(intent);
    }

    private void restartApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No GPS Enabled, Click on change Setting to enable your Location Service");
        builder.setPositiveButton("Change Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }
        });
        builder.setNegativeButton("Its' OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            }
        });
        dialog.show();
    }

    public void showLocAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No Location Service Enabled, Click on change Setting to enable your Location Service");
        builder.setPositiveButton("Change Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }
        });
        builder.setNegativeButton("Its' OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            }
        });
        dialog.show();
    }

    public void showNetworkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Not connected to Internet, click on Change Setting to enable Network Service");
        builder.setPositiveButton("Change Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS), 1);
            }
        });
        builder.setNegativeButton("Its' OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            }
        });
        dialog.show();
    }

}

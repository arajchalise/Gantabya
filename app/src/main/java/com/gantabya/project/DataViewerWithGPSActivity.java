//package com.gantabya.project;
//
//import android.content.Intent;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Toast;
//
//import com.mapbox.android.core.permissions.PermissionsListener;
//import com.mapbox.android.core.permissions.PermissionsManager;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.location.LocationComponent;
//import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
//import com.mapbox.mapboxsdk.location.modes.CameraMode;
//import com.mapbox.mapboxsdk.location.modes.RenderMode;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DataViewerWithGPSActivity extends AppCompatActivity implements
//        OnMapReadyCallback, PermissionsListener {
//
//    private PermissionsManager permissionsManager;
//    private MapboxMap mapboxMap;
//    private MapView mapView;
//    private Location location;
//    private double lang, lat;
//    private DataViewerActivity dv;
//    private DatabaseHelper myDB;
//    private ArrayList arrayList;
//    private double destLat, destLang;
//    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371000;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Mapbox.getInstance(this, getString(R.string.access_token));
//        setContentView(R.layout.activity_map);
//        Intent intent = getIntent();
//        destLat = intent.getDoubleExtra("destLat", 0);
//        destLang = intent.getDoubleExtra("destLang", 0);
//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//        myDB = new DatabaseHelper(this);
//        //Toast.makeText(this, location+" ", Toast.LENGTH_LONG).show();
//    }
//    @Override
//    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
//        DataViewerWithGPSActivity.this.mapboxMap = mapboxMap;
//        mapboxMap.setStyle(new Style.Builder().fromUrl("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
//                new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//                        enableLocationComponent(style);
//                    }
//                });
//
//    }
//
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//            locationComponent.activateLocationComponent(
//                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
//            locationComponent.setLocationComponentEnabled(true);
//            location = locationComponent.getLastKnownLocation();
//            lat = location.getLatitude();
//            lang = location.getLongitude();
//            if(location != null){
//                openNearestStationActivity(lat, lang, destLat, destLang);
//            }
//            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
//            locationComponent.setRenderMode(RenderMode.COMPASS);
//
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            mapboxMap.getStyle(new Style.OnStyleLoaded() {
//                @Override
//                public void onStyleLoaded(@NonNull Style style) {
//                    enableLocationComponent(style);
//                }
//            });
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }
//
//    @Override
//    @SuppressWarnings( {"MissingPermission"})
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    private void openNearestStationActivity(double lat, double lang, double dlat, double dlang) {
//        Intent intent = new Intent(this, NearestStationActivity.class);
//        intent.putExtra("lat", lat);
//        intent.putExtra("lang", lang);
//        intent.putExtra("dLat", dlat);
//        intent.putExtra("dLang", dlang);
//        startActivity(intent);
//    }
//
//
//
//}
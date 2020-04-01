package com.gantabya.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static final String GEOJSON_SOURCE_ID = "GEOJSONFILE";
    int[] array = new int[50];
    int sourceId;
    double gLang, gLat, pLat, pLang;
    DatabaseHelper myDB;
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            gLang = location.getLongitude();
            gLat = location.getLatitude();
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

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);
        myDB = new DatabaseHelper(this);
        Intent intent = getIntent();
        array = intent.getIntArrayExtra("array");
        sourceId = intent.getIntExtra("sI", 0);
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        Location location = lm.getLastKnownLocation(GPS_PROVIDER);
        gLang = location.getLongitude();
        gLat = location.getLatitude();
        lm.requestLocationUpdates(GPS_PROVIDER, 2000, 10, locationListener);
        pLat = intent.getDoubleExtra("pLat", 0);
        pLang = intent.getDoubleExtra("pLang", 0);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(gLat, gLang))
                .zoom(15)
                .build());
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                createGeoJsonSource(style);
                addPolygonLayer(style);
                addPointsLayer(style);
                style.addImage("marker-icon-id",
                        BitmapFactory.decodeResource(
                                MapActivity.this.getResources(), R.drawable.man));

                GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                        Point.fromLngLat(gLang, gLat)));
                style.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                symbolLayer.withProperties(
                        PropertyFactory.iconImage("marker-icon-id")
                );

                style.addLayer(symbolLayer);
            }

        });
    }

    private void createGeoJsonSource(@NonNull Style loadedMapStyle) {
        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        int i = 1;
        while (array[i] != sourceId){
            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(myDB.getLang(array[i]),  myDB.getLat(array[i]))));
            i++;
        }
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(myDB.getLang(sourceId), myDB.getLat(sourceId))));
        loadedMapStyle.addSource(new GeoJsonSource(GEOJSON_SOURCE_ID, FeatureCollection.fromFeatures(symbolLayerIconFeatureList)));
    }

    private void addPolygonLayer(@NonNull Style loadedMapStyle) {
        FillLayer countryPolygonFillLayer = new FillLayer("polygon", GEOJSON_SOURCE_ID);
        countryPolygonFillLayer.setProperties(
                PropertyFactory.fillColor(Color.BLUE),
                PropertyFactory.fillOpacity(.4f));
        countryPolygonFillLayer.setFilter(eq(literal("$type"), literal("Polygon")));
        loadedMapStyle.addLayer(countryPolygonFillLayer);
    }

    private void addPointsLayer(@NonNull Style loadedMapStyle) {
        // Create and style a CircleLayer that uses the Point Features' coordinates in the GeoJSON data
        CircleLayer individualCirclesLayer = new CircleLayer("points", GEOJSON_SOURCE_ID);
        individualCirclesLayer.setProperties(
                PropertyFactory.circleColor(Color.BLACK),
                PropertyFactory.circleRadius(7f));
        individualCirclesLayer.setFilter(eq(literal("$type"), literal("Point")));
        loadedMapStyle.addLayer(individualCirclesLayer);

//        SymbolLayer symbolLayer = new SymbolLayer("points", GEOJSON_SOURCE_ID);
//        loadedMapStyle.addImage("marker-icon-id1",
//                BitmapFactory.decodeResource(
//                        MapActivity.this.getResources(), R.drawable.bus));
//        symbolLayer.setProperties(
//                PropertyFactory.iconImage("marker-icon-id1"));
//        loadedMapStyle.addLayer(symbolLayer);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}

package com.gantabya.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gantabya1.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS stations (id INTEGER PRIMARY KEY AUTOINCREMENT, stationName VARCHAR(50) NOT NULL, lat DOUBLE NOT NULL, lang DOUBLE NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS route (id INTEGER PRIMARY KEY AUTOINCREMENT, routeName VARCHAR(50))");
        db.execSQL("CREATE TABLE IF NOT EXISTS busService (id INTEGER PRIMARY KEY AUTOINCREMENT, busServiceProvicerName VARCHAR(50) NOT NULL, address VARCHAR(150), contact VARCHAR(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS route_station (id INTEGER PRIMARY KEY AUTOINCREMENT, routeId INTEGER NOT NULL, stationId INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS route_busService (id INTEGER PRIMARY KEY AUTOINCREMENT, routeId INTEGER NOT NULL, busServiceId INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS stations");
        db.execSQL("DROP TABLE IF EXISTS route");
        db.execSQL("DROP TABLE IF EXISTS busService");
        db.execSQL("DROP TABLE IF EXISTS route_station");
        db.execSQL("DROP TABLE IF EXISTS route_busService");
        onCreate(db);
    }

    public Cursor getAllData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+tableName, null);
        return res;
    }

    public Cursor getRoute(String initId, String finalId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT route.routeName, stations.lat, stations.lang FROM stations JOIN route_station ON stations.id = route_station.stationId JOIN route ON route_station.routeId = route.id WHERE stations.stationName LIKE \"%"+initId+"%\" OR stations.stationName LIKE \"%"+finalId+"%\"", null);
        return res;
    }
    public String getRouteName(String initId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT route.routeName, stations.lat, stations.lang FROM stations JOIN route_station ON stations.id = route_station.stationId JOIN route ON route_station.routeId = route.id WHERE stations.stationName LIKE \"%"+initId+"%\"", null);
        while (res.moveToNext()){
            String routeName = res.getString(0);
            return routeName;
        }
        return null;
    }
    public boolean insertData(String s, String lt, String lo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("stationName", s);
        contentValues.put("lat", lt);
        contentValues.put("lang", lo);
        long res = db.insert("stations", null, contentValues);

        if(res == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean insertRoute(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("routeName", "Old Buspark-Bhaktapur");
        long res = db.insert("route", null, contentValues);
        if(res == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean insertRouteAndStation(int routeId, int stationId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("routeId", routeId);
        contentValues.put("stationId", stationId);
        long res = db.insert("route_station", null, contentValues);
        if (res == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor find(String source){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM stations WHERE stationName LIKE \"%"+source+"%\"", null);
    }
    public Cursor findStation(int source){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM stations WHERE id ="+source, null);
    }
    public String findStationName(int source){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM stations WHERE id ="+source, null);
        while (cr.moveToNext()){
            return cr.getString(1);
        }
        return null;
    }
    public int findRouteId(int s){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT routeId FROM route_station WHERE stationId="+s+"", null);
        while (cursor.moveToNext()){
            return cursor.getInt(0);
        }
        return 0;
    }

    public String findRouteName(int s){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT routeName FROM route JOIN route_station ON route.id = route_station.routeId WHERE route_station.stationId="+s+"", null);
        while (cursor.moveToNext()){
            return cursor.getString(0);
        }
        return null;
    }


    public Cursor getAllDataWithRoute(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM stations JOIN route_station ON stations.id=route_station.stationId JOIN route ON route_station.routeId = route.id", null);
    }
    public double getLat(int source){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT lat FROM stations WHERE id="+source, null);
        while (c.moveToNext()){
            return c.getDouble(0);
        }
        return 0;
    }
    public double getLang(int source){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT lang FROM stations WHERE id="+source, null);
        while (c.moveToNext()){
            return c.getDouble(0);
        }
        return 0;
    }

}

package com.gantabya.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class externalDB extends SQLiteOpenHelper{

    private static String DB_PATH = "";
    private static String DB_NAME = "gantabya.db";
    private SQLiteDatabase mDatabase;
    private Context mContext = null;

    public externalDB(Context context){
        super(context, DB_NAME, null, 1);
        if (Build.VERSION.SDK_INT > 17)
            DB_PATH = context.getApplicationInfo().dataDir+"/";

        mContext = context;


    }

    private boolean checkDB(){
        SQLiteDatabase tempDB = null;
        try{
            String path = DB_PATH+DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (Exception e){}
        if (tempDB != null)
            tempDB.close();
        return tempDB != null?true:false;

    }

    public void copyDatabase(){
        try {
            InputStream mInput = mContext.getAssets().open(DB_NAME);
            String outputFile  = DB_PATH+DB_NAME;
            OutputStream mOutput = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInput.read(buffer)) > 0){
                mOutput.write(buffer, 0, length);
            }

            mOutput.flush();
            mOutput.close();
            mInput.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDatabase(){
        try {
            String path = DB_PATH+DB_NAME;
            mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        } catch ( Exception e){}

    }

    public void createDB(){

        boolean isDBExist = checkDB();
        if (isDBExist){

        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch ( Exception e){}

        }
    }

    public Cursor getAllStation(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        try {

            cursor = db.rawQuery("SELECT * FROM stations", null);
            if (cursor == null) return null;
            else return cursor;


        } catch (Exception e){}
        return null;
    }
    public synchronized void close(){
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

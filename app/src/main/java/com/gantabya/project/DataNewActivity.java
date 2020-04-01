package com.gantabya.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DataNewActivity extends AppCompatActivity{
        DatabaseHelper myDB;
        int sourceId, destId;
        int[] a = new int[50];
        TextView tv;
        Button btn;
        double pLat, pLang, gLat, gLang;
        String testS = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_new);
        myDB = new DatabaseHelper(this);
        Intent intent = getIntent();
        String start = intent.getStringExtra("start");
        String finish = intent.getStringExtra("end");
        String route = intent.getStringExtra("route");
        pLat = intent.getDoubleExtra("dLat", 0);
        pLang = intent.getDoubleExtra("dLang", 0);
        gLat = intent.getDoubleExtra("gLat", 0);
        gLang = intent.getDoubleExtra("gLang", 0);
        //Toast.makeText(DataNewActivity.this, gLat+" "+gLang+" "+pLat+" "+pLang, Toast.LENGTH_LONG).show();
        tv = findViewById(R.id.textView2);
        btn = findViewById(R.id.button);
        Cursor s = myDB.find(start);
        Cursor d = myDB.find(finish);
        while (s.moveToNext()){
            sourceId = s.getInt(0);
        }
        while (d.moveToNext()){
            destId  = d.getInt(0);
        }
       // destId = 19;

        if (destId > sourceId){
            a[1] = destId;
            int i = destId-1;
            int j = 2;
            while (i != sourceId){
                a[j] = i;
                j++;
                i--;
            }
            a[j] = sourceId;

        } else {
            a[1] = destId;
            int i = destId+1;
            int j = 2;
            while (i != sourceId){
                a[j] = i;
                j++;
                i++;
            }
            a[j] = sourceId;

        }
        testS = testS + "From "+ start+" to "+finish+" via "+ route;
        tv.setText(testS);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });


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

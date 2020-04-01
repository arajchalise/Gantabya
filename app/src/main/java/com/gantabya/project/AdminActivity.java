package com.gantabya.project;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    TextInputEditText stat, lat, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        myDB = new DatabaseHelper(this);
        Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }
    public void addData(){
        stat = findViewById(R.id.textStation);
        lat = findViewById(R.id.textLat);
        lang = findViewById(R.id.textLang);
        String s = stat.getText().toString();
        String lt = lat.getText().toString();
        String ln = lang.getText().toString();
        boolean isInserted = myDB.insertData(s, lt, ln);
        if(isInserted){
                Toast.makeText(AdminActivity.this, "Ok", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(AdminActivity.this, "Ok", Toast.LENGTH_LONG).show();
        }
    }
}
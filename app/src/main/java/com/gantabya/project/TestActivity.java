package com.gantabya.project;

import android.os.Bundle;
import android.app.Activity;

public class TestActivity extends Activity {

    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

}

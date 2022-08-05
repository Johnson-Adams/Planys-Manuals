package com.awesome.adams.planysmanuals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Custom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        getSupportActionBar().setTitle("CUSTOMS");
    }
}

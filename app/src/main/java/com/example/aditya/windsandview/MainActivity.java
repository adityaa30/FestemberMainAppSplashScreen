package com.example.aditya.windsandview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindSandView windSandView = findViewById(R.id.windsand);
        Thread t = new Thread(windSandView);
        t.start();
    }
}

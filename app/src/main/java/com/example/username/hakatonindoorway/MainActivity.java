package com.example.username.hakatonindoorway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.indoorway.android.common.sdk.IndoorwaySdk;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndoorwaySdk.initContext(this);
        IndoorwaySdk.configure("babd13d7-abf9-4151-a965-3bffa2e0679b");

        startActivity(new Intent(this, MapActivity.class));
    }
}

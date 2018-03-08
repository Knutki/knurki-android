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
        IndoorwaySdk.configure("65c78b42-8d25-445b-9a22-e51d969fca09");

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_ROOM_NUMBER, "kuba");
        startActivity(intent);
        finish();
    }
}

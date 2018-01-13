package com.example.username.hakatonindoorway;

import android.app.Activity;
import android.os.Bundle;

import com.indoorway.android.common.sdk.IndoorwaySdk;
import com.indoorway.android.map.sdk.view.IndoorwayMapView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndoorwaySdk.initContext(this);
        IndoorwaySdk.configure("babd13d7-abf9-4151-a965-3bffa2e0679b");

        IndoorwayMapView indoorwayMapView = findViewById(R.id.mapView);
        indoorwayMapView.load("CScrSxCVhQg", "3-_M01M3r5w");
    }
}

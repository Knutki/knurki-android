package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.indoorway.android.fragments.sdk.map.IndoorwayMapFragment;
import com.indoorway.android.fragments.sdk.map.MapFragment;

public class MapActivity extends AppCompatActivity implements IndoorwayMapFragment.OnMapFragmentReadyListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        IndoorwayMapFragment.Config config = new IndoorwayMapFragment.Config();

        config.setLocationButtonVisible(true);
        config.setStartPositioningOnResume(false);
        config.setLoadLastKnownMap(false);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        IndoorwayMapFragment fragment = IndoorwayMapFragment.newInstance(this, config);
        fragmentTransaction.add(R.id.mapFragment, fragment, IndoorwayMapFragment.class.getSimpleName());
        fragmentTransaction.commit();


    }

    @Override
    public void onMapFragmentReady(MapFragment mapFragment) {
        mapFragment.getMapView().load("CScrSxCVhQg", "3-_M01M3r5w");
    }
}

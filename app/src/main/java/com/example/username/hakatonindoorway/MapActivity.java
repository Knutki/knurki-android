package com.example.username.hakatonindoorway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.username.hakatonindoorway.Navigation.BuildingManager;
import com.example.username.hakatonindoorway.Navigation.BuildingObject;
import com.example.username.hakatonindoorway.Navigation.LocationListener;
import com.example.username.hakatonindoorway.Navigation.NavigatorManager;
import com.indoorway.android.fragments.sdk.map.IndoorwayMapFragment;
import com.indoorway.android.fragments.sdk.map.MapFragment;
import com.indoorway.android.location.sdk.IndoorwayLocationSdk;


public class MapActivity extends AppCompatActivity implements IndoorwayMapFragment.OnMapFragmentReadyListener{
    public static final String EXTRA_ROOM_NUMBER = "ROOM_NUMBER";

    public LocationListener locationListener;
    public BuildingManager buildingManager;
    public NavigatorManager navigatorManager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        IndoorwayMapFragment.Config config = new IndoorwayMapFragment.Config();

        config.setLocationButtonVisible(true);
        config.setStartPositioningOnResume(false);
        config.setLoadLastKnownMap(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        IndoorwayMapFragment fragment = IndoorwayMapFragment.newInstance(this, config);
        fragmentTransaction.add(R.id.mapFragment, fragment, IndoorwayMapFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onMapFragmentReady(MapFragment mapFragment) {
        buildingManager = new BuildingManager(this);

        locationListener = new LocationListener(mapFragment.getMapView(), this);
        navigatorManager = new NavigatorManager(locationListener, buildingManager);
        IndoorwayLocationSdk.instance()
            .position()
            .onChange()
            .register(locationListener);
    }

    public void onLocationReady(){
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        String room = "107";
        if(getIntent() != null && getIntent().hasExtra(EXTRA_ROOM_NUMBER))
            room = getIntent().getStringExtra(EXTRA_ROOM_NUMBER);
        navigatorManager.navigateTo(room, BuildingObject.ROOM);
    }

    public void onShowCoursesClick(View view) {
        startActivity(new Intent(this, PlanActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up);
    }

    public void onMapChanged() {
        navigatorManager.onMapChanged();
    }

    public void onLocationUpdate() {
        navigatorManager.onLocationUpdate();
    }
}

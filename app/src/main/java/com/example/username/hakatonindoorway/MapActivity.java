package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.username.hakatonindoorway.Navigation.BuildingManager;
import com.example.username.hakatonindoorway.Navigation.BuildingObject;
import com.example.username.hakatonindoorway.Navigation.LocationListener;
import com.example.username.hakatonindoorway.Navigation.NavigatorManager;
import com.indoorway.android.fragments.sdk.map.IndoorwayMapFragment;
import com.indoorway.android.fragments.sdk.map.MapFragment;
import com.indoorway.android.location.sdk.IndoorwayLocationSdk;


public class MapActivity extends AppCompatActivity implements IndoorwayMapFragment.OnMapFragmentReadyListener{
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
        navigatorManager.navigateTo("107", BuildingObject.ROOM);
    }
}

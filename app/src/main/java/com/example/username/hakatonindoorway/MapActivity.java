package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.username.hakatonindoorway.Navigation.BuildingManager;
import com.example.username.hakatonindoorway.Navigation.BuildingObject;
import com.example.username.hakatonindoorway.Navigation.LocationListener;
import com.example.username.hakatonindoorway.Navigation.NavigatorManager;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.Coordinates;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;
import com.indoorway.android.fragments.sdk.map.IndoorwayMapFragment;
import com.indoorway.android.fragments.sdk.map.MapFragment;
import com.indoorway.android.location.sdk.IndoorwayLocationSdk;
import com.indoorway.android.map.sdk.view.MapView;


public class MapActivity extends AppCompatActivity implements IndoorwayMapFragment.OnMapFragmentReadyListener{
    public static final String EXTRA_ROOM_NUMBER = "ROOM_NUMBER";

    public LocationListener locationListener;
    public BuildingManager buildingManager;
    public NavigatorManager navigatorManager;

    public MapView mapView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        IndoorwayMapFragment.Config config = new IndoorwayMapFragment.Config();

        config.setLocationButtonVisible(false);
        config.setStartPositioningOnResume(true);
        config.setLoadLastKnownMap(false);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        IndoorwayMapFragment fragment = IndoorwayMapFragment.newInstance(this, config);
        fragmentTransaction.add(R.id.mapFragment, fragment, IndoorwayMapFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onMapFragmentReady(MapFragment mapFragment) {
        buildingManager = new BuildingManager(this);

        mapView = mapFragment.getMapView();

        locationListener = new LocationListener(this);
        navigatorManager = new NavigatorManager(locationListener, buildingManager);
        IndoorwayLocationSdk.instance()
            .position()
            .onChange()
            .register(locationListener);
    }

    public void onLocationReady(String buildingUuid, String mapUuid){
        this.mapView.setOnMapLoadCompletedListener(new Action1<IndoorwayMap>() {
            @Override
            public void onAction(IndoorwayMap indoorwayMap) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                if(getIntent() != null && getIntent().hasExtra(EXTRA_ROOM_NUMBER)) {
                    String room = getIntent().getStringExtra(EXTRA_ROOM_NUMBER);
                    navigatorManager.navigateTo(room, BuildingObject.ROOM, indoorwayMap);
                }
                mapView.setOnMapLoadCompletedListener(null);
            }
        });
        this.mapView.load(buildingUuid, mapUuid);
    }

    public void onMapChanged(String buildingUuid, String mapUuid) {
        if (this.mapView != null ){
            this.mapView.getNavigation().stop();
        }
        this.mapView.setOnMapLoadCompletedListener(new Action1<IndoorwayMap>() {
            @Override
            public void onAction(IndoorwayMap indoorwayMap) {
                navigatorManager.onMapChanged(indoorwayMap);
                mapView.setOnMapLoadCompletedListener(null);
            }
        });
        this.mapView.load(buildingUuid, mapUuid);

    }

    public void onLocationUpdate(IndoorwayPosition position) {
        mapView.getPosition().setPosition(position, true);
        navigatorManager.onLocationUpdate();
    }

    public void startNavigation(Coordinates start, Coordinates end){
        mapView.getNavigation().start(start, end);
    }

    @Override
    protected void onStop() {
        super.onStop();
        IndoorwayLocationSdk.instance()
                .position()
                .onChange()
                .unregister(locationListener);

        navigatorManager.onStop();
    }
}

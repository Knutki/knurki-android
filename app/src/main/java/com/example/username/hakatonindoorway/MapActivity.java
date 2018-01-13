package com.example.username.hakatonindoorway;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.username.hakatonindoorway.Navigation.LocationListener;
import com.indoorway.android.common.sdk.IndoorwaySdk;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.task.IndoorwayTask;
import com.indoorway.android.fragments.sdk.map.IndoorwayMapFragment;
import com.indoorway.android.fragments.sdk.map.MapFragment;
import com.indoorway.android.location.sdk.IndoorwayLocationSdk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements IndoorwayMapFragment.OnMapFragmentReadyListener{
    public LocationListener locationListener;


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
        mapFragment.getMapView().load(getString(R.string.building_id), getString(R.string.floor_2_id));
        mapFragment.getMapView().load(getString(R.string.building_id), getString(R.string.floor_1_id));
        mapFragment.getMapView().load(getString(R.string.building_id), getString(R.string.floor_0_id));

        locationListener = new LocationListener(mapFragment.getMapView(), this);
        IndoorwayLocationSdk.instance()
                .position()
                .onChange()
                .register(locationListener);
    }

    public void navigateToRoom(Integer room_number){
        final String[] names =  new String[]{"Room " + room_number, "ROOM " + room_number};
        IndoorwaySdk.instance()
            .map()
            .details(getString(R.string.building_id), floorId(2))
            .setOnCompletedListener(new Action1<IndoorwayMap>() {
                @Override
                public void onAction(IndoorwayMap indoorwayMap) {
                    List<IndoorwayObjectParameters> rooms = indoorwayMap.objectsWithType("room");
                    for(IndoorwayObjectParameters room:rooms){
                        if (room.getName().contains(names[0]) || room.getName().contains(names[1])){
                            locationListener.mapView.getNavigation().start(
                                locationListener.getLastKnownPosition(),
                                room.getCenterPoint()
                            );
                        }
                    }
                }
            })
            .setOnFailedListener(new Action1<IndoorwayTask.ProcessingException>() {
                @Override
                public void onAction(IndoorwayTask.ProcessingException e) {
                    // handle error, original exception is given on e.getCause()
                }
            })
            .execute();
    }

    private String floorId(Integer floorId){
        Map<Integer, String> floors = new HashMap<>();
        floors.put(0, getString(R.string.floor_0_id));
        floors.put(1, getString(R.string.floor_1_id));
        floors.put(2, getString(R.string.floor_2_id));
        return floors.get(floorId);
    }
}

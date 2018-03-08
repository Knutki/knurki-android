package com.example.username.hakatonindoorway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.username.hakatonindoorway.Navigation.BuildingManager;

import com.example.username.hakatonindoorway.Navigation.BuildingObject;
import com.example.username.hakatonindoorway.Navigation.LocationListener;
import com.example.username.hakatonindoorway.Navigation.NavigatorManager;
import com.indoorway.android.common.sdk.listeners.generic.Action0;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.Coordinates;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;
import com.indoorway.android.fragments.sdk.map.IndoorwayMapFragment;
import com.indoorway.android.fragments.sdk.map.MapFragment;
import com.indoorway.android.location.sdk.IndoorwayLocationSdk;
import com.indoorway.android.map.sdk.view.MapView;

import java.util.List;


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
        mapView = mapFragment.getMapView();
        buildingManager = new BuildingManager();
        locationListener = new LocationListener(this);
        navigatorManager = new NavigatorManager(locationListener, buildingManager);
        IndoorwayLocationSdk.instance()
            .position()
            .onChange()
            .register(locationListener);
    }

    public void onLocationReady(final String buildingUuid, String mapUuid){
        buildingManager.setOnMapReady(new Action0(){
            @Override
            public void onAction() {
                final List<IndoorwayObjectParameters> objects = buildingManager.findObjects(BuildingObject.ROOM);
                new AlertDialog.Builder(MapActivity.this).setAdapter(
                        new ArrayAdapter<IndoorwayObjectParameters>(MapActivity.this, android.R.layout.simple_list_item_1, objects) {
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                IndoorwayObjectParameters item = getItem(position);
                                if (convertView == null) {
                                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                                }
                                ((TextView)convertView).setText(item.getName());
                                return convertView;
                            }
                        },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigatorManager.navigateTo(objects.get(i));
                    }
                }
                ).create().show();
            }
        });
        buildingManager.loadMap(buildingUuid, mapUuid);

        this.mapView.setOnMapLoadCompletedListener(new Action1<IndoorwayMap>() {
            @Override
            public void onAction(final IndoorwayMap indoorwayMap) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
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
                buildingManager.onMapChanged(indoorwayMap);
                navigatorManager.onMapChanged();
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

package com.example.username.hakatonindoorway.Navigation;

import com.example.username.hakatonindoorway.MapActivity;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;
import com.indoorway.android.map.sdk.view.MapView;

public class LocationListener implements Action1<IndoorwayPosition> {
    private IndoorwayPosition currentPosition;
    public MapView mapView;
    public MapActivity mapActivity;

    public LocationListener(MapView mapView, MapActivity mapActivity){
        super();
        this.mapView = mapView;
        this.mapActivity = mapActivity;
    }

    @Override
    public void onAction(IndoorwayPosition position) {
        // store last position as a field
        if (currentPosition == null && position != null){
            mapActivity.navigateToRoom(214);
        }
        currentPosition = position;


        // react for position changes...

        // If you are using map view, you can pass position.
        // Second argument indicates if you want to auto reload map on position change
        // for eg. after going to different building level.
        mapView.getPosition().setPosition(position, true);
    }
    public IndoorwayPosition getLastKnownPosition(){
        return currentPosition;
    }
}
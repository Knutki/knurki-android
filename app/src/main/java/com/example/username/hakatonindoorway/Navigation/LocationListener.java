package com.example.username.hakatonindoorway.Navigation;

import com.example.username.hakatonindoorway.MapActivity;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;

public class LocationListener implements Action1<IndoorwayPosition> {
    private IndoorwayPosition currentPosition;
    public MapActivity mapActivity;

    private String buildingUuid;
    private String mapUuid;

    public LocationListener(MapActivity mapActivity){
        super();
        this.mapActivity = mapActivity;
    }

    @Override
    public void onAction(IndoorwayPosition position) {
        // store last position as a field
        if (currentPosition == null){
            currentPosition = position;
            buildingUuid = position.getBuildingUuid();
            mapUuid = position.getMapUuid();
            mapActivity.onLocationReady(buildingUuid, mapUuid);
        }
        currentPosition = position;

        if (!position.getMapUuid().equals(mapUuid)) {
            buildingUuid = position.getBuildingUuid();
            mapUuid = position.getMapUuid();
            mapActivity.onMapChanged(buildingUuid, mapUuid);
        }

        mapActivity.onLocationUpdate(position);
    }

    public IndoorwayPosition getLastKnownPosition(){
        return currentPosition;
    }
}
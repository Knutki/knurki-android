package com.example.username.hakatonindoorway.Navigation;

import com.indoorway.android.common.sdk.model.Coordinates;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;

import java.util.List;


public class NavigatorManager {
    private LocationListener locationListener;
    private BuildingManager buildingManager;

    public NavigatorManager(LocationListener locationListener, BuildingManager buildingManager){
        this.locationListener = locationListener;
        this.buildingManager = buildingManager;
    }

    public void navigateTo(String name, BuildingObject type){
        IndoorwayObjectParameters object = buildingManager.findObject(name, type);
        IndoorwayPosition currentLocation = locationListener.getLastKnownPosition();
        IndoorwayMap objectMap = buildingManager.findMap(name, type);

        Integer currentLocationFloor = buildingManager.floorNumber(currentLocation.getMapUuid());
        Integer objectLocationFloor = buildingManager.floorNumber(objectMap.getMapUuid());

        if (currentLocationFloor.equals(objectLocationFloor))
            navigateToOnSameFloor(currentLocation.getCoordinates(), object.getCenterPoint());
        else {
            List<IndoorwayObjectParameters> stairs = buildingManager.findObjectsOnFloor(
                    BuildingObject.STAIRS, currentLocationFloor
            );
            navigateToOnSameFloor(currentLocation.getCoordinates(), stairs.get(0).getCenterPoint());
        }
    }

    private void navigateToOnSameFloor(Coordinates start, Coordinates stop){
        locationListener.mapView.getNavigation().start(start, stop);
    }
}

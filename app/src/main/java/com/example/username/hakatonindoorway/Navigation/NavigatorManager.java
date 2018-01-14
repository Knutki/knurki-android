package com.example.username.hakatonindoorway.Navigation;

import android.widget.Toast;

import com.example.username.hakatonindoorway.Navigation.algorithms.Dijkstra;
import com.example.username.hakatonindoorway.Navigation.algorithms.Turning;
import com.indoorway.android.common.sdk.model.Coordinates;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayNode;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class DistanceManipulator{
    static IndoorwayNode closestNode(List<IndoorwayNode> nodes, Coordinates closestTo){
        IndoorwayNode closestNode = nodes.get(0);
        for (IndoorwayNode node: nodes){
            if (
                closestNode.getCoordinates().getDistanceTo(closestTo) >
                node.getCoordinates().getDistanceTo(closestTo)
            )
                closestNode = node;
        }
        return closestNode;
    }
}


public class NavigatorManager {
    private final double ON_NODE_MARGIN = 4.5;

    private LocationListener locationListener;
    private BuildingManager buildingManager;

    private IndoorwayObjectParameters currentlyNavigatingTo;
    private List<Turning.Turn> currentTurnings;
    private Integer currentLocationFloor;
    private Integer destinationLocationFloor;


    public NavigatorManager(LocationListener locationListener, BuildingManager buildingManager){
        this.locationListener = locationListener;
        this.buildingManager = buildingManager;
    }

    public void navigateTo(String name, BuildingObject type){
        currentlyNavigatingTo = buildingManager.findObject(name, type);
        navigateToObject(currentlyNavigatingTo);
    }

    private void navigateToObject(IndoorwayObjectParameters destinationObject){
        IndoorwayPosition currentLocation = locationListener.getLastKnownPosition();
        IndoorwayMap destinationMap = buildingManager.findMap(destinationObject);

        currentLocationFloor = buildingManager.floorNumber(currentLocation.getMapUuid());
        destinationLocationFloor = buildingManager.floorNumber(destinationMap.getMapUuid());

        if (currentLocationFloor.equals(destinationLocationFloor))
            navigateToOnSameFloor(currentLocation.getCoordinates(), destinationObject.getCenterPoint());
        else {
            List<IndoorwayObjectParameters> stairs = buildingManager.findObjectsOnFloor(
                    BuildingObject.ELEVATOR, currentLocationFloor
            );
            navigateToOnSameFloor(currentLocation.getCoordinates(), stairs.get(0).getCenterPoint());
        }
    }

    private void navigateToOnSameFloor(Coordinates start, Coordinates end){
        locationListener.mapView.getNavigation().start(start, end);
        List<IndoorwayNode> paths = locationListener.mapView.currentMap().getPaths();

        Map<Long, IndoorwayNode> nodesMap = new HashMap<>();
        for (IndoorwayNode node: paths){
            nodesMap.put(node.getId(), node);
        }

        List<IndoorwayNode> shortestPath = Dijkstra.getShortestPath(
            DistanceManipulator.closestNode(paths, start),
            DistanceManipulator.closestNode(paths, end),
            nodesMap
        );
        currentTurnings = Turning.getTurnings(shortestPath);
    }

    public void onMapChanged() {
        if (currentlyNavigatingTo != null){
            navigateToObject(currentlyNavigatingTo);
        }
    }

    public void onLocationUpdate() {
        if (currentlyNavigatingTo == null){
            return;
        }

        IndoorwayPosition lastKnownPosition = locationListener.getLastKnownPosition();
        if (currentTurnings.size() == 0){
            if (lastKnownPosition.getCoordinates().getDistanceTo(currentlyNavigatingTo.getCenterPoint()) < ON_NODE_MARGIN){
                Toast.makeText(locationListener.mapActivity, "Jesteś u celu", Toast.LENGTH_LONG).show();
                locationListener.mapActivity.finish();
            }
            else{
                Toast.makeText(locationListener.mapActivity, "Pojedź windą na " + destinationLocationFloor, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Turning.Turn turn = currentTurnings.get(0);
            if (turn.getNode().getCoordinates().getDistanceTo(lastKnownPosition.getCoordinates()) < ON_NODE_MARGIN){
                currentTurnings.remove(turn);
                Toast.makeText(locationListener.mapActivity, "Skrec w " + turn.turn(), Toast.LENGTH_LONG).show();
            }
        }
    }
}

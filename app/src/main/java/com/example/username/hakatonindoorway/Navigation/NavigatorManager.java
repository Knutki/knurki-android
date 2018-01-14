package com.example.username.hakatonindoorway.Navigation;

import android.speech.tts.TextToSpeech;
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
import java.util.Locale;
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

    private TextToSpeech textToSpeech;
    private Boolean elevatorInfo = false;


    public NavigatorManager(LocationListener locationListener, BuildingManager buildingManager){
        this.locationListener = locationListener;
        this.buildingManager = buildingManager;

        textToSpeech = new TextToSpeech(locationListener.mapActivity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.forLanguageTag("PL"));
            }
        });
    }

    public void navigateTo(String name, BuildingObject type, IndoorwayMap currentMap){
        currentlyNavigatingTo = buildingManager.findObject(name, type);
        navigateToObject(currentlyNavigatingTo, currentMap);
    }

    private void navigateToObject(IndoorwayObjectParameters destinationObject, IndoorwayMap currentMap){
        IndoorwayPosition currentLocation = locationListener.getLastKnownPosition();
        IndoorwayMap destinationMap = buildingManager.findMap(destinationObject);

        currentLocationFloor = buildingManager.floorNumber(currentLocation.getMapUuid());
        destinationLocationFloor = buildingManager.floorNumber(destinationMap.getMapUuid());

        if (currentLocationFloor.equals(destinationLocationFloor))
            navigateToOnSameFloor(currentLocation.getCoordinates(), destinationObject.getCenterPoint(), currentMap);
        else {
            List<IndoorwayObjectParameters> elevator = buildingManager.findObjectsOnFloor(
                    BuildingObject.ELEVATOR, currentLocationFloor
            );
            navigateToOnSameFloor(
                    currentLocation.getCoordinates(), elevator.get(0).getCenterPoint(),
                    currentMap
            );
        }
    }

    private void navigateToOnSameFloor(Coordinates start, Coordinates end, IndoorwayMap currentMap){
        locationListener.mapActivity.startNavigation(start, end);
        List<IndoorwayNode> paths = currentMap.getPaths();

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

    public void onMapChanged(IndoorwayMap currentMap) {
        if (currentlyNavigatingTo != null){
            navigateToObject(currentlyNavigatingTo, currentMap);
        }
    }

    public void onLocationUpdate() {
        if (currentlyNavigatingTo == null){
            return;
        }



        IndoorwayPosition lastKnownPosition = locationListener.getLastKnownPosition();
        if (currentTurnings.size() == 0){
            if (lastKnownPosition.getCoordinates().getDistanceTo(currentlyNavigatingTo.getCenterPoint()) < ON_NODE_MARGIN){
                textToSpeech.speak(
                        "Jesteś u celu",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "destination"
                );
                locationListener.mapActivity.finish();
            }
            else if (!elevatorInfo){
                textToSpeech.speak(
                        "Pojedź windą na " + destinationLocationFloor,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "destination"
                );
                elevatorInfo = true;
            }
        }
        else {
            Turning.Turn turn = currentTurnings.get(0);
            if (turn.getNode().getCoordinates().getDistanceTo(lastKnownPosition.getCoordinates()) < ON_NODE_MARGIN){
                currentTurnings.remove(turn);
                textToSpeech.speak(
                        "Skręć w " + turn.turnInPolish(),
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "destination"
                );
            }
        }
    }
}

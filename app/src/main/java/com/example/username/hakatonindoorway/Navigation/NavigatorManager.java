package com.example.username.hakatonindoorway.Navigation;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

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
    private IndoorwayObjectParameters currentElevator;


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

    public void navigateTo(IndoorwayObjectParameters destinationObject){
        textToSpeech.speak(
                "Ruszajmy",
                TextToSpeech.QUEUE_FLUSH,
                null,
                "navigation_start"
        );
        currentlyNavigatingTo = destinationObject;
        navigateToObject(destinationObject);
    }

    public void navigateToObject(IndoorwayObjectParameters destinationObject){

        IndoorwayPosition currentLocation = locationListener.getLastKnownPosition();
        IndoorwayMap destinationMap = buildingManager.getMap(destinationObject);
        IndoorwayMap currentMap = buildingManager.getCurrentMap();

        currentLocationFloor = buildingManager.getFloorNumber(currentMap);
        destinationLocationFloor = buildingManager.getFloorNumber(destinationMap);

        if (currentLocationFloor.equals(destinationLocationFloor))
            navigateToOnSameFloor(currentLocation.getCoordinates(), destinationObject.getCenterPoint(), currentMap);
        else {
            List<IndoorwayObjectParameters> elevator = buildingManager.findObjects(
                BuildingObject.ELEVATOR
            );
            currentElevator = elevator.get(0);
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
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {}

                    @Override
                    public void onDone(String s) {
                        locationListener.mapActivity.finish();
                    }

                    @Override
                    public void onError(String s) {}
                });
                textToSpeech.speak(
                        "Jesteś u celu",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "destination"
                );
            }
            else if (!elevatorInfo && lastKnownPosition.getCoordinates().getDistanceTo(currentElevator.getCenterPoint()) < ON_NODE_MARGIN){
                textToSpeech.speak(
                        "Pojedź windą na piętro " + destinationLocationFloor,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "elevator"
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
                        "turn"
                );
            }
        }
    }

    public void onStop(){
        textToSpeech.shutdown();
    }
}

package com.example.username.hakatonindoorway.Navigation;

import com.amazonaws.transform.MapEntry;
import com.indoorway.android.common.sdk.IndoorwaySdk;
import com.indoorway.android.common.sdk.listeners.generic.Action0;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.task.IndoorwayTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildingManager {

    private Map<Integer, IndoorwayMap> floorMap = new HashMap<>();
    private IndoorwayMap currentMap;
    private Action0 onLoadMapAction;

    public BuildingManager(){}

    public IndoorwayMap getCurrentMap(){
        return currentMap;
    }

    public List<IndoorwayObjectParameters> findObjects(BuildingObject type){
        return getCurrentMap().objectsWithType(type.getName());
    }

    public void loadMap(String buildingUuid, String mapUuid){
        IndoorwaySdk.instance()
            .map()
            .details(buildingUuid, mapUuid)
            .setOnCompletedListener(new Action1<IndoorwayMap>() {
                @Override
                public void onAction(IndoorwayMap indoorwayMap) {
                    Matcher matcher = Pattern.compile("FLOOR:(\\d+)").matcher(indoorwayMap.getMapName());
                    matcher.find();
                    Integer floor = Integer.valueOf(matcher.group(1));
                    floorMap.put(floor, indoorwayMap);
                    currentMap = indoorwayMap;
                    onLoadMapAction.onAction();
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

    public void setOnMapReady(Action0 action){
        onLoadMapAction = action;
    }

    public Integer getFloorNumber(IndoorwayMap indoorwayMap){
        for (Map.Entry<Integer, IndoorwayMap> item: floorMap.entrySet())
            if (item.getValue() == indoorwayMap) return item.getKey();
        return 0;
    }

    public IndoorwayMap getMap(IndoorwayObjectParameters navigateToObject){
        for (IndoorwayMap map: floorMap.values()){
            IndoorwayObjectParameters foundObject = map.objectWithId(navigateToObject.getId());
            if (foundObject != null) return map;
        }
        return null;
    }

    public void onMapChanged(IndoorwayMap indoorwayMap) {
        currentMap = indoorwayMap;
    }
}

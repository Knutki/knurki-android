package com.example.username.hakatonindoorway.Navigation;

import android.content.Context;

import com.example.username.hakatonindoorway.R;
import com.indoorway.android.common.sdk.IndoorwaySdk;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.task.IndoorwayTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingManager {


    private Context context;
    private Map<Integer, IndoorwayMap> floorMap = new HashMap<>();
    private Integer floorNumber = 3;

    public BuildingManager(Context context){
        this.context = context;
        for (int floor=0; floor < floorNumber; floor++){
            loadMap(floor);
        }
    }

    public IndoorwayObjectParameters findObject(String name, BuildingObject type){
        for (int floor=0; floor < floorNumber; floor++){
            IndoorwayObjectParameters foundObject = findObjectOnFloor(name, type, floor);
            if (foundObject != null){return foundObject;}
        }
        return null;
    }

    public IndoorwayMap findMap(String name, BuildingObject type){
        for (int floor=0; floor < floorNumber; floor++){
            IndoorwayObjectParameters foundObject = findObjectOnFloor(name, type, floor);
            if (foundObject != null){return floorMap.get(floor);}
        }
        return null;
    }

    public IndoorwayObjectParameters findObjectOnFloor(String name, BuildingObject type, Integer floor){
        IndoorwayMap map = floorMap.get(floor);
        List<IndoorwayObjectParameters> objects = map.objectsWithType(type.getName());
        for (IndoorwayObjectParameters object: objects){
            if (object.getName().contains(name))
                return object;
        }
        return null;
    }

    public List<IndoorwayObjectParameters> findObjectsOnFloor(BuildingObject type, Integer floor){
        IndoorwayMap map = floorMap.get(floor);
        return map.objectsWithType(type.getName());
    }

    private void loadMap(final Integer floor){
        IndoorwaySdk.instance()
            .map()
            .details(context.getString(R.string.building_id), floorId(floor))
            .setOnCompletedListener(new Action1<IndoorwayMap>() {
                @Override
                public void onAction(IndoorwayMap indoorwayMap) {
                    floorMap.put(floor, indoorwayMap);
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

    // For hackathon only, use building api instead
    public String floorId(Integer floorNumber){
        Map<Integer, String> floors = new HashMap<>();
        floors.put(0, context.getString(R.string.floor_0_id));
        floors.put(1, context.getString(R.string.floor_1_id));
        floors.put(2, context.getString(R.string.floor_2_id));
        return floors.get(floorNumber);
    }

    // For hackathon only, use building api instead
    public Integer floorNumber(String floorId){
        Map<String, Integer> floors = new HashMap<>();
        floors.put(context.getString(R.string.floor_0_id), 0);
        floors.put(context.getString(R.string.floor_1_id), 1);
        floors.put(context.getString(R.string.floor_2_id), 2);
        return floors.get(floorId);
    }
}

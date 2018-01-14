package com.example.username.hakatonindoorway.Navigation.algorithms;

import android.support.annotation.NonNull;

import com.indoorway.android.common.sdk.model.IndoorwayNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class Dijkstra {
    private static class State implements Comparable<State> {
        private IndoorwayNode node;
        private double distance;
        private State predecessor;

        public State(IndoorwayNode node, double distance, State predecessor) {
            this.node = node;
            this.distance = distance;
            this.predecessor = predecessor;
        }

        public IndoorwayNode getIndoorwayNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }

        public State getPredecessor() {
            return predecessor;
        }

        @Override
        public int compareTo(@NonNull State state) {
            return Double.valueOf(this.distance).compareTo(state.distance);
        }
    }

    private static double getDistance(IndoorwayNode start, IndoorwayNode end) {
        return start.getCoordinates().getDistanceTo(end.getCoordinates());
    }

    public static List<IndoorwayNode> getShortestPath(IndoorwayNode start, IndoorwayNode end, Map<Long, IndoorwayNode> nodes) {
        List<IndoorwayNode> path = new LinkedList<>();
        Map<Long, State> predecessors = new HashMap<>();
        PriorityQueue<State> heap = new PriorityQueue<>();
        heap.add(new State(start, 0.0, null));
        while(!heap.isEmpty() && !predecessors.containsKey(end.getId())) {
            State currentState = heap.poll();
            if(predecessors.containsKey(currentState.getIndoorwayNode().getId()))
                continue;
            predecessors.put(currentState.getIndoorwayNode().getId(), currentState);
            for(Long _neighbour: currentState.getIndoorwayNode().getNeighbours()) {
                IndoorwayNode neighbour = nodes.get(_neighbour);
                heap.add(
                    new State(
                        neighbour,
                        currentState.getDistance() + getDistance(currentState.getIndoorwayNode(), neighbour),
                        currentState
                    )
                );
            }
        }
        if(!predecessors.containsKey(end.getId()))
            throw new IllegalStateException("Cannot find path to end");
        State endState = predecessors.get(end.getId());
        while(endState != null) {
            path.add(0, endState.getIndoorwayNode());
            endState = endState.getPredecessor();
        }
        return path;
    }
}

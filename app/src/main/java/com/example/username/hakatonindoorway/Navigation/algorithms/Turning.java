package com.example.username.hakatonindoorway.Navigation.algorithms;

import com.indoorway.android.common.sdk.model.IndoorwayNode;

import java.util.LinkedList;
import java.util.List;

public class Turning {
    static private final double MIN_TURNING_DEGREE = 20;
    static private final double MAX_TURNING_DEGREE = 360 - MIN_TURNING_DEGREE;
    private enum Side {
        LEFT, RIGHT;
    }
    static public class Turn {
        private Side side;
        private IndoorwayNode node;

        public Turn(Side side, IndoorwayNode node) {
            this.side = side;
            this.node = node;
        }

        public Side getSide() {
            return side;
        }

        public IndoorwayNode getNode() {
            return node;
        }

        public String turn() {
            return this.side.name().toLowerCase();
        }
    }

    static private double getTurnDegree(double firstDegree, double secondDegree) {
        secondDegree -= firstDegree;
        secondDegree += 720;
        while(secondDegree >= 360) {
            secondDegree -= 360;
        }
        return secondDegree;
    }

    static private double getDegree(IndoorwayNode node1, IndoorwayNode node2) {
        return node1.getCoordinates().getAngleTo(node2.getCoordinates());
    }

    static private boolean isTurning(double degree) {
        return degree > MIN_TURNING_DEGREE && degree < MAX_TURNING_DEGREE;
    }

    static private Side getTurning(double degree) {
        return degree > 180 ? Side.LEFT : Side.RIGHT;
    }

    public static List<Turn> getTurnings(List<IndoorwayNode> path) {
        List<Turn> turnings = new LinkedList<>();
        for(int i = 0; i + 2 < path.size(); i++) {
            IndoorwayNode previous = path.get(i);
            IndoorwayNode current = path.get(i+1);
            IndoorwayNode next = path.get(i + 2);

            double turnDegree = getTurnDegree(
                    getDegree(previous, current),
                    getDegree(current, next)
            );

            if(isTurning(turnDegree))
                turnings.add(new Turn(getTurning(turnDegree), current));
        }
        return turnings;
    }
}

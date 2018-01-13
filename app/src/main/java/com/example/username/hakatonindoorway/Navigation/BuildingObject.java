package com.example.username.hakatonindoorway.Navigation;

public enum BuildingObject {
    ROOM, ELEVATOR, STAIRS;

    public String getName() {
        return this.name().toLowerCase();
    }
}

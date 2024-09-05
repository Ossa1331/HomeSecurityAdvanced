package com.example.homesecurity.enums;

public enum Locations {
    BATHROOM("Bathroom"),
    DINING_ROOM("Dining room"),
    COURTYARD("Courtyard"),
    TERRACE("Terrace"),
    HALLWAY("Hallway"),
    KITCHEN("Kitchen"),
    BACKYARD("Backyard"),
    BEDROOM("Bedroom"),
    BASEMENT("Basement"),
    STAIRWAY("Stairway"),
    UTILITY_ROOM("Utility room");


    private final String location;

    Locations(String location){
        this.location=location;
    }

    public String getLocation(){
        return location;
    }
}

package com.mulungushi.lodge.billing;

public enum RoomType {
    STANDARD("Standard", 350.0),
    DELUXE("Deluxe", 550.0),
    FAMILY("Family", 750.0);

    private final String displayName;
    private final double ratePerNight;

    RoomType(String displayName, double ratePerNight) {
        this.displayName = displayName;
        this.ratePerNight = ratePerNight;
    }

    public String displayName() { return displayName; }
    public double ratePerNight() { return ratePerNight; }

    public static RoomType fromDisplayName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Room type is required.");
        }
        for (RoomType type : values()) {
            if (type.displayName.equalsIgnoreCase(name.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown room type: " + name);
    }
}

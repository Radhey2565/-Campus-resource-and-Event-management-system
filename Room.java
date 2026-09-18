package com.campusmanager.model;

/**
 * Represents physical rooms such as Classrooms and Seminar Halls.
 * Demonstrates Inheritance and Method Overriding.
 */
public class Room extends Resource {
    private boolean hasProjector;
    private boolean hasAirConditioning;

    public Room() {
        super();
    }

    public Room(String id, String name, ResourceType type, String location, ResourceStatus status, int capacity,
                boolean hasProjector, boolean hasAirConditioning) {
        super(id, name, type, location, status, capacity);
        this.hasProjector = hasProjector;
        this.hasAirConditioning = hasAirConditioning;
    }

    public boolean isHasProjector() {
        return hasProjector;
    }

    public void setHasProjector(boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public boolean isHasAirConditioning() {
        return hasAirConditioning;
    }

    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Projector: %s, AC: %s",
                hasProjector ? "Yes" : "No",
                hasAirConditioning ? "Yes" : "No");
    }
}

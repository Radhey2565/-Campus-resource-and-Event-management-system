package com.campusmanager.model;

/**
 * Enumeration of resource types available on campus.
 */
public enum ResourceType {
    CLASSROOM("Classroom"),
    COMPUTER_LAB("Computer Lab"),
    SEMINAR_HALL("Seminar Hall"),
    PROJECTOR("Projector"),
    OTHER_EQUIPMENT("Other Equipment");

    private final String displayName;

    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

package com.campusmanager.model;

/**
 * Enumeration representing operational availability status of a campus resource.
 */
public enum ResourceStatus {
    AVAILABLE("Available"),
    UNDER_MAINTENANCE("Under Maintenance"),
    DECOMMISSIONED("Decommissioned");

    private final String description;

    ResourceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

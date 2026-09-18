package com.campusmanager.model;

/**
 * Enumeration representing repair status for reported issues.
 */
public enum MaintenanceStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String label;

    MaintenanceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

package com.campusmanager.model;

/**
 * Enumeration representing the lifecycle state of a resource booking.
 */
public enum BookingStatus {
    ACTIVE("Active"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");

    private final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

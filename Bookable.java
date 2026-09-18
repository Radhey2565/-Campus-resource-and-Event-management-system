package com.campusmanager.model;

/**
 * Interface representing any campus entity that can be scheduled or booked.
 * Demonstrates the interface concept in Java.
 */
public interface Bookable {
    String getId();
    String getName();
    boolean isAvailable();
    void setStatus(ResourceStatus status);
}

package com.campusmanager.exception;

/**
 * Thrown when trying to create a booking for a resource currently marked as under maintenance.
 */
public class ResourceUnderMaintenanceException extends CampusManagementException {
    public ResourceUnderMaintenanceException(String message) {
        super(message);
    }
}

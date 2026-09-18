package com.campusmanager.exception;

/**
 * Thrown when attempting to book or operate on a resource that is not available
 * (e.g., decommissioned or already occupied).
 */
public class ResourceNotAvailableException extends CampusManagementException {
    public ResourceNotAvailableException(String message) {
        super(message);
    }
}

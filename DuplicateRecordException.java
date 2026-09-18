package com.campusmanager.exception;

/**
 * Thrown when trying to insert a resource, event, or participant with an identifier that already exists.
 */
public class DuplicateRecordException extends CampusManagementException {
    public DuplicateRecordException(String message) {
        super(message);
    }
}

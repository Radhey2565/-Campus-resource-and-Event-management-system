package com.campusmanager.exception;

/**
 * Thrown when domain business rules are violated (e.g., start time is after end time, past dates, etc.).
 */
public class InvalidInputException extends CampusManagementException {
    public InvalidInputException(String message) {
        super(message);
    }
}

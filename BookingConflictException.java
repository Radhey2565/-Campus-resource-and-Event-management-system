package com.campusmanager.exception;

/**
 * Thrown when an overlapping booking exists for the same resource in the requested time slot.
 */
public class BookingConflictException extends CampusManagementException {
    public BookingConflictException(String message) {
        super(message);
    }
}

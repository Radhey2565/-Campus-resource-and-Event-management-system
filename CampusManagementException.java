package com.campusmanager.exception;

/**
 * Base custom checked exception for the Campus Resource Management System.
 * Demonstrates Custom Exception Hierarchy in Java.
 */
public class CampusManagementException extends Exception {
    public CampusManagementException(String message) {
        super(message);
    }

    public CampusManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}

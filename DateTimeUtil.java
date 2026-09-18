package com.campusmanager.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class demonstrating Java 8+ Date and Time API (java.time).
 * Uses static members and immutable formatters.
 */
public final class DateTimeUtil {

    // Formatters
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    // Private constructor to prevent instantiation of utility class
    private DateTimeUtil() {
    }

    public static LocalDateTime parseDateTime(String input) throws DateTimeParseException {
        if (input == null || input.trim().isEmpty()) {
            throw new DateTimeParseException("Date-time string cannot be empty", "", 0);
        }
        return LocalDateTime.parse(input.trim(), DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String input) throws DateTimeParseException {
        if (input == null || input.trim().isEmpty()) {
            throw new DateTimeParseException("Date string cannot be empty", "", 0);
        }
        return LocalDate.parse(input.trim(), DATE_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DATE_FORMATTER);
    }

    /**
     * Checks if two time intervals [startA, endA) and [startB, endB) overlap.
     * Standard mathematical interval intersection: startA < endB and endA > startB.
     */
    public static boolean isOverlapping(LocalDateTime startA, LocalDateTime endA,
                                       LocalDateTime startB, LocalDateTime endB) {
        if (startA == null || endA == null || startB == null || endB == null) {
            return false;
        }
        return startA.isBefore(endB) && endA.isAfter(startB);
    }

    /**
     * Validates that start time is strictly before end time.
     */
    public static boolean isValidInterval(LocalDateTime start, LocalDateTime end) {
        return start != null && end != null && start.isBefore(end);
    }
}

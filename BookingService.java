package com.campusmanager.service;

import com.campusmanager.exception.*;
import com.campusmanager.model.*;
import com.campusmanager.repository.DataStore;
import com.campusmanager.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing resource bookings and conflict detection.
 * Enforces business rules:
 * - Overlap prevention
 * - Maintenance restriction
 * - Resource operational availability
 */
public class BookingService {
    private final DataStore dataStore;

    public BookingService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(dataStore.getBookings().values());
    }

    public Booking getBookingById(String id) {
        if (id == null) return null;
        return dataStore.getBookings().get(id.trim());
    }

    /**
     * Checks if a resource is available for the given time slot.
     */
    public boolean isResourceAvailable(String resourceId, LocalDateTime start, LocalDateTime end) {
        Resource resource = dataStore.getResources().get(resourceId);
        if (resource == null || resource.getStatus() != ResourceStatus.AVAILABLE) {
            return false;
        }

        // Check if there are active bookings overlapping with requested slot
        return dataStore.getBookings().values().stream()
                .filter(b -> b.getResourceId().equalsIgnoreCase(resourceId) && b.isActive())
                .noneMatch(b -> DateTimeUtil.isOverlapping(start, end, b.getStartDateTime(), b.getEndDateTime()));
    }

    /**
     * Finds any conflicting booking for the given resource and time window.
     */
    public Booking findConflictingBooking(String resourceId, LocalDateTime start, LocalDateTime end) {
        return dataStore.getBookings().values().stream()
                .filter(b -> b.getResourceId().equalsIgnoreCase(resourceId) && b.isActive())
                .filter(b -> DateTimeUtil.isOverlapping(start, end, b.getStartDateTime(), b.getEndDateTime()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a new booking after strictly validating:
     * 1. Resource existence and availability
     * 2. Maintenance restrictions
     * 3. Time interval correctness
     * 4. Overlap with existing active bookings
     */
    public Booking createBooking(String resourceId, String eventId, String purpose,
                                 LocalDateTime start, LocalDateTime end, String bookedBy)
            throws ResourceNotAvailableException, BookingConflictException,
                   ResourceUnderMaintenanceException, InvalidInputException {

        if (resourceId == null || resourceId.trim().isEmpty()) {
            throw new InvalidInputException("Resource ID is required.");
        }
        Resource resource = dataStore.getResources().get(resourceId.trim());
        if (resource == null) {
            throw new ResourceNotAvailableException("Resource with ID '" + resourceId + "' does not exist.");
        }

        // Check maintenance status
        if (resource.getStatus() == ResourceStatus.UNDER_MAINTENANCE) {
            throw new ResourceUnderMaintenanceException("Cannot book resource '" + resource.getName() +
                    "' because it is currently UNDER MAINTENANCE.");
        }

        if (resource.getStatus() == ResourceStatus.DECOMMISSIONED) {
            throw new ResourceNotAvailableException("Resource '" + resource.getName() +
                    "' is permanently DECOMMISSIONED and cannot be booked.");
        }

        // Time slot validation
        if (!DateTimeUtil.isValidInterval(start, end)) {
            throw new InvalidInputException("Booking start time must be strictly before end time.");
        }

        // If linked to an event, validate event exists and is not cancelled
        if (eventId != null && !eventId.trim().isEmpty() && !eventId.equalsIgnoreCase("N/A")) {
            Event event = dataStore.getEvents().get(eventId.trim());
            if (event == null) {
                throw new InvalidInputException("Linked event with ID '" + eventId + "' was not found.");
            }
            if (event.isCancelled()) {
                throw new InvalidInputException("Cannot book resource for an event that is CANCELLED.");
            }
        }

        // Check for conflicting bookings
        Booking conflict = findConflictingBooking(resource.getId(), start, end);
        if (conflict != null) {
            throw new BookingConflictException(String.format(
                    "Booking Conflict! Resource '%s' is already booked by '%s' from %s to %s (Booking ID: %s).",
                    resource.getName(), conflict.getBookedBy(),
                    DateTimeUtil.formatDateTime(conflict.getStartDateTime()),
                    DateTimeUtil.formatDateTime(conflict.getEndDateTime()),
                    conflict.getId()));
        }

        String bookingId = "BK-" + (dataStore.getBookings().size() + 1001);
        Booking booking = new Booking(bookingId, resource.getId(),
                (eventId != null && !eventId.trim().isEmpty()) ? eventId.trim() : null,
                purpose != null ? purpose.trim() : "Campus Activity",
                start, end,
                bookedBy != null ? bookedBy.trim() : "Faculty/Staff",
                BookingStatus.ACTIVE);

        dataStore.getBookings().put(bookingId, booking);
        dataStore.saveBookings();
        return booking;
    }

    public boolean cancelBooking(String bookingId) throws InvalidInputException {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidInputException("Booking '" + bookingId + "' is already cancelled.");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        dataStore.saveBookings();
        return true;
    }

    public List<Booking> getBookingsForResource(String resourceId) {
        return dataStore.getBookings().values().stream()
                .filter(b -> b.getResourceId().equalsIgnoreCase(resourceId))
                .collect(Collectors.toList());
    }

    public List<Booking> getActiveBookings() {
        return dataStore.getBookings().values().stream()
                .filter(Booking::isActive)
                .collect(Collectors.toList());
    }
}

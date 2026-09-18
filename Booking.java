package com.campusmanager.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a reservation of a campus resource for a specific event or purpose.
 */
public class Booking {
    private String id;
    private String resourceId;
    private String eventId;
    private String purpose;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String bookedBy;
    private BookingStatus status;

    public Booking() {
        this.status = BookingStatus.ACTIVE;
    }

    public Booking(String id, String resourceId, String eventId, String purpose,
                   LocalDateTime startDateTime, LocalDateTime endDateTime, String bookedBy, BookingStatus status) {
        this.id = id;
        this.resourceId = resourceId;
        this.eventId = eventId;
        this.purpose = purpose;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.bookedBy = bookedBy;
        this.status = status != null ? status : BookingStatus.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return this.status == BookingStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] Res: %s | Event: %s | By: %s | Slot: %s to %s | Status: %s | Purpose: %s",
                id, resourceId, (eventId != null ? eventId : "N/A"), bookedBy,
                startDateTime, endDateTime, status.getLabel(), purpose);
    }
}

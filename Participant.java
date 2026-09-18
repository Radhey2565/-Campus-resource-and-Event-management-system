package com.campusmanager.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a registered attendee/participant for a campus event.
 */
public class Participant {
    private String participantId;
    private String eventId;
    private String name;
    private String email;
    private String department;
    private LocalDateTime registeredDate;

    public Participant() {
    }

    public Participant(String participantId, String eventId, String name, String email, String department, LocalDateTime registeredDate) {
        this.participantId = participantId;
        this.eventId = eventId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.registeredDate = registeredDate != null ? registeredDate : LocalDateTime.now();
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDateTime registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(eventId, that.eventId) &&
               Objects.equals(email != null ? email.toLowerCase() : null, that.email != null ? that.email.toLowerCase() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, email != null ? email.toLowerCase() : null);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s, Dept: %s)", participantId, name, email, department);
    }
}

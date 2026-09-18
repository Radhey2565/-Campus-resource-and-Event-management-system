package com.campusmanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a campus event scheduled by academic or student bodies.
 * Demonstrates Encapsulation and Collections (List/ArrayList).
 */
public class Event {
    private String id;
    private String title;
    private String organizer;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private EventStatus status;
    private int expectedAttendees;
    private final List<Participant> participants = new ArrayList<>();

    public Event() {
        this.status = EventStatus.SCHEDULED;
    }

    public Event(String id, String title, String organizer, LocalDateTime startDateTime,
                 LocalDateTime endDateTime, EventStatus status, int expectedAttendees) {
        this.id = id;
        this.title = title;
        this.organizer = organizer;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status != null ? status : EventStatus.SCHEDULED;
        this.expectedAttendees = expectedAttendees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public int getExpectedAttendees() {
        return expectedAttendees;
    }

    public void setExpectedAttendees(int expectedAttendees) {
        this.expectedAttendees = expectedAttendees;
    }

    public List<Participant> getParticipants() {
        return new ArrayList<>(participants);
    }

    public boolean addParticipant(Participant participant) {
        if (participant == null) return false;
        for (Participant p : participants) {
            if (p.getEmail().equalsIgnoreCase(participant.getEmail())) {
                return false; // Already registered
            }
        }
        return participants.add(participant);
    }

    public void setParticipants(List<Participant> newParticipants) {
        this.participants.clear();
        if (newParticipants != null) {
            for (Participant p : newParticipants) {
                addParticipant(p);
            }
        }
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public boolean isCancelled() {
        return status == EventStatus.CANCELLED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (Org: %s) | From: %s to %s | Status: %s | Attendees: %d (Reg: %d)",
                id, title, organizer, startDateTime, endDateTime, status.getLabel(), expectedAttendees, getParticipantCount());
    }
}

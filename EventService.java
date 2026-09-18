package com.campusmanager.service;

import com.campusmanager.exception.DuplicateRecordException;
import com.campusmanager.exception.InvalidInputException;
import com.campusmanager.model.Booking;
import com.campusmanager.model.BookingStatus;
import com.campusmanager.model.Event;
import com.campusmanager.model.EventStatus;
import com.campusmanager.model.Participant;
import com.campusmanager.repository.DataStore;
import com.campusmanager.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing college events and participant registrations.
 * Demonstrates Collections, Stream API, and cascading business rules.
 */
public class EventService {
    private final DataStore dataStore;

    public EventService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(dataStore.getEvents().values());
    }

    public Event getEventById(String id) {
        if (id == null) return null;
        return dataStore.getEvents().get(id.trim());
    }

    public void createEvent(Event event) throws DuplicateRecordException, InvalidInputException {
        if (event == null) {
            throw new InvalidInputException("Event cannot be null.");
        }
        if (event.getId() == null || event.getId().trim().isEmpty()) {
            throw new InvalidInputException("Event ID cannot be empty.");
        }
        String id = event.getId().trim();
        if (dataStore.getEvents().containsKey(id)) {
            throw new DuplicateRecordException("An event with ID '" + id + "' already exists.");
        }
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Event title cannot be empty.");
        }
        if (!DateTimeUtil.isValidInterval(event.getStartDateTime(), event.getEndDateTime())) {
            throw new InvalidInputException("Event start time must be strictly before end time.");
        }
        if (event.getStartDateTime().isBefore(LocalDateTime.now().minusDays(30))) {
            throw new InvalidInputException("Cannot schedule an event in the past (more than 30 days prior).");
        }
        if (event.getExpectedAttendees() <= 0) {
            throw new InvalidInputException("Expected attendees must be greater than zero.");
        }

        dataStore.getEvents().put(id, event);
        dataStore.saveEvents();
    }

    public boolean updateEvent(String id, String newTitle, String newOrganizer, Integer newAttendees)
            throws InvalidInputException {
        Event event = getEventById(id);
        if (event == null) {
            return false;
        }
        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new InvalidInputException("Cannot update an event that is cancelled.");
        }
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            event.setTitle(newTitle.trim());
        }
        if (newOrganizer != null && !newOrganizer.trim().isEmpty()) {
            event.setOrganizer(newOrganizer.trim());
        }
        if (newAttendees != null) {
            if (newAttendees <= 0) {
                throw new InvalidInputException("Expected attendees must be greater than zero.");
            }
            event.setExpectedAttendees(newAttendees);
        }
        dataStore.saveEvents();
        return true;
    }

    /**
     * Cancels an event and cascades cancellation to all its active resource bookings.
     * Business rule: Cancelled events should not retain active resource bookings.
     */
    public boolean cancelEvent(String id) throws InvalidInputException {
        Event event = getEventById(id);
        if (event == null) {
            return false;
        }
        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new InvalidInputException("Event '" + id + "' is already cancelled.");
        }

        event.setStatus(EventStatus.CANCELLED);
        dataStore.saveEvents();

        // Cascade cancellation to active bookings linked to this event
        boolean bookingsModified = false;
        for (Booking booking : dataStore.getBookings().values()) {
            if (id.equalsIgnoreCase(booking.getEventId()) && booking.isActive()) {
                booking.setStatus(BookingStatus.CANCELLED);
                bookingsModified = true;
            }
        }
        if (bookingsModified) {
            dataStore.saveBookings();
        }

        return true;
    }

    /**
     * Registers a participant for an event.
     * Business rule: A participant should not be registered twice for the same event.
     */
    public void registerParticipant(String eventId, String name, String email, String department)
            throws InvalidInputException, DuplicateRecordException {
        Event event = getEventById(eventId);
        if (event == null) {
            throw new InvalidInputException("Event with ID '" + eventId + "' does not exist.");
        }
        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new InvalidInputException("Cannot register participants for a cancelled event.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Participant name cannot be blank.");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new InvalidInputException("Invalid participant email address.");
        }

        // Check for duplicate participant for this event
        boolean alreadyRegistered = event.getParticipants().stream()
                .anyMatch(p -> p.getEmail().equalsIgnoreCase(email.trim()));
        if (alreadyRegistered) {
            throw new DuplicateRecordException("Participant with email '" + email.trim() + "' is already registered for this event.");
        }

        String participantId = "PRT-" + (dataStore.getParticipants().size() + 101);
        Participant participant = new Participant(participantId, eventId, name.trim(), email.trim(),
                department != null ? department.trim() : "General", LocalDateTime.now());

        event.addParticipant(participant);
        dataStore.getParticipants().add(participant);
        dataStore.saveParticipants();
    }

    public List<Event> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return dataStore.getEvents().values().stream()
                .filter(e -> e.getEndDateTime().isAfter(now) && e.getStatus() != EventStatus.CANCELLED)
                .sorted((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()))
                .collect(Collectors.toList());
    }
}

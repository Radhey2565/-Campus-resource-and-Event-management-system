package com.campusmanager.service;

import com.campusmanager.exception.DuplicateRecordException;
import com.campusmanager.exception.InvalidInputException;
import com.campusmanager.model.*;
import com.campusmanager.repository.DataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service handling campus resources logic (Classrooms, Labs, Seminar Halls, Equipment).
 * Demonstrates Collections, Stream API, and Custom Exception handling.
 */
public class ResourceService {
    private final DataStore dataStore;

    public ResourceService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<Resource> getAllResources() {
        return new ArrayList<>(dataStore.getResources().values());
    }

    public Resource getResourceById(String id) {
        if (id == null) return null;
        return dataStore.getResources().get(id.trim());
    }

    public void addResource(Resource resource) throws DuplicateRecordException, InvalidInputException {
        if (resource == null) {
            throw new InvalidInputException("Resource cannot be null.");
        }
        if (resource.getId() == null || resource.getId().trim().isEmpty()) {
            throw new InvalidInputException("Resource ID cannot be empty.");
        }
        String id = resource.getId().trim();
        if (dataStore.getResources().containsKey(id)) {
            throw new DuplicateRecordException("A resource with ID '" + id + "' already exists.");
        }
        if (resource.getName() == null || resource.getName().trim().isEmpty()) {
            throw new InvalidInputException("Resource name cannot be empty.");
        }
        if (resource.getCapacity() <= 0) {
            throw new InvalidInputException("Capacity must be greater than zero.");
        }

        dataStore.getResources().put(id, resource);
        dataStore.saveResources();
    }

    public boolean updateResource(String id, String newName, String newLocation, int newCapacity)
            throws InvalidInputException {
        Resource resource = getResourceById(id);
        if (resource == null) {
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            resource.setName(newName.trim());
        }
        if (newLocation != null && !newLocation.trim().isEmpty()) {
            resource.setLocation(newLocation.trim());
        }
        if (newCapacity > 0) {
            resource.setCapacity(newCapacity);
        } else if (newCapacity < 0) {
            throw new InvalidInputException("Capacity cannot be negative.");
        }
        dataStore.saveResources();
        return true;
    }

    public boolean removeResource(String id) throws InvalidInputException {
        Resource resource = getResourceById(id);
        if (resource == null) {
            return false;
        }
        // Check for active bookings associated with this resource
        boolean hasActiveBookings = dataStore.getBookings().values().stream()
                .anyMatch(b -> b.getResourceId().equalsIgnoreCase(id) && b.isActive());
        if (hasActiveBookings) {
            throw new InvalidInputException("Cannot delete resource '" + id + "' because it has active bookings. Cancel the bookings first.");
        }

        dataStore.getResources().remove(id);
        dataStore.saveResources();
        return true;
    }

    public List<Resource> searchResources(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllResources();
        }
        String q = query.trim().toLowerCase();
        return dataStore.getResources().values().stream()
                .filter(r -> r.getId().toLowerCase().contains(q) ||
                             r.getName().toLowerCase().contains(q) ||
                             r.getLocation().toLowerCase().contains(q) ||
                             r.getType().name().toLowerCase().contains(q) ||
                             r.getType().getDisplayName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Resource> getAvailableResources() {
        return dataStore.getResources().values().stream()
                .filter(Resource::isAvailable)
                .collect(Collectors.toList());
    }
}

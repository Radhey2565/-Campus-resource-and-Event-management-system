package com.campusmanager.model;

import java.util.Objects;

/**
 * Abstract base class demonstrating OOP principles:
 * Encapsulation, Abstraction, and Polymorphism.
 */
public abstract class Resource implements Bookable {
    private String id;
    private String name;
    private ResourceType type;
    private String location;
    private ResourceStatus status;
    private int capacity;

    // Default constructor
    public Resource() {
        this.status = ResourceStatus.AVAILABLE;
        this.capacity = 1;
    }

    // Parameterized constructor
    public Resource(String id, String name, ResourceType type, String location, ResourceStatus status, int capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.status = status != null ? status : ResourceStatus.AVAILABLE;
        this.capacity = capacity;
    }

    // Abstract method demonstrating Polymorphism in subclasses
    public abstract String getSpecificDetails();

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ResourceStatus status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean isAvailable() {
        return this.status == ResourceStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Loc: %s | Cap: %d | Status: %s | %s",
                id, name, type.getDisplayName(), location, capacity, status.getDescription(), getSpecificDetails());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

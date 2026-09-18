package com.campusmanager.model;

/**
 * Represents standalone campus equipment such as Projectors and Audio systems.
 * Demonstrates Inheritance and Method Overriding.
 */
public class Equipment extends Resource {
    private String specifications;
    private boolean portable;

    public Equipment() {
        super();
    }

    public Equipment(String id, String name, ResourceType type, String location, ResourceStatus status,
                     String specifications, boolean portable) {
        super(id, name, type, location, status, 1);
        this.specifications = specifications;
        this.portable = portable;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public boolean isPortable() {
        return portable;
    }

    public void setPortable(boolean portable) {
        this.portable = portable;
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Specs: %s, Portable: %s",
                specifications != null ? specifications : "N/A",
                portable ? "Yes" : "No");
    }
}

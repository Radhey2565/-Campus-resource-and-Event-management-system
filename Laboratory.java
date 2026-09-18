package com.campusmanager.model;

/**
 * Represents computer and engineering laboratories.
 * Demonstrates Inheritance and Method Overriding.
 */
public class Laboratory extends Resource {
    private int workstationsCount;
    private String softwareConfig;

    public Laboratory() {
        super();
    }

    public Laboratory(String id, String name, String location, ResourceStatus status, int capacity,
                      int workstationsCount, String softwareConfig) {
        super(id, name, ResourceType.COMPUTER_LAB, location, status, capacity);
        this.workstationsCount = workstationsCount;
        this.softwareConfig = softwareConfig;
    }

    public int getWorkstationsCount() {
        return workstationsCount;
    }

    public void setWorkstationsCount(int workstationsCount) {
        this.workstationsCount = workstationsCount;
    }

    public String getSoftwareConfig() {
        return softwareConfig;
    }

    public void setSoftwareConfig(String softwareConfig) {
        this.softwareConfig = softwareConfig;
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Workstations: %d, Software: %s",
                workstationsCount, softwareConfig != null ? softwareConfig : "Standard");
    }
}

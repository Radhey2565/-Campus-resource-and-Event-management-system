package com.campusmanager.service;

import com.campusmanager.exception.InvalidInputException;
import com.campusmanager.model.MaintenanceRecord;
import com.campusmanager.model.MaintenanceStatus;
import com.campusmanager.model.Resource;
import com.campusmanager.model.ResourceStatus;
import com.campusmanager.repository.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing maintenance reports, repair tracking, and resource health status.
 */
public class MaintenanceService {
    private final DataStore dataStore;

    public MaintenanceService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<MaintenanceRecord> getAllRecords() {
        return new ArrayList<>(dataStore.getMaintenanceRecords());
    }

    public List<MaintenanceRecord> getPendingRecords() {
        return dataStore.getMaintenanceRecords().stream()
                .filter(MaintenanceRecord::isPendingOrInProgress)
                .collect(Collectors.toList());
    }

    public MaintenanceRecord getRecordById(String id) {
        if (id == null) return null;
        return dataStore.getMaintenanceRecords().stream()
                .filter(m -> m.getId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Reports a damaged resource.
     * Automatically changes the resource status to UNDER_MAINTENANCE.
     */
    public MaintenanceRecord reportIssue(String resourceId, String issueDescription, String reportedBy)
            throws InvalidInputException {
        if (resourceId == null || resourceId.trim().isEmpty()) {
            throw new InvalidInputException("Resource ID cannot be empty.");
        }
        Resource resource = dataStore.getResources().get(resourceId.trim());
        if (resource == null) {
            throw new InvalidInputException("Resource with ID '" + resourceId + "' does not exist.");
        }
        if (issueDescription == null || issueDescription.trim().isEmpty()) {
            throw new InvalidInputException("Issue description cannot be empty.");
        }

        String recordId = "MNT-" + (dataStore.getMaintenanceRecords().size() + 501);
        MaintenanceRecord record = new MaintenanceRecord(recordId, resource.getId(), issueDescription.trim(),
                LocalDate.now(), null, MaintenanceStatus.PENDING,
                reportedBy != null ? reportedBy.trim() : "Campus Staff");

        // Mark resource as under maintenance
        resource.setStatus(ResourceStatus.UNDER_MAINTENANCE);

        dataStore.getMaintenanceRecords().add(record);
        dataStore.saveResources();
        dataStore.saveMaintenance();

        return record;
    }

    /**
     * Updates status of maintenance record.
     * When status transitions to RESOLVED, the resource status is automatically restored to AVAILABLE.
     */
    public boolean updateMaintenanceStatus(String recordId, MaintenanceStatus newStatus, LocalDate resolvedDate)
            throws InvalidInputException {
        MaintenanceRecord record = getRecordById(recordId);
        if (record == null) {
            return false;
        }
        if (newStatus == null) {
            throw new InvalidInputException("New maintenance status must be provided.");
        }

        record.setStatus(newStatus);
        if (newStatus == MaintenanceStatus.RESOLVED) {
            record.setResolvedDate(resolvedDate != null ? resolvedDate : LocalDate.now());

            // If no other pending maintenance exists for this resource, restore it to AVAILABLE
            boolean hasOtherPending = dataStore.getMaintenanceRecords().stream()
                    .filter(m -> !m.getId().equalsIgnoreCase(record.getId()))
                    .filter(m -> m.getResourceId().equalsIgnoreCase(record.getResourceId()))
                    .anyMatch(MaintenanceRecord::isPendingOrInProgress);

            if (!hasOtherPending) {
                Resource resource = dataStore.getResources().get(record.getResourceId());
                if (resource != null && resource.getStatus() == ResourceStatus.UNDER_MAINTENANCE) {
                    resource.setStatus(ResourceStatus.AVAILABLE);
                    dataStore.saveResources();
                }
            }
        }

        dataStore.saveMaintenance();
        return true;
    }
}

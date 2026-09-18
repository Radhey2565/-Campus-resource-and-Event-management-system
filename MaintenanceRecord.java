package com.campusmanager.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a maintenance issue, repair task, or condition log for a resource.
 */
public class MaintenanceRecord {
    private String id;
    private String resourceId;
    private String issueDescription;
    private LocalDate reportedDate;
    private LocalDate resolvedDate;
    private MaintenanceStatus status;
    private String reportedBy;

    public MaintenanceRecord() {
        this.status = MaintenanceStatus.PENDING;
        this.reportedDate = LocalDate.now();
    }

    public MaintenanceRecord(String id, String resourceId, String issueDescription,
                             LocalDate reportedDate, LocalDate resolvedDate,
                             MaintenanceStatus status, String reportedBy) {
        this.id = id;
        this.resourceId = resourceId;
        this.issueDescription = issueDescription;
        this.reportedDate = reportedDate != null ? reportedDate : LocalDate.now();
        this.resolvedDate = resolvedDate;
        this.status = status != null ? status : MaintenanceStatus.PENDING;
        this.reportedBy = reportedBy;
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

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocalDate getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDate resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public boolean isPendingOrInProgress() {
        return status == MaintenanceStatus.PENDING || status == MaintenanceStatus.IN_PROGRESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceRecord that = (MaintenanceRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] Res: %s | Status: %s | Reported: %s by %s | Resolved: %s | Issue: %s",
                id, resourceId, status.getLabel(), reportedDate, reportedBy,
                resolvedDate != null ? resolvedDate.toString() : "Pending", issueDescription);
    }
}

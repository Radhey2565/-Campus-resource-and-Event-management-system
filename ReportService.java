package com.campusmanager.service;

import com.campusmanager.model.*;
import com.campusmanager.repository.DataStore;
import com.campusmanager.util.DateTimeUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Service for analytical summaries, utilization metrics, and asynchronous report generation.
 * Demonstrates:
 * - Java Stream API (filtering, grouping, counting, sorting)
 * - Multithreading (generating and exporting reports in a background thread)
 */
public class ReportService {
    private final DataStore dataStore;

    public ReportService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Generates a comprehensive analytical text report.
     * Uses Java Stream API for aggregation.
     */
    public String generateFullReport() {
        StringBuilder sb = new StringBuilder();
        String line = "=".repeat(70);
        String subLine = "-".repeat(70);

        sb.append(line).append("\n");
        sb.append("        CAMPUS RESOURCE & EVENT MANAGEMENT SYSTEM - AUDIT REPORT\n");
        sb.append("        Generated On: ").append(DateTimeUtil.formatDateTime(LocalDateTime.now())).append("\n");
        sb.append(line).append("\n\n");

        // 1. RESOURCE SUMMARY
        Collection<Resource> resources = dataStore.getResources().values();
        long totalResources = resources.size();
        long availableResources = resources.stream().filter(Resource::isAvailable).count();
        long maintenanceResources = resources.stream().filter(r -> r.getStatus() == ResourceStatus.UNDER_MAINTENANCE).count();
        long decommissionedResources = resources.stream().filter(r -> r.getStatus() == ResourceStatus.DECOMMISSIONED).count();

        sb.append("1. RESOURCE INVENTORY & HEALTH SUMMARY\n");
        sb.append(subLine).append("\n");
        sb.append(String.format("  Total Registered Resources : %d\n", totalResources));
        sb.append(String.format("  [+] Available for Booking  : %d (%.1f%%)\n",
                availableResources, totalResources > 0 ? (availableResources * 100.0 / totalResources) : 0));
        sb.append(String.format("  [!] Under Maintenance      : %d (%.1f%%)\n",
                maintenanceResources, totalResources > 0 ? (maintenanceResources * 100.0 / totalResources) : 0));
        sb.append(String.format("  [-] Decommissioned         : %d\n", decommissionedResources));

        // Group by ResourceType
        Map<ResourceType, Long> byType = resources.stream()
                .collect(Collectors.groupingBy(Resource::getType, Collectors.counting()));
        sb.append("  Breakdown by Category:\n");
        for (ResourceType type : ResourceType.values()) {
            long count = byType.getOrDefault(type, 0L);
            sb.append(String.format("    - %-20s : %d\n", type.getDisplayName(), count));
        }
        sb.append("\n");

        // 2. EVENT SUMMARY
        Collection<Event> events = dataStore.getEvents().values();
        LocalDateTime now = LocalDateTime.now();
        long totalEvents = events.size();
        List<Event> upcomingEvents = events.stream()
                .filter(e -> e.getEndDateTime().isAfter(now) && e.getStatus() != EventStatus.CANCELLED)
                .sorted(Comparator.comparing(Event::getStartDateTime))
                .collect(Collectors.toList());
        long cancelledEvents = events.stream().filter(Event::isCancelled).count();
        int totalRegistrations = dataStore.getParticipants().size();

        sb.append("2. EVENT MANAGEMENT & PARTICIPATION SUMMARY\n");
        sb.append(subLine).append("\n");
        sb.append(String.format("  Total Events Scheduled     : %d\n", totalEvents));
        sb.append(String.format("  Upcoming / Active Events   : %d\n", upcomingEvents.size()));
        sb.append(String.format("  Cancelled Events           : %d\n", cancelledEvents));
        sb.append(String.format("  Total Participant Sign-ups : %d\n", totalRegistrations));

        if (!upcomingEvents.isEmpty()) {
            sb.append("  Next Upcoming Events:\n");
            for (Event e : upcomingEvents) {
                sb.append(String.format("    * [%s] %-30s | %s | Att: %d\n",
                        e.getId(), e.getTitle(), DateTimeUtil.formatDateTime(e.getStartDateTime()), e.getParticipantCount()));
            }
        }
        sb.append("\n");

        // 3. BOOKING STATISTICS & MOST FREQUENTLY BOOKED
        Collection<Booking> bookings = dataStore.getBookings().values();
        long totalBookings = bookings.size();
        long activeBookings = bookings.stream().filter(Booking::isActive).count();
        long completedBookings = bookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count();
        long cancelledBookings = bookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count();

        sb.append("3. BOOKING UTILIZATION & MOST-USED RESOURCES\n");
        sb.append(subLine).append("\n");
        sb.append(String.format("  Total Booking Records      : %d\n", totalBookings));
        sb.append(String.format("  Active Bookings            : %d\n", activeBookings));
        sb.append(String.format("  Completed Bookings         : %d\n", completedBookings));
        sb.append(String.format("  Cancelled Bookings         : %d\n", cancelledBookings));

        // Most booked resources
        Map<String, Long> bookingCounts = bookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .collect(Collectors.groupingBy(Booking::getResourceId, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedBookingCounts = bookingCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        sb.append("  Most Frequently Booked Resources:\n");
        if (sortedBookingCounts.isEmpty()) {
            sb.append("    (No active or completed bookings recorded)\n");
        } else {
            int rank = 1;
            for (Map.Entry<String, Long> entry : sortedBookingCounts) {
                Resource r = dataStore.getResources().get(entry.getKey());
                String rName = r != null ? r.getName() : "Unknown";
                sb.append(String.format("    %d. [%s] %-28s : %d bookings\n",
                        rank++, entry.getKey(), rName, entry.getValue()));
                if (rank > 5) break; // Top 5
            }
        }
        sb.append("\n");

        // 4. MAINTENANCE LOG AUDIT
        List<MaintenanceRecord> records = dataStore.getMaintenanceRecords();
        long totalIncidents = records.size();
        long pendingIssues = records.stream().filter(m -> m.getStatus() == MaintenanceStatus.PENDING).count();
        long inProgressIssues = records.stream().filter(m -> m.getStatus() == MaintenanceStatus.IN_PROGRESS).count();
        long resolvedIssues = records.stream().filter(m -> m.getStatus() == MaintenanceStatus.RESOLVED).count();

        sb.append("4. MAINTENANCE STATUS & DEFECT LOGS\n");
        sb.append(subLine).append("\n");
        sb.append(String.format("  Total Maintenance Incidents: %d\n", totalIncidents));
        sb.append(String.format("  Pending Repair             : %d\n", pendingIssues));
        sb.append(String.format("  Work In Progress           : %d\n", inProgressIssues));
        sb.append(String.format("  Resolved Issues            : %d\n", resolvedIssues));

        List<MaintenanceRecord> openIncidents = records.stream()
                .filter(MaintenanceRecord::isPendingOrInProgress)
                .collect(Collectors.toList());
        if (!openIncidents.isEmpty()) {
            sb.append("  Urgent Unresolved Issues:\n");
            for (MaintenanceRecord rec : openIncidents) {
                sb.append(String.format("    * [%s] Res: %-10s | %-12s | Reported: %s | Issue: %s\n",
                        rec.getId(), rec.getResourceId(), rec.getStatus().getLabel(),
                        DateTimeUtil.formatDate(rec.getReportedDate()), rec.getIssueDescription()));
            }
        }

        sb.append("\n").append(line).append("\n");
        sb.append("                       END OF REPORT\n");
        sb.append(line).append("\n");

        return sb.toString();
    }

    /**
     * Synchronously exports the report to a file.
     */
    public void exportReportSync(String destinationPath) throws IOException {
        String report = generateFullReport();
        File file = new File(destinationPath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print(report);
        }
    }

    /**
     * Demonstrates MULTITHREADING in Java:
     * Generates and writes the audit report in a dedicated background worker Thread
     * so that the main UI thread remains responsive and non-blocking.
     */
    public Thread exportReportAsync(String destinationPath, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread workerThread = new Thread(() -> {
            try {
                // Simulate intensive analytical calculation / aggregation
                Thread.sleep(800);
                exportReportSync(destinationPath);
                if (onSuccess != null) {
                    onSuccess.accept(destinationPath);
                }
            } catch (Exception e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }, "ReportGenerator-Thread");

        workerThread.setDaemon(true);
        workerThread.start();
        return workerThread;
    }
}

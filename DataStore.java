package com.campusmanager.repository;

import com.campusmanager.model.*;
import com.campusmanager.util.DateTimeUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Local file-based persistence manager.
 * Reads and writes CSV files without requiring an external database.
 * Demonstrates Java File I/O, Collections (Map, List), and String tokenization.
 */
public class DataStore {
    private final String dataDirectory;

    private final Map<String, Resource> resources = new LinkedHashMap<>();
    private final Map<String, Event> events = new LinkedHashMap<>();
    private final Map<String, Booking> bookings = new LinkedHashMap<>();
    private final List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
    private final List<Participant> participants = new ArrayList<>();

    public DataStore(String dataDirectory) {
        this.dataDirectory = dataDirectory != null ? dataDirectory : "data";
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(dataDirectory));
        } catch (IOException e) {
            System.err.println("Warning: Could not create data directory: " + e.getMessage());
        }
    }

    public synchronized void loadAllData() {
        loadResources();
        loadEvents();
        loadParticipants();
        linkParticipantsToEvents();
        loadBookings();
        loadMaintenance();
    }

    // ==================== RESOURCES ====================

    private void loadResources() {
        resources.clear();
        File file = new File(dataDirectory, "resources.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length < 6) continue;

                String id = parts[0].trim();
                String name = parts[1].trim();
                ResourceType type;
                try {
                    type = ResourceType.valueOf(parts[2].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    type = ResourceType.OTHER_EQUIPMENT;
                }
                String location = parts[3].trim();
                ResourceStatus status;
                try {
                    status = ResourceStatus.valueOf(parts[4].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    status = ResourceStatus.AVAILABLE;
                }
                int capacity = parseIntOrDefault(parts[5], 1);
                String extra = parts.length > 6 ? parts[6].trim() : "";

                Resource resource;
                if (type == ResourceType.CLASSROOM || type == ResourceType.SEMINAR_HALL) {
                    boolean proj = extra.toLowerCase().contains("projector:yes") || extra.toLowerCase().contains("stage:yes");
                    boolean ac = extra.toLowerCase().contains("ac:yes") || extra.toLowerCase().contains("audio");
                    resource = new Room(id, name, type, location, status, capacity, proj, ac);
                } else if (type == ResourceType.COMPUTER_LAB) {
                    int workstations = capacity;
                    String sw = "Ubuntu / Windows Dev Environment";
                    if (!extra.isEmpty()) {
                        String[] pairs = extra.split(";");
                        for (String p : pairs) {
                            if (p.toLowerCase().startsWith("workstations:")) {
                                workstations = parseIntOrDefault(p.substring(13), capacity);
                            } else if (p.toLowerCase().startsWith("os:") || p.toLowerCase().startsWith("hardwarekits:")) {
                                sw = p;
                            }
                        }
                    }
                    resource = new Laboratory(id, name, location, status, capacity, workstations, sw);
                } else {
                    boolean portable = extra.toLowerCase().contains("portable:true") || type == ResourceType.PROJECTOR;
                    resource = new Equipment(id, name, type, location, status, extra, portable);
                }
                resources.put(id, resource);
            }
        } catch (IOException e) {
            System.err.println("Warning: Error loading resources: " + e.getMessage());
        }
    }

    public synchronized void saveResources() {
        File file = new File(dataDirectory, "resources.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("id,name,type,location,status,capacity,extraDetails");
            for (Resource r : resources.values()) {
                writer.printf("%s,%s,%s,%s,%s,%d,%s%n",
                        escapeCsv(r.getId()),
                        escapeCsv(r.getName()),
                        r.getType().name(),
                        escapeCsv(r.getLocation()),
                        r.getStatus().name(),
                        r.getCapacity(),
                        escapeCsv(r.getSpecificDetails()));
            }
        } catch (IOException e) {
            System.err.println("Error saving resources: " + e.getMessage());
        }
    }

    // ==================== EVENTS ====================

    private void loadEvents() {
        events.clear();
        File file = new File(dataDirectory, "events.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length < 7) continue;

                String id = parts[0].trim();
                String title = parts[1].trim();
                String organizer = parts[2].trim();
                LocalDateTime start = DateTimeUtil.parseDateTime(parts[3].trim());
                LocalDateTime end = DateTimeUtil.parseDateTime(parts[4].trim());
                EventStatus status;
                try {
                    status = EventStatus.valueOf(parts[5].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    status = EventStatus.SCHEDULED;
                }
                int attendees = parseIntOrDefault(parts[6], 50);

                Event event = new Event(id, title, organizer, start, end, status, attendees);
                events.put(id, event);
            }
        } catch (Exception e) {
            System.err.println("Warning: Error loading events: " + e.getMessage());
        }
    }

    public synchronized void saveEvents() {
        File file = new File(dataDirectory, "events.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("id,title,organizer,startDateTime,endDateTime,status,expectedAttendees");
            for (Event e : events.values()) {
                writer.printf("%s,%s,%s,%s,%s,%s,%d%n",
                        escapeCsv(e.getId()),
                        escapeCsv(e.getTitle()),
                        escapeCsv(e.getOrganizer()),
                        DateTimeUtil.formatDateTime(e.getStartDateTime()),
                        DateTimeUtil.formatDateTime(e.getEndDateTime()),
                        e.getStatus().name(),
                        e.getExpectedAttendees());
            }
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
        }
    }

    // ==================== BOOKINGS ====================

    private void loadBookings() {
        bookings.clear();
        File file = new File(dataDirectory, "bookings.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length < 8) continue;

                String id = parts[0].trim();
                String resourceId = parts[1].trim();
                String eventId = parts[2].trim();
                String purpose = parts[3].trim();
                LocalDateTime start = DateTimeUtil.parseDateTime(parts[4].trim());
                LocalDateTime end = DateTimeUtil.parseDateTime(parts[5].trim());
                String bookedBy = parts[6].trim();
                BookingStatus status;
                try {
                    status = BookingStatus.valueOf(parts[7].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    status = BookingStatus.ACTIVE;
                }

                Booking booking = new Booking(id, resourceId, eventId, purpose, start, end, bookedBy, status);
                bookings.put(id, booking);
            }
        } catch (Exception e) {
            System.err.println("Warning: Error loading bookings: " + e.getMessage());
        }
    }

    public synchronized void saveBookings() {
        File file = new File(dataDirectory, "bookings.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("id,resourceId,eventId,purpose,startDateTime,endDateTime,bookedBy,status");
            for (Booking b : bookings.values()) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                        escapeCsv(b.getId()),
                        escapeCsv(b.getResourceId()),
                        escapeCsv(b.getEventId() != null ? b.getEventId() : ""),
                        escapeCsv(b.getPurpose()),
                        DateTimeUtil.formatDateTime(b.getStartDateTime()),
                        DateTimeUtil.formatDateTime(b.getEndDateTime()),
                        escapeCsv(b.getBookedBy()),
                        b.getStatus().name());
            }
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    // ==================== MAINTENANCE ====================

    private void loadMaintenance() {
        maintenanceRecords.clear();
        File file = new File(dataDirectory, "maintenance.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length < 7) continue;

                String id = parts[0].trim();
                String resourceId = parts[1].trim();
                String issue = parts[2].trim();
                LocalDate reported = DateTimeUtil.parseDate(parts[3].trim());
                LocalDate resolved = null;
                if (!parts[4].trim().equalsIgnoreCase("N/A") && !parts[4].trim().isEmpty()) {
                    try {
                        resolved = DateTimeUtil.parseDate(parts[4].trim());
                    } catch (Exception ignored) {}
                }
                MaintenanceStatus status;
                try {
                    status = MaintenanceStatus.valueOf(parts[5].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    status = MaintenanceStatus.PENDING;
                }
                String reportedBy = parts[6].trim();

                MaintenanceRecord record = new MaintenanceRecord(id, resourceId, issue, reported, resolved, status, reportedBy);
                maintenanceRecords.add(record);
            }
        } catch (Exception e) {
            System.err.println("Warning: Error loading maintenance records: " + e.getMessage());
        }
    }

    public synchronized void saveMaintenance() {
        File file = new File(dataDirectory, "maintenance.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("id,resourceId,issueDescription,reportedDate,resolvedDate,status,reportedBy");
            for (MaintenanceRecord m : maintenanceRecords) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        escapeCsv(m.getId()),
                        escapeCsv(m.getResourceId()),
                        escapeCsv(m.getIssueDescription()),
                        DateTimeUtil.formatDate(m.getReportedDate()),
                        m.getResolvedDate() != null ? DateTimeUtil.formatDate(m.getResolvedDate()) : "N/A",
                        m.getStatus().name(),
                        escapeCsv(m.getReportedBy()));
            }
        } catch (IOException e) {
            System.err.println("Error saving maintenance: " + e.getMessage());
        }
    }

    // ==================== PARTICIPANTS ====================

    private void loadParticipants() {
        participants.clear();
        File file = new File(dataDirectory, "participants.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsvLine(line);
                if (parts.length < 6) continue;

                String id = parts[0].trim();
                String eventId = parts[1].trim();
                String name = parts[2].trim();
                String email = parts[3].trim();
                String department = parts[4].trim();
                LocalDateTime regDate;
                try {
                    regDate = DateTimeUtil.parseDateTime(parts[5].trim());
                } catch (Exception e) {
                    regDate = LocalDateTime.now();
                }

                Participant p = new Participant(id, eventId, name, email, department, regDate);
                participants.add(p);
            }
        } catch (Exception e) {
            System.err.println("Warning: Error loading participants: " + e.getMessage());
        }
    }

    private void linkParticipantsToEvents() {
        for (Participant p : participants) {
            Event event = events.get(p.getEventId());
            if (event != null) {
                event.addParticipant(p);
            }
        }
    }

    public synchronized void saveParticipants() {
        File file = new File(dataDirectory, "participants.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("participantId,eventId,name,email,department,registeredDate");
            for (Participant p : participants) {
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        escapeCsv(p.getParticipantId()),
                        escapeCsv(p.getEventId()),
                        escapeCsv(p.getName()),
                        escapeCsv(p.getEmail()),
                        escapeCsv(p.getDepartment()),
                        DateTimeUtil.formatDateTime(p.getRegisteredDate()));
            }
        } catch (IOException e) {
            System.err.println("Error saving participants: " + e.getMessage());
        }
    }

    // ==================== GETTERS ====================

    public Map<String, Resource> getResources() {
        return resources;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public Map<String, Booking> getBookings() {
        return bookings;
    }

    public List<MaintenanceRecord> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    // ==================== CSV HELPERS ====================

    private static String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++; // skip escaped quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static int parseIntOrDefault(String text, int def) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return def;
        }
    }
}

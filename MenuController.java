package com.campusmanager.ui;

import com.campusmanager.exception.*;
import com.campusmanager.model.*;
import com.campusmanager.service.*;
import com.campusmanager.util.DateTimeUtil;
import com.campusmanager.util.InputReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for the command-line menu interface.
 * Implements robust error handling and user-friendly console interaction.
 */
public class MenuController {
    private final ResourceService resourceService;
    private final EventService eventService;
    private final BookingService bookingService;
    private final MaintenanceService maintenanceService;
    private final ReportService reportService;
    private final InputReader input;

    public MenuController(ResourceService resourceService, EventService eventService,
                          BookingService bookingService, MaintenanceService maintenanceService,
                          ReportService reportService, Scanner scanner) {
        this.resourceService = resourceService;
        this.eventService = eventService;
        this.bookingService = bookingService;
        this.maintenanceService = maintenanceService;
        this.reportService = reportService;
        this.input = new InputReader(scanner);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printHeader("CAMPUS RESOURCE & EVENT MANAGEMENT SYSTEM");
            System.out.println("1. Resource Management");
            System.out.println("2. Event Management");
            System.out.println("3. Booking Management");
            System.out.println("4. Maintenance Management");
            System.out.println("5. Reports & Analytics");
            System.out.println("6. Exit");
            System.out.println("========================================");

            int choice = input.readIntInRange("Enter choice (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> handleResourceMenu();
                case 2 -> handleEventMenu();
                case 3 -> handleBookingMenu();
                case 4 -> handleMaintenanceMenu();
                case 5 -> handleReportMenu();
                case 6 -> {
                    System.out.println("\nThank you for using Campus Resource & Event Management System. Goodbye!");
                    running = false;
                }
            }
        }
    }

    // ==================== 1. RESOURCE MANAGEMENT ====================

    private void handleResourceMenu() {
        boolean back = false;
        while (!back) {
            printHeader("RESOURCE MANAGEMENT");
            System.out.println("1. Add Resource");
            System.out.println("2. View All Resources");
            System.out.println("3. Search Resource");
            System.out.println("4. Update Resource");
            System.out.println("5. Remove Resource");
            System.out.println("6. Back");
            System.out.println("----------------------------------------");

            int choice = input.readIntInRange("Enter choice (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> addResource();
                case 2 -> viewAllResources();
                case 3 -> searchResources();
                case 4 -> updateResource();
                case 5 -> removeResource();
                case 6 -> back = true;
            }
        }
    }

    private void addResource() {
        printSubHeader("ADD NEW RESOURCE");
        System.out.println("Select Resource Category:");
        System.out.println("1. Classroom");
        System.out.println("2. Computer Lab");
        System.out.println("3. Seminar Hall");
        System.out.println("4. Projector");
        System.out.println("5. Other Equipment");

        int typeChoice = input.readIntInRange("Enter category (1-5): ", 1, 5);
        ResourceType type = switch (typeChoice) {
            case 1 -> ResourceType.CLASSROOM;
            case 2 -> ResourceType.COMPUTER_LAB;
            case 3 -> ResourceType.SEMINAR_HALL;
            case 4 -> ResourceType.PROJECTOR;
            default -> ResourceType.OTHER_EQUIPMENT;
        };

        String id = input.readNonEmptyString("Enter Unique Resource ID (e.g. CR-201, LAB-301): ");
        String name = input.readNonEmptyString("Enter Resource Name: ");
        String location = input.readNonEmptyString("Enter Campus Location: ");

        try {
            Resource resource;
            if (type == ResourceType.CLASSROOM || type == ResourceType.SEMINAR_HALL) {
                int capacity = input.readInt("Enter Seating Capacity: ");
                boolean proj = input.readBoolean("Does it have an installed projector?");
                boolean ac = input.readBoolean("Does it have air conditioning?");
                resource = new Room(id, name, type, location, ResourceStatus.AVAILABLE, capacity, proj, ac);
            } else if (type == ResourceType.COMPUTER_LAB) {
                int capacity = input.readInt("Enter Capacity (Student Seats): ");
                int ws = input.readInt("Enter Number of Workstations: ");
                String sw = input.readNonEmptyString("Enter Software/OS Configuration: ");
                resource = new Laboratory(id, name, location, ResourceStatus.AVAILABLE, capacity, ws, sw);
            } else {
                String specs = input.readNonEmptyString("Enter Specifications / Model: ");
                boolean portable = input.readBoolean("Is it portable?");
                resource = new Equipment(id, name, type, location, ResourceStatus.AVAILABLE, specs, portable);
            }

            resourceService.addResource(resource);
            System.out.println("\n[SUCCESS] Resource successfully added: " + resource.getId());
        } catch (DuplicateRecordException | InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void viewAllResources() {
        printSubHeader("CAMPUS RESOURCES DIRECTORY");
        List<Resource> list = resourceService.getAllResources();
        if (list.isEmpty()) {
            System.out.println("No campus resources currently registered.");
        } else {
            System.out.printf("%-9s | %-28s | %-14s | %-22s | %-4s | %-16s | %s%n",
                    "ID", "NAME", "TYPE", "LOCATION", "CAP", "STATUS", "DETAILS");
            System.out.println("-".repeat(115));
            for (Resource r : list) {
                System.out.printf("%-9s | %-28s | %-14s | %-22s | %-4d | %-16s | %s%n",
                        r.getId(),
                        truncate(r.getName(), 28),
                        r.getType().getDisplayName(),
                        truncate(r.getLocation(), 22),
                        r.getCapacity(),
                        r.getStatus().getDescription(),
                        r.getSpecificDetails());
            }
        }
        input.waitForEnter();
    }

    private void searchResources() {
        printSubHeader("SEARCH RESOURCES");
        String query = input.readNonEmptyString("Enter search keyword (name, ID, location, or type): ");
        List<Resource> results = resourceService.searchResources(query);
        System.out.println("\nFound " + results.size() + " matching resource(s):");
        for (Resource r : results) {
            System.out.println(" -> " + r);
        }
        input.waitForEnter();
    }

    private void updateResource() {
        printSubHeader("UPDATE RESOURCE");
        String id = input.readNonEmptyString("Enter Resource ID to update: ");
        Resource resource = resourceService.getResourceById(id);
        if (resource == null) {
            System.out.println("[ERROR] Resource with ID '" + id + "' not found.");
            input.waitForEnter();
            return;
        }

        System.out.println("Current details: " + resource);
        String newName = input.readOptionalString("Enter New Name", resource.getName());
        String newLoc = input.readOptionalString("Enter New Location", resource.getLocation());
        int newCap = input.readInt("Enter New Capacity (" + resource.getCapacity() + "): ");

        try {
            resourceService.updateResource(id, newName, newLoc, newCap);
            System.out.println("\n[SUCCESS] Resource '" + id + "' updated successfully.");
        } catch (InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void removeResource() {
        printSubHeader("REMOVE RESOURCE");
        String id = input.readNonEmptyString("Enter Resource ID to remove: ");
        Resource resource = resourceService.getResourceById(id);
        if (resource == null) {
            System.out.println("[ERROR] Resource with ID '" + id + "' not found.");
            input.waitForEnter();
            return;
        }

        boolean confirm = input.readBoolean("Are you sure you want to remove '" + resource.getName() + "'?");
        if (confirm) {
            try {
                resourceService.removeResource(id);
                System.out.println("\n[SUCCESS] Resource removed.");
            } catch (InvalidInputException e) {
                System.out.println("\n[ERROR] " + e.getMessage());
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        input.waitForEnter();
    }

    // ==================== 2. EVENT MANAGEMENT ====================

    private void handleEventMenu() {
        boolean back = false;
        while (!back) {
            printHeader("EVENT MANAGEMENT");
            System.out.println("1. Create Event");
            System.out.println("2. View All Events");
            System.out.println("3. Update Event Details");
            System.out.println("4. Cancel Event");
            System.out.println("5. Register Participant");
            System.out.println("6. View Event Participants");
            System.out.println("7. Back");
            System.out.println("----------------------------------------");

            int choice = input.readIntInRange("Enter choice (1-7): ", 1, 7);
            switch (choice) {
                case 1 -> createEvent();
                case 2 -> viewAllEvents();
                case 3 -> updateEvent();
                case 4 -> cancelEvent();
                case 5 -> registerParticipant();
                case 6 -> viewEventParticipants();
                case 7 -> back = true;
            }
        }
    }

    private void createEvent() {
        printSubHeader("CREATE NEW EVENT");
        String id = input.readNonEmptyString("Enter Unique Event ID (e.g. EVT-201): ");
        String title = input.readNonEmptyString("Enter Event Title: ");
        String organizer = input.readNonEmptyString("Enter Organizing Body / Dept: ");
        LocalDateTime start = input.readDateTime("Enter Start Time");
        LocalDateTime end = input.readDateTime("Enter End Time");
        int attendees = input.readInt("Enter Expected Attendees: ");

        try {
            Event event = new Event(id, title, organizer, start, end, EventStatus.SCHEDULED, attendees);
            eventService.createEvent(event);
            System.out.println("\n[SUCCESS] Event created successfully: " + event.getId());
        } catch (DuplicateRecordException | InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void viewAllEvents() {
        printSubHeader("CAMPUS EVENTS SCHEDULE");
        List<Event> list = eventService.getAllEvents();
        if (list.isEmpty()) {
            System.out.println("No campus events registered.");
        } else {
            System.out.printf("%-8s | %-28s | %-20s | %-16s | %-16s | %-10s | %s%n",
                    "ID", "TITLE", "ORGANIZER", "START", "END", "STATUS", "REG/EXP");
            System.out.println("-".repeat(118));
            for (Event e : list) {
                System.out.printf("%-8s | %-28s | %-20s | %-16s | %-16s | %-10s | %d/%d%n",
                        e.getId(),
                        truncate(e.getTitle(), 28),
                        truncate(e.getOrganizer(), 20),
                        DateTimeUtil.formatDateTime(e.getStartDateTime()),
                        DateTimeUtil.formatDateTime(e.getEndDateTime()),
                        e.getStatus().getLabel(),
                        e.getParticipantCount(),
                        e.getExpectedAttendees());
            }
        }
        input.waitForEnter();
    }

    private void updateEvent() {
        printSubHeader("UPDATE EVENT");
        String id = input.readNonEmptyString("Enter Event ID to update: ");
        Event event = eventService.getEventById(id);
        if (event == null) {
            System.out.println("[ERROR] Event with ID '" + id + "' not found.");
            input.waitForEnter();
            return;
        }

        System.out.println("Current details: " + event);
        String title = input.readOptionalString("Enter New Title", event.getTitle());
        String organizer = input.readOptionalString("Enter New Organizer", event.getOrganizer());
        int attendees = input.readInt("Enter New Expected Attendees (" + event.getExpectedAttendees() + "): ");

        try {
            eventService.updateEvent(id, title, organizer, attendees);
            System.out.println("\n[SUCCESS] Event '" + id + "' updated successfully.");
        } catch (InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void cancelEvent() {
        printSubHeader("CANCEL EVENT");
        String id = input.readNonEmptyString("Enter Event ID to cancel: ");
        Event event = eventService.getEventById(id);
        if (event == null) {
            System.out.println("[ERROR] Event with ID '" + id + "' not found.");
            input.waitForEnter();
            return;
        }

        boolean confirm = input.readBoolean("Are you sure you want to cancel event '" + event.getTitle() + "'?\n(Notice: All linked active room bookings will be cancelled automatically)");
        if (confirm) {
            try {
                eventService.cancelEvent(id);
                System.out.println("\n[SUCCESS] Event and linked bookings cancelled successfully.");
            } catch (InvalidInputException e) {
                System.out.println("\n[ERROR] " + e.getMessage());
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
        input.waitForEnter();
    }

    private void registerParticipant() {
        printSubHeader("REGISTER PARTICIPANT FOR EVENT");
        String eventId = input.readNonEmptyString("Enter Event ID: ");
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            System.out.println("[ERROR] Event not found.");
            input.waitForEnter();
            return;
        }

        System.out.println("Registering for: " + event.getTitle() + " (" + event.getStatus().getLabel() + ")");
        String name = input.readNonEmptyString("Enter Participant Full Name: ");
        String email = input.readNonEmptyString("Enter Participant Email: ");
        String dept = input.readNonEmptyString("Enter Department / Class: ");

        try {
            eventService.registerParticipant(eventId, name, email, dept);
            System.out.println("\n[SUCCESS] Participant successfully registered for event " + eventId);
        } catch (DuplicateRecordException | InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void viewEventParticipants() {
        printSubHeader("VIEW EVENT PARTICIPANTS");
        String eventId = input.readNonEmptyString("Enter Event ID: ");
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            System.out.println("[ERROR] Event not found.");
            input.waitForEnter();
            return;
        }

        System.out.println("\nRegistered Attendees for '" + event.getTitle() + "': " + event.getParticipantCount());
        List<Participant> list = event.getParticipants();
        if (list.isEmpty()) {
            System.out.println("No participants registered yet.");
        } else {
            System.out.printf("%-9s | %-24s | %-28s | %-20s | %s%n",
                    "ID", "NAME", "EMAIL", "DEPARTMENT", "REGISTERED ON");
            System.out.println("-".repeat(95));
            for (Participant p : list) {
                System.out.printf("%-9s | %-24s | %-28s | %-20s | %s%n",
                        p.getParticipantId(),
                        truncate(p.getName(), 24),
                        truncate(p.getEmail(), 28),
                        truncate(p.getDepartment(), 20),
                        DateTimeUtil.formatDateTime(p.getRegisteredDate()));
            }
        }
        input.waitForEnter();
    }

    // ==================== 3. BOOKING MANAGEMENT ====================

    private void handleBookingMenu() {
        boolean back = false;
        while (!back) {
            printHeader("BOOKING MANAGEMENT");
            System.out.println("1. Create Booking");
            System.out.println("2. View All Bookings");
            System.out.println("3. Check Resource Availability");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Back");
            System.out.println("----------------------------------------");

            int choice = input.readIntInRange("Enter choice (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> createBooking();
                case 2 -> viewAllBookings();
                case 3 -> checkResourceAvailability();
                case 4 -> cancelBooking();
                case 5 -> back = true;
            }
        }
    }

    private void createBooking() {
        printSubHeader("CREATE RESOURCE BOOKING");
        String resourceId = input.readNonEmptyString("Enter Resource ID to book: ");
        Resource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            System.out.println("[ERROR] Resource not found.");
            input.waitForEnter();
            return;
        }

        System.out.println("Selected Resource: " + resource.getName() + " [Status: " + resource.getStatus().getDescription() + "]");
        String eventId = input.readOptionalString("Enter Linked Event ID (or press Enter if general booking)", "");
        String purpose = input.readNonEmptyString("Enter Booking Purpose / Agenda: ");
        LocalDateTime start = input.readDateTime("Enter Start Time");
        LocalDateTime end = input.readDateTime("Enter End Time");
        String bookedBy = input.readNonEmptyString("Enter Booked By (Name/Faculty ID): ");

        try {
            Booking booking = bookingService.createBooking(resourceId, eventId, purpose, start, end, bookedBy);
            System.out.println("\n[SUCCESS] Booking confirmed! Booking ID: " + booking.getId());
            System.out.println("Resource '" + resource.getName() + "' is reserved from " +
                    DateTimeUtil.formatDateTime(start) + " to " + DateTimeUtil.formatDateTime(end));
        } catch (BookingConflictException | ResourceUnderMaintenanceException |
                 ResourceNotAvailableException | InvalidInputException e) {
            System.out.println("\n[BOOKING REJECTED] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void viewAllBookings() {
        printSubHeader("CAMPUS RESOURCE BOOKINGS");
        List<Booking> list = bookingService.getAllBookings();
        if (list.isEmpty()) {
            System.out.println("No booking records on file.");
        } else {
            System.out.printf("%-9s | %-10s | %-9s | %-16s | %-16s | %-10s | %-18s | %s%n",
                    "ID", "RESOURCE", "EVENT", "START", "END", "STATUS", "BOOKED BY", "PURPOSE");
            System.out.println("-".repeat(110));
            for (Booking b : list) {
                System.out.printf("%-9s | %-10s | %-9s | %-16s | %-16s | %-10s | %-18s | %s%n",
                        b.getId(),
                        b.getResourceId(),
                        (b.getEventId() != null ? b.getEventId() : "-"),
                        DateTimeUtil.formatDateTime(b.getStartDateTime()),
                        DateTimeUtil.formatDateTime(b.getEndDateTime()),
                        b.getStatus().getLabel(),
                        truncate(b.getBookedBy(), 18),
                        truncate(b.getPurpose(), 25));
            }
        }
        input.waitForEnter();
    }

    private void checkResourceAvailability() {
        printSubHeader("CHECK RESOURCE AVAILABILITY");
        String resourceId = input.readNonEmptyString("Enter Resource ID: ");
        Resource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            System.out.println("[ERROR] Resource not found.");
            input.waitForEnter();
            return;
        }

        LocalDateTime start = input.readDateTime("Enter Desired Start Time");
        LocalDateTime end = input.readDateTime("Enter Desired End Time");

        if (!DateTimeUtil.isValidInterval(start, end)) {
            System.out.println("[ERROR] Start time must be strictly before end time.");
            input.waitForEnter();
            return;
        }

        boolean available = bookingService.isResourceAvailable(resourceId, start, end);
        if (available) {
            System.out.println("\n[AVAILABLE] Resource '" + resource.getName() +
                    "' is completely FREE and available for booking during this slot.");
        } else {
            System.out.println("\n[UNAVAILABLE] Resource '" + resource.getName() +
                    "' is NOT available for the requested slot.");
            if (resource.getStatus() == ResourceStatus.UNDER_MAINTENANCE) {
                System.out.println("Reason: Resource is currently UNDER MAINTENANCE.");
            } else {
                Booking conflict = bookingService.findConflictingBooking(resourceId, start, end);
                if (conflict != null) {
                    System.out.println("Reason: Overlapping booking found -> ID: " + conflict.getId() +
                            " booked by '" + conflict.getBookedBy() + "' (" +
                            DateTimeUtil.formatDateTime(conflict.getStartDateTime()) + " to " +
                            DateTimeUtil.formatDateTime(conflict.getEndDateTime()) + ")");
                }
            }
        }
        input.waitForEnter();
    }

    private void cancelBooking() {
        printSubHeader("CANCEL BOOKING");
        String id = input.readNonEmptyString("Enter Booking ID to cancel: ");
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            System.out.println("[ERROR] Booking with ID '" + id + "' not found.");
            input.waitForEnter();
            return;
        }

        boolean confirm = input.readBoolean("Confirm cancellation of booking '" + id + "' (" + booking.getPurpose() + ")?");
        if (confirm) {
            try {
                bookingService.cancelBooking(id);
                System.out.println("\n[SUCCESS] Booking '" + id + "' marked as CANCELLED. Resource slot released.");
            } catch (InvalidInputException e) {
                System.out.println("\n[ERROR] " + e.getMessage());
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
        input.waitForEnter();
    }

    // ==================== 4. MAINTENANCE MANAGEMENT ====================

    private void handleMaintenanceMenu() {
        boolean back = false;
        while (!back) {
            printHeader("MAINTENANCE MANAGEMENT");
            System.out.println("1. Report Damaged Resource");
            System.out.println("2. Update Maintenance Status");
            System.out.println("3. View Pending Maintenance");
            System.out.println("4. View Maintenance History");
            System.out.println("5. Back");
            System.out.println("----------------------------------------");

            int choice = input.readIntInRange("Enter choice (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> reportDamagedResource();
                case 2 -> updateMaintenanceStatus();
                case 3 -> viewPendingMaintenance();
                case 4 -> viewMaintenanceHistory();
                case 5 -> back = true;
            }
        }
    }

    private void reportDamagedResource() {
        printSubHeader("REPORT DAMAGED RESOURCE");
        String resourceId = input.readNonEmptyString("Enter Resource ID needing maintenance: ");
        String issue = input.readNonEmptyString("Describe the issue or defect: ");
        String reporter = input.readNonEmptyString("Enter Reporter Name / Role: ");

        try {
            MaintenanceRecord record = maintenanceService.reportIssue(resourceId, issue, reporter);
            System.out.println("\n[SUCCESS] Maintenance Ticket created: " + record.getId());
            System.out.println("Resource status has been updated to: UNDER_MAINTENANCE");
            System.out.println("(Notice: No new bookings can be made for this resource until resolved)");
        } catch (InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void updateMaintenanceStatus() {
        printSubHeader("UPDATE MAINTENANCE TICKET");
        String id = input.readNonEmptyString("Enter Maintenance Record ID (e.g. MNT-501): ");
        MaintenanceRecord record = maintenanceService.getRecordById(id);
        if (record == null) {
            System.out.println("[ERROR] Ticket with ID '" + id + "' not found.");
            input.waitForEnter();
            return;
        }

        System.out.println("Current status: " + record.getStatus().getLabel() + " | Resource: " + record.getResourceId());
        System.out.println("Select New Status:");
        System.out.println("1. PENDING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. RESOLVED (Restores resource to AVAILABLE)");

        int sChoice = input.readIntInRange("Enter choice (1-3): ", 1, 3);
        MaintenanceStatus newStatus = switch (sChoice) {
            case 1 -> MaintenanceStatus.PENDING;
            case 2 -> MaintenanceStatus.IN_PROGRESS;
            default -> MaintenanceStatus.RESOLVED;
        };

        LocalDate resolvedDate = null;
        if (newStatus == MaintenanceStatus.RESOLVED) {
            resolvedDate = LocalDate.now();
        }

        try {
            maintenanceService.updateMaintenanceStatus(id, newStatus, resolvedDate);
            System.out.println("\n[SUCCESS] Maintenance ticket updated to: " + newStatus.getLabel());
            if (newStatus == MaintenanceStatus.RESOLVED) {
                System.out.println("Resource '" + record.getResourceId() + "' restored to AVAILABLE status for bookings.");
            }
        } catch (InvalidInputException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        input.waitForEnter();
    }

    private void viewPendingMaintenance() {
        printSubHeader("PENDING / IN-PROGRESS MAINTENANCE");
        List<MaintenanceRecord> pending = maintenanceService.getPendingRecords();
        if (pending.isEmpty()) {
            System.out.println("All campus resources are healthy! No pending maintenance issues.");
        } else {
            printMaintenanceTable(pending);
        }
        input.waitForEnter();
    }

    private void viewMaintenanceHistory() {
        printSubHeader("MAINTENANCE LOG & HISTORY");
        List<MaintenanceRecord> list = maintenanceService.getAllRecords();
        if (list.isEmpty()) {
            System.out.println("No maintenance logs on file.");
        } else {
            printMaintenanceTable(list);
        }
        input.waitForEnter();
    }

    private void printMaintenanceTable(List<MaintenanceRecord> records) {
        System.out.printf("%-8s | %-10s | %-12s | %-12s | %-12s | %-18s | %s%n",
                "ID", "RESOURCE", "STATUS", "REPORTED", "RESOLVED", "REPORTED BY", "ISSUE");
        System.out.println("-".repeat(110));
        for (MaintenanceRecord m : records) {
            System.out.printf("%-8s | %-10s | %-12s | %-12s | %-12s | %-18s | %s%n",
                    m.getId(),
                    m.getResourceId(),
                    m.getStatus().getLabel(),
                    DateTimeUtil.formatDate(m.getReportedDate()),
                    m.getResolvedDate() != null ? DateTimeUtil.formatDate(m.getResolvedDate()) : "Pending",
                    truncate(m.getReportedBy(), 18),
                    truncate(m.getIssueDescription(), 30));
        }
    }

    // ==================== 5. REPORTS & UTILIZATION ====================

    private void handleReportMenu() {
        boolean back = false;
        while (!back) {
            printHeader("REPORTS & UTILIZATION METRICS");
            System.out.println("1. View Comprehensive Audit Report (Console)");
            System.out.println("2. View Resource Utilization & Health");
            System.out.println("3. View Upcoming Events Schedule");
            System.out.println("4. Export Report to File (Multithreaded Background Task)");
            System.out.println("5. Back");
            System.out.println("----------------------------------------");

            int choice = input.readIntInRange("Enter choice (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> {
                    System.out.println();
                    System.out.println(reportService.generateFullReport());
                    input.waitForEnter();
                }
                case 2 -> {
                    printSubHeader("RESOURCE UTILIZATION SUMMARY");
                    List<Resource> all = resourceService.getAllResources();
                    long avail = all.stream().filter(Resource::isAvailable).count();
                    long underMnt = all.stream().filter(r -> r.getStatus() == ResourceStatus.UNDER_MAINTENANCE).count();
                    System.out.printf("Total Campus Resources : %d%n", all.size());
                    System.out.printf("Available Capacity     : %d%n", avail);
                    System.out.printf("Under Maintenance      : %d%n", underMnt);
                    input.waitForEnter();
                }
                case 3 -> {
                    printSubHeader("UPCOMING EVENTS");
                    List<Event> upcoming = eventService.getUpcomingEvents();
                    if (upcoming.isEmpty()) {
                        System.out.println("No upcoming events scheduled.");
                    } else {
                        for (Event e : upcoming) {
                            System.out.printf(" * [%s] %s | Starts: %s | Attendees: %d%n",
                                    e.getId(), e.getTitle(),
                                    DateTimeUtil.formatDateTime(e.getStartDateTime()),
                                    e.getParticipantCount());
                        }
                    }
                    input.waitForEnter();
                }
                case 4 -> exportReportMultithreaded();
                case 5 -> back = true;
            }
        }
    }

    /**
     * Demonstrates Multithreading:
     * Generates and writes the audit report in a dedicated background worker thread
     * so that the main UI remains non-blocking.
     */
    private void exportReportMultithreaded() {
        printSubHeader("MULTITHREADED REPORT EXPORTER");
        String filename = input.readOptionalString("Enter destination filename/path", "data/campus_report.txt");

        System.out.println("\n[MULTITHREADING] Spawning background worker thread to compile statistics...");
        reportService.exportReportAsync(filename,
                path -> System.out.println("\n[THREAD SUCCESS] Report exported asynchronously to: " + path),
                err -> System.out.println("\n[THREAD ERROR] Failed to export report: " + err.getMessage()));

        System.out.println("[MAIN THREAD] Main CLI loop continues running uninterrupted while thread executes!");
        input.waitForEnter();
    }

    // ==================== HELPER METHODS ====================

    private void printHeader(String title) {
        System.out.println();
        System.out.println("=".repeat(48));
        System.out.printf("   %s%n", title);
        System.out.println("=".repeat(48));
    }

    private void printSubHeader(String title) {
        System.out.println();
        System.out.println("-".repeat(48));
        System.out.printf(" >>> %s%n", title);
        System.out.println("-".repeat(48));
    }

    private String truncate(String str, int maxLen) {
        if (str == null) return "";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }
}

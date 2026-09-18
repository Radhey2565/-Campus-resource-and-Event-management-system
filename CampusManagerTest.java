package com.campusmanager;

import com.campusmanager.exception.*;
import com.campusmanager.model.*;
import com.campusmanager.repository.DataStore;
import com.campusmanager.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 Test Suite for Campus Resource & Event Management System.
 * Tests business rules, validation constraints, and exception handling.
 */
class CampusManagerTest {

    @TempDir
    Path tempDir;

    private DataStore dataStore;
    private ResourceService resourceService;
    private EventService eventService;
    private BookingService bookingService;
    private MaintenanceService maintenanceService;
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore(tempDir.toString());
        resourceService = new ResourceService(dataStore);
        eventService = new EventService(dataStore);
        bookingService = new BookingService(dataStore);
        maintenanceService = new MaintenanceService(dataStore);
        reportService = new ReportService(dataStore);
    }

    // ==================== RESOURCE TESTS ====================

    @Test
    @DisplayName("Test 1: Resource Creation and Polymorphic Subclasses")
    void testResourceCreation() throws Exception {
        Room room = new Room("CR-301", "Math Seminar Room", ResourceType.CLASSROOM,
                "Block C", ResourceStatus.AVAILABLE, 50, true, true);
        resourceService.addResource(room);

        Resource retrieved = resourceService.getResourceById("CR-301");
        assertNotNull(retrieved);
        assertEquals("Math Seminar Room", retrieved.getName());
        assertEquals(50, retrieved.getCapacity());
        assertTrue(retrieved.isAvailable());
        assertTrue(retrieved.getSpecificDetails().contains("Projector: Yes"));
    }

    @Test
    @DisplayName("Test 2: Duplicate Resource ID Detection")
    void testDuplicateResourceDetection() throws Exception {
        Room r1 = new Room("LAB-99", "AI Lab 1", ResourceType.COMPUTER_LAB, "Block B", ResourceStatus.AVAILABLE, 30, false, true);
        resourceService.addResource(r1);

        Room r2 = new Room("LAB-99", "AI Lab 2", ResourceType.COMPUTER_LAB, "Block B", ResourceStatus.AVAILABLE, 40, true, true);

        assertThrows(DuplicateRecordException.class, () -> resourceService.addResource(r2),
                "Adding resource with duplicate ID must throw DuplicateRecordException");
    }

    // ==================== EVENT TESTS ====================

    @Test
    @DisplayName("Test 3: Event Creation and Time Validation")
    void testEventCreation() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withHour(12).withMinute(0);

        Event event = new Event("EVT-901", "Hackathon Opening Ceremony", "CS Dept",
                start, end, EventStatus.SCHEDULED, 120);
        eventService.createEvent(event);

        Event retrieved = eventService.getEventById("EVT-901");
        assertNotNull(retrieved);
        assertEquals(120, retrieved.getExpectedAttendees());
        assertEquals(EventStatus.SCHEDULED, retrieved.getStatus());
    }

    @Test
    @DisplayName("Test 4: Invalid Event Time Interval (Start >= End)")
    void testEventInvalidTimeInterval() {
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withHour(12).withMinute(0); // Before start!

        Event invalidEvent = new Event("EVT-902", "Invalid Timing Meet", "Club", start, end, EventStatus.SCHEDULED, 20);

        assertThrows(InvalidInputException.class, () -> eventService.createEvent(invalidEvent),
                "Start time after end time must throw InvalidInputException");
    }

    @Test
    @DisplayName("Test 5: Duplicate Participant Registration Prevention")
    void testDuplicateParticipantPrevention() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = start.plusHours(2);
        Event event = new Event("EVT-903", "Robotics Demo", "RoboClub", start, end, EventStatus.SCHEDULED, 40);
        eventService.createEvent(event);

        // Register first time - must succeed
        eventService.registerParticipant("EVT-903", "John Doe", "john.doe@campus.edu", "Computer Science");
        assertEquals(1, event.getParticipantCount());

        // Register same email second time - must throw DuplicateRecordException
        assertThrows(DuplicateRecordException.class, () ->
                eventService.registerParticipant("EVT-903", "John Doe", "john.doe@campus.edu", "Computer Science"),
                "Registering identical email for same event must throw DuplicateRecordException");
    }

    @Test
    @DisplayName("Test 6: Event Cancellation Cascades to Active Bookings")
    void testEventCancellationCascades() throws Exception {
        Room room = new Room("HALL-C", "Conference Hall C", ResourceType.SEMINAR_HALL, "Admin Block", ResourceStatus.AVAILABLE, 200, true, true);
        resourceService.addResource(room);

        LocalDateTime start = LocalDateTime.now().plusDays(5).withHour(9).withMinute(0);
        LocalDateTime end = LocalDateTime.now().plusDays(5).withHour(13).withMinute(0);
        Event event = new Event("EVT-904", "Alumni Reunion", "Dean Office", start, end, EventStatus.SCHEDULED, 150);
        eventService.createEvent(event);

        Booking booking = bookingService.createBooking("HALL-C", "EVT-904", "Alumni Setup", start, end, "Alumni Team");
        assertTrue(booking.isActive());

        // Cancel event
        eventService.cancelEvent("EVT-904");
        assertEquals(EventStatus.CANCELLED, event.getStatus());

        // Booking must be automatically cancelled
        Booking updatedBooking = bookingService.getBookingById(booking.getId());
        assertEquals(BookingStatus.CANCELLED, updatedBooking.getStatus());
    }

    // ==================== BOOKING & CONFLICT TESTS ====================

    @Test
    @DisplayName("Test 7: Booking Creation and Availability Check")
    void testBookingCreationAndAvailability() throws Exception {
        Room room = new Room("CR-401", "Classroom 401", ResourceType.CLASSROOM, "Block D", ResourceStatus.AVAILABLE, 45, true, false);
        resourceService.addResource(room);

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(11).withMinute(30);

        assertTrue(bookingService.isResourceAvailable("CR-401", start, end));

        Booking booking = bookingService.createBooking("CR-401", null, "Guest Talk", start, end, "Prof. Roy");
        assertNotNull(booking);
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());

        // Now resource should not be available during that exact slot
        assertFalse(bookingService.isResourceAvailable("CR-401", start, end));
    }

    @Test
    @DisplayName("Test 8: Booking Conflict Detection (Overlapping Slot)")
    void testBookingConflictDetection() throws Exception {
        Room room = new Room("CR-501", "Hall 501", ResourceType.CLASSROOM, "Block E", ResourceStatus.AVAILABLE, 60, true, true);
        resourceService.addResource(room);

        LocalDateTime slot1Start = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
        LocalDateTime slot1End = LocalDateTime.now().plusDays(2).withHour(12).withMinute(0);

        // First booking: 10:00 to 12:00
        bookingService.createBooking("CR-501", null, "First Lecture", slot1Start, slot1End, "Dr. Adams");

        // Overlapping booking: 11:00 to 13:00 (overlaps by 1 hour)
        LocalDateTime slot2Start = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0);
        LocalDateTime slot2End = LocalDateTime.now().plusDays(2).withHour(13).withMinute(0);

        assertThrows(BookingConflictException.class, () ->
                bookingService.createBooking("CR-501", null, "Conflicting Slot", slot2Start, slot2End, "Dr. Baker"),
                "Overlapping booking must throw BookingConflictException");
    }

    @Test
    @DisplayName("Test 9: Prevent Booking Resource Under Maintenance")
    void testBookingUnderMaintenanceResource() throws Exception {
        Equipment prj = new Equipment("PRJ-88", "Hitachi Projector", ResourceType.PROJECTOR, "AV Lab", ResourceStatus.AVAILABLE, "4000 Lumens", true);
        resourceService.addResource(prj);

        // Report maintenance
        maintenanceService.reportIssue("PRJ-88", "Lens bulb flickering", "Lab Assistant");
        assertEquals(ResourceStatus.UNDER_MAINTENANCE, prj.getStatus());

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(11).withMinute(0);

        assertThrows(ResourceUnderMaintenanceException.class, () ->
                bookingService.createBooking("PRJ-88", null, "Presentation", start, end, "Student Club"),
                "Booking a resource under maintenance must throw ResourceUnderMaintenanceException");
    }

    // ==================== MAINTENANCE & REPORT TESTS ====================

    @Test
    @DisplayName("Test 10: Maintenance Resolution Restores Resource Availability")
    void testMaintenanceResolution() throws Exception {
        Laboratory lab = new Laboratory("LAB-505", "Networks Lab", "Tech Block", ResourceStatus.AVAILABLE, 35, 35, "Cisco Packet Tracer");
        resourceService.addResource(lab);

        MaintenanceRecord record = maintenanceService.reportIssue("LAB-505", "Switch port error", "NetAdmin");
        assertEquals(ResourceStatus.UNDER_MAINTENANCE, lab.getStatus());

        // Resolve maintenance ticket
        maintenanceService.updateMaintenanceStatus(record.getId(), MaintenanceStatus.RESOLVED, LocalDate.now());
        assertEquals(ResourceStatus.AVAILABLE, lab.getStatus(),
                "Resolving maintenance ticket must restore resource status to AVAILABLE");
    }

    @Test
    @DisplayName("Test 11: Report Generation and File Export")
    void testReportGenerationAndExport() throws Exception {
        Room r = new Room("CR-888", "Room 888", ResourceType.CLASSROOM, "Wing B", ResourceStatus.AVAILABLE, 30, false, false);
        resourceService.addResource(r);

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(11).withMinute(0);
        bookingService.createBooking("CR-888", null, "Audit Test Booking", start, end, "Auditor");

        String report = reportService.generateFullReport();
        assertNotNull(report);
        assertTrue(report.contains("CAMPUS RESOURCE & EVENT MANAGEMENT SYSTEM"));
        assertTrue(report.contains("CR-888"));
        assertTrue(report.contains("Total Registered Resources : 1"));

        // Test export to file
        File exportFile = new File(tempDir.toFile(), "test_report.txt");
        reportService.exportReportSync(exportFile.getAbsolutePath());
        assertTrue(exportFile.exists());
        assertTrue(exportFile.length() > 0);
    }
}

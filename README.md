# Campus Resource & Event Management System (CREMS)
> **A Comprehensive Object-Oriented Command-Line Java Application for College Resource Planning and Event Coordination**  
> *Academic Course Evaluation Project for Programming in Java (Undergraduate)*

---

## 1. Project Abstract & Overview

Higher education campuses contain hundreds of physical assets—ranging from lecture halls and high-performance computing labs to portable multimedia projectors and sound systems. Managing these across diverse departments frequently results in double-bookings, scheduling disputes, untracked equipment damage, and chaotic event registrations.

The **Campus Resource & Event Management System (CREMS)** is a lightweight, reliable, pure Java desktop command-line application engineered to streamline campus facility scheduling. Built without bloated enterprise frameworks, CREMS showcases clean Software Engineering paradigms, Core Java APIs, Object-Oriented Design (Polymorphism, Inheritance, Encapsulation, Abstraction), robust Checked Exceptions, Date/Time interval arithmetic, Stream aggregations, non-blocking Multithreading, and CSV file persistence.

---

## 2. Key Features

### 🏢 Resource Management Module
* **Polymorphic Asset Hierarchy**: Model distinct physical assets (`Room`, `Laboratory`, `Equipment`) extending an abstract `Resource` base class and implementing the `Bookable` interface.
* **Specialized Domain Metadata**: Classrooms track projectors and air conditioning; Computer Labs track workstation counts and installed software stacks; Equipment tracks technical specs and portability.
* **CRUD & Dynamic Search**: Add, browse, update, remove, and search resources by ID, name, location, or equipment specifications.

### 📅 Event Management Module
* **Lifecycle State Tracking**: Schedule, monitor, and update events through states: `SCHEDULED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`.
* **Unique Participant Registration**: Enroll attendees with email deduplication (ensuring a student cannot be registered twice for the same event).
* **Cascading Cancellation**: Cancelling an event automatically cancels all associated resource reservations, instantly releasing slots for other campus users.

### ⏰ Booking & Conflict Detection Engine
* **Interval Overlap Arithmetic**: Automated detection of overlapping time slots using the `java.time` API ($Start_A < End_B \land End_A > Start_B$).
* **Maintenance & Health Lock**: Immediate rejection of booking requests targeting resources flagged as `UNDER_MAINTENANCE` or `DECOMMISSIONED`.
* **Interactive Availability Prober**: Check whether a resource is free during any custom time window before committing a reservation.

### 🔧 Maintenance & Health Tracking Module
* **Defect Incident Ticketing**: Log equipment malfunctions with issue details, reporter name, and status (`PENDING`, `IN_PROGRESS`, `RESOLVED`).
* **Automated State Transition**: Filing a defect automatically transitions the resource status to `UNDER_MAINTENANCE`.
* **Automated Recovery**: Resolving the defect restores the resource status to `AVAILABLE` (if no other pending tickets exist).

### 📊 Analytics & Multithreaded Reporting
* **Stream API Aggregation**: Real-time statistical audits grouping resources by category, computing utilization rates, and identifying the top 5 most frequently booked resources.
* **Asynchronous Background Export**: Dispatches audit report compilation and file writing to a dedicated worker thread (`ReportGenerator-Thread`), ensuring zero UI lag or freezing on the main terminal.

---

## 3. Technology Stack & Prerequisites

* **Language**: Java 17 LTS (Pure Standard Java SE)
* **Build System**: Apache Maven 3.8+
* **Unit Testing**: JUnit 5 Jupiter (`5.9.2`)
* **Persistence**: Pure File I/O (`java.io.BufferedReader`, `java.io.PrintWriter`) storing delimited CSV records
* **Dependencies**: Zero external runtime libraries or network SDKs. Runs entirely offline and locally.

---

## 4. Architecture & Design Principles

The application adopts a **Tiered Layered Architecture** with strict Separation of Concerns:

```
src/main/java/com/campusmanager/
├── Main.java               # Application bootstrap & dependency wiring
├── exception/              # Custom checked domain exception hierarchy
│   ├── CampusManagementException.java
│   ├── ResourceNotAvailableException.java
│   ├── BookingConflictException.java
│   ├── ResourceUnderMaintenanceException.java
│   ├── DuplicateRecordException.java
│   └── InvalidInputException.java
├── model/                  # Domain POJOs, interfaces, and enums
│   ├── Bookable.java       # Interface
│   ├── Resource.java       # Abstract Base
│   ├── Room.java           # Subclass
│   ├── Laboratory.java     # Subclass
│   ├── Equipment.java      # Subclass
│   ├── Event.java, Booking.java, Participant.java, MaintenanceRecord.java
│   └── ResourceType.java, ResourceStatus.java, BookingStatus.java, ...
├── repository/             # Data persistence & CSV serialization engine
│   └── DataStore.java
├── service/                # Business logic & conflict verification
│   ├── ResourceService.java
│   ├── EventService.java
│   ├── BookingService.java
│   ├── MaintenanceService.java
│   └── ReportService.java
├── ui/                     # Terminal presentation & user interaction
│   └── MenuController.java
└── util/                   # Reusable helpers & guards
    ├── DateTimeUtil.java   # Date/Time formatting and overlap math
    └── InputReader.java    # Type-safe Scanner input reader
```

### Applied Java & OOP Concepts
| Concept | Concrete Project Implementation |
| :--- | :--- |
| **Abstraction & Interfaces** | `Bookable` interface declares resource contracts; `Resource` is an abstract class with abstract method `getSpecificDetails()`. |
| **Inheritance & Polymorphism** | `Room`, `Laboratory`, and `Equipment` extend `Resource`, providing category-specific logic rendered polymorphically. |
| **Encapsulation** | All entity fields are `private` with validated getters/setters and defensive copying. |
| **Enums with Fields** | `ResourceType`, `ResourceStatus`, `BookingStatus`, `EventStatus`, and `MaintenanceStatus` encapsulate human-readable labels. |
| **Collections Framework** | `Map<String, Resource>`, `Map<String, Event>`, `List<Participant>`, `PriorityQueue`/`SortedSet` patterns. |
| **Java 8+ Stream API** | Used in `ReportService` for filtering, category grouping (`Collectors.groupingBy`), sorting, and computing frequencies. |
| **Modern Date/Time API** | `java.time.LocalDateTime` and `LocalDate` handle ISO-8601 timestamps and mathematical overlap validation. |
| **Checked Exceptions** | `CampusManagementException` base class with typed subclasses for conflicts, maintenance locks, and invalid data. |
| **Multithreading** | Asynchronous background daemon thread in `ReportService.exportReportAsync` prevents terminal I/O blocking. |
| **File I/O Persistence** | Delimited CSV file storage with safe auto-flushing and parsing. |

---

## 5. Directory & File Structure

```
├── pom.xml                               # Maven Project Object Model
├── statement.md                          # Problem Statement & System Specifications
├── README.md                             # Comprehensive Documentation & Viva Guide
├── data/                                 # CSV Data Persistence Directory
│   ├── resources.csv                     # Campus rooms, labs, and equipment
│   ├── events.csv                        # Scheduled campus events
│   ├── bookings.csv                      # Resource reservations
│   ├── maintenance.csv                   # Equipment defect reports
│   └── participants.csv                  # Event participant registrations
├── docs/                                 # Architectural & UML Diagrams
│   ├── use-case-diagram.md               # User interactions & functional scope
│   ├── class-diagram.md                  # OOP class hierarchy & associations
│   ├── sequence-diagram-booking.md       # Booking validation & conflict flow
│   ├── activity-workflow-diagram.md      # Application lifecycle & state transitions
│   └── system-architecture.md            # Tiered layer architecture
├── src/
│   ├── main/java/com/campusmanager/      # Main Application Source Code
│   └── test/java/com/campusmanager/      # JUnit 5 Automated Test Suite
```

---

## 6. How to Build & Run

### Prerequisites
* JDK 17 or higher installed (`java -version`, `javac -version`)
* Apache Maven installed (`mvn -version`)

### Compilation & Testing
```bash
# 1. Clean and compile all Java classes
mvn clean compile

# 2. Run the automated JUnit 5 test suite (11 test cases covering all business constraints)
mvn test

# 3. Package the project into an executable JAR file
mvn package
```

### Running the Application
You can run the application directly using Maven or using the compiled standalone JAR:

```bash
# Option A: Run via Maven Exec plugin
mvn exec:java -Dexec.mainClass="com.campusmanager.Main"

# Option B: Run via Standalone Executable JAR (Recommended)
java -jar target/campus-resource-event-manager-1.0.0.jar data
```

---

## 7. Sample Initial Dataset

The repository comes pre-loaded with sample campus data in the `data/` folder:
* **8 Resources**:
  * `CR-101`: Lecture Hall 101 (Classroom, 60 capacity, Projector & AC)
  * `CR-102`: Classroom 102 (Classroom, 40 capacity)
  * `LAB-201`: Alan Turing Computing Lab (Computer Lab, 45 Workstations, Ubuntu/Win11)
  * `LAB-202`: VLSI & Embedded Systems Lab (30 Workstations)
  * `HALL-A`: Dr. APJ Abdul Kalam Auditorium (Seminar Hall, 350 capacity)
  * `PRJ-01`: Epson PowerLite 1080p (Projector, currently **Under Maintenance**)
  * `PRJ-02`: BenQ Conference Projector (Projector, Available)
  * `EQP-01`: Yamaha StagePas PA System (Portable Sound System)
* **5 Events**: Annual Tech Symposium 2026, Python AI Workshop, Cyber Security Lecture, etc.
* **5 Bookings**: Active reservations for classes and symposium setup.
* **3 Maintenance Records**: Including lamp burn-out on `PRJ-01` and fan vibration on `CR-101`.

---

## 8. Viva & Evaluation Demonstration Guide

When presenting this project to an evaluator or viva examiner, follow this guided flow:

### 1. Demonstrate Polymorphism & Inheritance
* Navigate to **Resource Management** (`Option 1`) $\rightarrow$ **View All Resources** (`Option 2`).
* Explain to the examiner that all resources are stored in a unified `Collection<Resource>`, but dynamic method dispatch invokes `getSpecificDetails()` on `Room`, `Laboratory`, or `Equipment` to display custom properties (e.g., Workstation count vs. Lumens).

### 2. Demonstrate Booking Conflict Prevention
* Navigate to **Booking Management** (`Option 3`) $\rightarrow$ **Create New Booking** (`Option 1`).
* Try booking resource `CR-101` for `2026-10-15 09:30` to `2026-10-15 11:30`.
* The system will throw and catch `BookingConflictException`, gracefully informing the user:
  `[BOOKING REJECTED] Conflict detected with Booking BK-1001: 2026-10-15 09:00 to 2026-10-15 12:00`.

### 3. Demonstrate Maintenance Status Restriction
* Try to book resource `PRJ-01` (which is under maintenance for a burnt lamp).
* The system rejects the reservation with `ResourceUnderMaintenanceException`:
  `[BOOKING REJECTED] Resource PRJ-01 is currently UNDER MAINTENANCE.`

### 4. Demonstrate Dynamic Maintenance Resolution
* Navigate to **Maintenance Management** (`Option 4`) $\rightarrow$ **Update Maintenance Status** (`Option 3`).
* Select ticket `MNT-501` and mark it `RESOLVED`.
* Observe that resource `PRJ-01` automatically transitions back to `AVAILABLE`. Now attempt to book `PRJ-01`—it succeeds immediately!

### 5. Demonstrate Multithreaded Asynchronous Report Export
* Navigate to **Reports & Analytics** (`Option 5`) $\rightarrow$ **Export Report to File** (`Option 4`).
* Notice that the system immediately returns control to the interactive menu while a background worker thread (`ReportGenerator-Thread`) writes the audit report to `data/campus_audit_report.txt` and notifies upon completion.

---

## 9. Edge Cases & Robustness Handled

1. **Terminal Non-Crash Guarantee**: Typing alphabetic characters into integer/date prompts triggers friendly validation messages rather than unhandled `InputMismatchException` or `NumberFormatException`.
2. **Time Paradox Prevention**: Booking or event start times must precede end times ($Start < End$).
3. **No Duplicate Resource IDs**: Attempting to add an existing Resource ID throws `DuplicateRecordException`.
4. **Duplicate Attendee Guard**: Prevents the same student email from being registered twice for a single event.
5. **Cascading Integrity**: Cancelling an event cascades and marks associated resource bookings as cancelled.
6. **Graceful Stream EOF**: Exiting or piping inputs terminates cleanly without `NoSuchElementException`.

---

## 10. Future Extensibility

* Adding a lightweight SQLite/H2 embedded database layer using JDBC.
* Incorporating role-based user authentication (Student vs. Faculty vs. Admin).
* Exporting timetable calendar schedules to `.ics` format for Google Calendar/Outlook import.

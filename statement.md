# Campus Resource & Event Management System (CREMS)
## Problem Statement & System Specification Document

---

### 1. Project Title
**Campus Resource & Event Management System (CREMS)**  
*Course Evaluation Project for Programming in Java*

---

### 2. Problem Statement
Higher education institutions manage hundreds of physical resources across diverse departments—including lecture halls, computer labs, multimedia seminar auditoriums, portable projectors, and audio systems. Currently, many campus offices coordinate resource allocations through ad-hoc logbooks, emails, or fragmented spreadsheets. This causes frequent scheduling collisions, duplicate room bookings during symposiums, failure to track damaged equipment, and confusion regarding room occupancy. There is a strong need for a centralized, reliable, and lightweight desktop/CLI management tool that college staff and student societies can run locally without complex infrastructure.

---

### 3. Proposed Solution
The proposed solution is a modular, command-line Java application built with standard Object-Oriented Programming (OOP) principles and file persistence. The system enables administrative coordinators to:
1. Catalog campus rooms, computer labs, auditoriums, and portable equipment.
2. Schedule events and track attendees without double-registrations.
3. Reserve resources with strict automated validation preventing double-booking and forbidding bookings on damaged equipment.
4. Log maintenance complaints and dynamically isolate defective equipment until certified repaired.
5. Generate analytical utilization audits and asynchronous background reports.

---

### 4. Target Users
* **Academic Department Administrators**: Scheduling lecture halls and departmental seminars.
* **Campus Facilities & IT Support Team**: Managing hardware equipment and logging maintenance.
* **Student Activity Councils & Club Coordinators**: Registering campus events and participants.
* **Course Evaluators / Academic Viva Examiners**: Reviewing Java OOP paradigms and clean software design.

---

### 5. Main Modules
1. **Resource Management Module**: CRUD operations for Rooms, Labs, and Equipment with category-specific attributes.
2. **Event Management Module**: Lifecycle tracking (Scheduled, In-Progress, Cancelled) and attendee registration.
3. **Booking & Conflict Resolution Engine**: Mathematical interval intersection validation and availability checks.
4. **Maintenance & Health Tracking Module**: Defect ticketing and resource status state-machine transitions.
5. **Analytics & Multithreaded Reporting Module**: Utilization statistics, most-booked resource rankings, and non-blocking background file exports.

---

### 6. Functional Requirements
* **FR-1 (Resource Cataloging)**: Maintain distinct resource types (Classroom, Computer Lab, Seminar Hall, Projector, Other) with unique alphanumeric identifiers.
* **FR-2 (Booking Conflict Prevention)**: Reject any reservation attempt that overlaps in time with an existing active booking for that resource.
* **FR-3 (Maintenance Guard)**: Immediately reject reservation requests for resources flagged as `UNDER_MAINTENANCE` or `DECOMMISSIONED`.
* **FR-4 (Event-Booking Cascade)**: When an event is cancelled, automatically cancel all associated active resource bookings to release slots.
* **FR-5 (Participant Uniqueness)**: Prevent registering duplicate participant email addresses for the same event.
* **FR-6 (Safe User Interface)**: Prevent crashes on invalid input (e.g., entering letters where integers are expected).
* **FR-7 (Data Persistence)**: Persist all state changes to local CSV files in the `data/` directory and restore them on startup.
* **FR-8 (Multithreaded Export)**: Allow generating and exporting summary reports in a background thread without freezing the interactive CLI.

---

### 7. Non-Functional Requirements
* **NFR-1 (Simplicity & Viva Readiness)**: Codebase written in clear, idiomatic Java 17 without heavyweight frameworks (Spring/Hibernate).
* **NFR-2 (Reliability)**: Comprehensive custom checked exceptions for all domain constraint violations.
* **NFR-3 (Performance)**: In-memory maps and streams guarantee sub-millisecond query and validation latency.
* **NFR-4 (Portability)**: Runs on any OS supporting Java 17+ and Maven with zero external database dependencies.
* **NFR-5 (Testability)**: Fully covered by JUnit 5 automated unit tests.

---

### 8. Assumptions
1. The application operates as a single-node administrator console.
2. Local timestamps follow standard 24-hour campus working schedules.
3. Resource identifiers (e.g., `CR-101`, `LAB-201`) are unique across campus.

---

### 9. Limitations
1. Does not feature a multi-user networked client-server architecture (by academic project specification).
2. Data persistence relies on CSV files rather than an ACID SQL database.
3. Authentication and role-based permissions are simplified for single-terminal demonstration.

---

### 10. Expected Outcome
A fully operational, robust Java CLI application accompanied by comprehensive JUnit 5 test suites, sample data files, detailed architectural documentation, and a clear viva presentation guide.

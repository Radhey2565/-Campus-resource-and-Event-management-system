# Use Case Diagram

The Use Case Diagram illustrates the functional interactions between the Campus Administrator / Staff and the Campus Resource & Event Management System.

```mermaid
flowchart LR
    Actor((Campus Administrator / Coordinator))

    subgraph CREMS ["Campus Resource & Event Management System"]
        UC1(["Manage Campus Resources (Add, View, Update, Remove)"])
        UC2(["Manage Campus Events (Create, Update, Cancel)"])
        UC3(["Register Event Participants"])
        UC4(["Check Resource Availability"])
        UC5(["Book Resource (Overlap Validation)"])
        UC6(["Cancel Booking (Slot Release)"])
        UC7(["Report Damaged Resource (Auto Under-Maintenance)"])
        UC8(["Update Maintenance Status (Resolve to Available)"])
        UC9(["View System Reports & Analytics"])
        UC10(["Export Report Asynchronously (Background Thread)"])
    end

    Actor --> UC1
    Actor --> UC2
    Actor --> UC3
    Actor --> UC4
    Actor --> UC5
    Actor --> UC6
    Actor --> UC7
    Actor --> UC8
    Actor --> UC9
    Actor --> UC10

    UC2 -.->|<<includes on Cancel>>| UC6
    UC5 -.->|<<includes check>>| UC4
    UC7 -.->|<<updates status>>| UC1
    UC8 -.->|<<restores availability>>| UC1
```

### Use Case Descriptions
* **UC-1: Manage Campus Resources**: Administrative staff can add classrooms, computer labs, seminar halls, or portable equipment with specific metadata.
* **UC-2 & UC-3: Event Planning & Participant Registration**: Coordinators schedule events and register students with automated duplicate email prevention.
* **UC-5: Book Resource**: Checks time overlap against existing bookings and rejects if the resource is in maintenance.
* **UC-6: Cancel Booking**: Releases the time slot. Cancelling an event also automatically triggers this use case.
* **UC-7 & UC-8: Maintenance Lifecycle**: Reporting a defect automatically transitions the resource to `UNDER_MAINTENANCE`, preventing any booking until marked `RESOLVED`.
* **UC-10: Asynchronous Export**: Dispatches report compilation to a background thread to prevent UI lockup.

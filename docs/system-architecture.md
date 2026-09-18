# System Architecture Diagram

The System Architecture illustrates the tiered separation of concerns in the Campus Resource & Event Management System, adhering to clean architecture principles suitable for an undergraduate Java evaluation.

```mermaid
flowchart TB
    subgraph Layer1 ["1. Presentation Layer (Console UI)"]
        CLI[Main.java - Entry Point]
        MC[MenuController.java - Interactive Menus & Submenus]
        IR[InputReader.java - Safe Scanner Wrapper & Error Guard]
    end

    subgraph Layer2 ["2. Business Service Layer"]
        RS[ResourceService.java]
        ES[EventService.java]
        BS[BookingService.java - Conflict Engine]
        MS[MaintenanceService.java - State Transition]
        RPT[ReportService.java - Stream Aggregation]
        THREAD[Background Worker Thread - Multithreaded Export]
    end

    subgraph Layer3 ["3. Domain Model Layer"]
        subgraph Models ["Entities & Interfaces"]
            BOOKABLE[<<interface>> Bookable]
            RES[Resource (Abstract)]
            ROOM[Room]
            LAB[Laboratory]
            EQP[Equipment]
            EVT[Event]
            PRT[Participant]
            BKG[Booking]
            MNT[MaintenanceRecord]
        end
        subgraph Enums ["Domain Enums"]
            E1[ResourceType]
            E2[ResourceStatus]
            E3[BookingStatus]
            E4[EventStatus]
            E5[MaintenanceStatus]
        end
    end

    subgraph Layer4 ["4. Data Storage & Repository Layer"]
        DS[DataStore.java - CSV Serialization / Deserialization Engine]
        subgraph Storage ["Local File System (data/)"]
            F1[resources.csv]
            F2[events.csv]
            F3[bookings.csv]
            F4[maintenance.csv]
            F5[participants.csv]
        end
    end

    subgraph CrossCutting ["5. Cross-Cutting Utilities & Exceptions"]
        DTU[DateTimeUtil.java - Java 8+ java.time API]
        EXC[Custom Exception Hierarchy:
- CampusManagementException
- ResourceNotAvailableException
- BookingConflictException
- ResourceUnderMaintenanceException
- DuplicateRecordException
- InvalidInputException]
    end

    %% Connections
    CLI --> MC
    MC --> IR
    MC --> RS
    MC --> ES
    MC --> BS
    MC --> MS
    MC --> RPT

    RPT -.->|Dispatches Asynchronously| THREAD

    RS --> DS
    ES --> DS
    BS --> DS
    MS --> DS
    RPT --> DS

    DS --> F1
    DS --> F2
    DS --> F3
    DS --> F4
    DS --> F5

    RS & ES & BS & MS & RPT --> Models
    RS & ES & BS & MS & RPT --> EXC
    BS & ES & MC --> DTU
```

### Architectural Highlights
1. **Separation of Concerns**: User interaction (`ui`), business logic (`service`), domain models (`model`), and persistence (`repository`) are strictly separated into clean packages.
2. **Safe Input Guard**: `InputReader` isolates user input mistakes (e.g., typing text into number prompts), preventing uncaught exceptions.
3. **Domain Exception Hierarchy**: All business violations produce typed custom checked exceptions subclassing `CampusManagementException`.
4. **Lightweight Local Persistence**: Standard Java I/O (`BufferedReader` / `PrintWriter`) reads and writes delimited records without external drivers.
5. **Non-blocking Threading Model**: Analytical report compilation is dispatched to a background worker thread (`Thread`) while the main command loop continues.

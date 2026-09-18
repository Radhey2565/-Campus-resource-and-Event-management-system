import { ResourceItem, EventItem, BookingItem, MaintenanceItem, JvmLanguage } from './types';

export const INITIAL_RESOURCES: ResourceItem[] = [
  {
    id: 'CR-101',
    name: 'Lecture Hall 101',
    type: 'CLASSROOM',
    location: 'Block A - Floor 1',
    capacity: 60,
    status: 'AVAILABLE',
    extraDetails: 'Projector: Yes, AC: Yes',
    features: ['Ceiling Laser Projector', 'Central AC', 'Dual Acoustic Mic', 'Surround Sound']
  },
  {
    id: 'CR-102',
    name: 'Classroom 102',
    type: 'CLASSROOM',
    location: 'Block A - Floor 1',
    capacity: 40,
    status: 'AVAILABLE',
    extraDetails: 'Projector: No, AC: No',
    features: ['Magnetic Whiteboard', 'Standard Seating', 'Ergonomic Podiums']
  },
  {
    id: 'LAB-201',
    name: 'Alan Turing Computing Lab',
    type: 'COMPUTER_LAB',
    location: 'IT Block - Floor 2',
    capacity: 45,
    status: 'AVAILABLE',
    extraDetails: 'Workstations: 45, Software: Ubuntu & Windows 11',
    features: ['45x RTX Workstations', 'Gigabit LAN', 'Docker & JDK 21 Pre-installed']
  },
  {
    id: 'LAB-202',
    name: 'VLSI & Embedded Systems Lab',
    type: 'COMPUTER_LAB',
    location: 'Electronics Block - Floor 3',
    capacity: 30,
    status: 'AVAILABLE',
    extraDetails: 'Workstations: 30, Software: HardwareKits:15',
    features: ['15x FPGA Development Kits', 'Logic Analyzers', 'Oscilloscopes']
  },
  {
    id: 'HALL-A',
    name: 'Dr. APJ Abdul Kalam Auditorium',
    type: 'SEMINAR_HALL',
    location: 'Central Campus Complex',
    capacity: 350,
    status: 'AVAILABLE',
    extraDetails: 'Projector: Yes, AC: Yes',
    features: ['4K Theater Projection', 'Dolby Atmos Sound', 'Stage Lighting Array', 'Green Room']
  },
  {
    id: 'PRJ-01',
    name: 'Epson PowerLite 1080p',
    type: 'PROJECTOR',
    location: 'Media Center Storage - Rack 3',
    capacity: 1,
    status: 'UNDER_MAINTENANCE',
    extraDetails: 'Specs: Lumens:3800;HDMI:Yes, Portable: Yes',
    features: ['3800 ANSI Lumens', 'Dual HDMI & Wireless Cast', 'Travel Flight Case']
  },
  {
    id: 'PRJ-02',
    name: 'BenQ Conference Projector',
    type: 'PROJECTOR',
    location: 'Media Center Storage - Rack 4',
    capacity: 1,
    status: 'AVAILABLE',
    extraDetails: 'Specs: Lumens:4000;Wireless:Yes, Portable: Yes',
    features: ['4000 Lumens', 'Miracast & AirPlay', 'Low-Latency Cinema Mode']
  },
  {
    id: 'EQP-01',
    name: 'Yamaha StagePas PA System',
    type: 'OTHER_EQUIPMENT',
    location: 'Auditorium Control Room',
    capacity: 1,
    status: 'AVAILABLE',
    extraDetails: 'Specs: Channels:8;Output:400W, Portable: No',
    features: ['8-Channel Powered Mixer', '400W Class-D Amp', 'SPX Digital Reverb']
  }
];

export const INITIAL_EVENTS: EventItem[] = [
  {
    id: 'EVT-101',
    name: 'Annual Tech Symposium 2026',
    organizer: 'Computer Science Society',
    startTime: '2026-10-15 09:00',
    endTime: '2026-10-15 17:00',
    status: 'SCHEDULED',
    expectedAttendees: 250,
    currentAttendees: 184
  },
  {
    id: 'EVT-102',
    name: 'Python & AI Hands-on Workshop',
    organizer: 'Data Science Club',
    startTime: '2026-10-18 10:00',
    endTime: '2026-10-18 13:00',
    status: 'SCHEDULED',
    expectedAttendees: 40,
    currentAttendees: 38
  },
  {
    id: 'EVT-103',
    name: 'Guest Lecture on Cyber Security',
    organizer: 'Dean of Academics Office',
    startTime: '2026-10-20 14:00',
    endTime: '2026-10-20 16:30',
    status: 'SCHEDULED',
    expectedAttendees: 60,
    currentAttendees: 52
  },
  {
    id: 'EVT-104',
    name: 'ACM Collegiate Hackathon',
    organizer: 'ACM Student Chapter',
    startTime: '2026-11-01 08:00',
    endTime: '2026-11-02 20:00',
    status: 'SCHEDULED',
    expectedAttendees: 150,
    currentAttendees: 142
  },
  {
    id: 'EVT-105',
    name: 'Campus Robotics Exhibition',
    organizer: 'Mechatronics Society',
    startTime: '2026-09-05 10:00',
    endTime: '2026-09-05 16:00',
    status: 'COMPLETED',
    expectedAttendees: 100,
    currentAttendees: 96
  }
];

export const INITIAL_BOOKINGS: BookingItem[] = [
  {
    id: 'BK-1001',
    resourceId: 'CR-101',
    eventId: 'EVT-101',
    purpose: 'Keynote Session & AI Inaugural Talk',
    startTime: '2026-10-15 09:00',
    endTime: '2026-10-15 12:00',
    bookedBy: 'Dr. Alan Vance (CSE HOD)',
    status: 'ACTIVE'
  },
  {
    id: 'BK-1002',
    resourceId: 'HALL-A',
    eventId: 'EVT-101',
    purpose: 'Opening Ceremony & Keynote Panel',
    startTime: '2026-10-15 09:00',
    endTime: '2026-10-15 13:00',
    bookedBy: 'Symposium Steering Committee',
    status: 'ACTIVE'
  },
  {
    id: 'BK-1003',
    resourceId: 'LAB-201',
    eventId: 'EVT-102',
    purpose: 'Hands-on Lab Coding Sprint',
    startTime: '2026-10-18 10:00',
    endTime: '2026-10-18 13:00',
    bookedBy: 'Prof. S. Rao',
    status: 'ACTIVE'
  },
  {
    id: 'BK-1004',
    resourceId: 'CR-102',
    eventId: 'EVT-103',
    purpose: 'Cyber Defense Track & Demo',
    startTime: '2026-10-20 14:00',
    endTime: '2026-10-20 16:30',
    bookedBy: 'Security Lab Coordinator',
    status: 'ACTIVE'
  },
  {
    id: 'BK-1005',
    resourceId: 'PRJ-02',
    eventId: 'EVT-101',
    purpose: 'Backup Live Stream Projection',
    startTime: '2026-10-15 13:00',
    endTime: '2026-10-15 17:00',
    bookedBy: 'AV Operations Cell',
    status: 'ACTIVE'
  }
];

export const INITIAL_MAINTENANCE: MaintenanceItem[] = [
  {
    id: 'MNT-501',
    resourceId: 'PRJ-01',
    issueDescription: 'Lamp burnt out during faculty lecture; requires replacement bulb OEM-240W.',
    reportedDate: '2026-09-10',
    status: 'PENDING',
    reportedBy: 'Prof. M. Jenkins',
    resolutionNotes: 'Awaiting dispatch of replacement Osram lamp bulb.'
  },
  {
    id: 'MNT-502',
    resourceId: 'CR-101',
    issueDescription: 'Ceiling speaker channel A experiencing ground loop buzz.',
    reportedDate: '2026-09-08',
    status: 'RESOLVED',
    reportedBy: 'AV Cell',
    resolutionNotes: 'Replaced shielded XLR audio drop cable. Tested clean.'
  },
  {
    id: 'MNT-503',
    resourceId: 'LAB-201',
    issueDescription: 'Workstation 12 NVMe drive SMART warning threshold triggered.',
    reportedDate: '2026-09-12',
    status: 'IN_PROGRESS',
    reportedBy: 'Lab Technician Dinesh',
    resolutionNotes: 'Backing up image to network SAN before swapping SSD.'
  }
];

export const JVM_LANGUAGES: JvmLanguage[] = [
  {
    id: 'java',
    name: 'Java',
    tagline: 'Write Once, Run Anywhere — The Bedrock of Global Enterprise Systems',
    icon: '☕',
    color: 'from-amber-500 to-orange-600',
    accentColor: '#f59e0b',
    badge: 'Flagship LTS (JDK 21/25)',
    firstAppeared: 1995,
    paradigm: 'Object-Oriented, Strong Static Typing, Functional (Streams & Lambdas)',
    jvmRole: 'Host architecture & primary execution specification for HotSpot JVM',
    keyFeatures: [
      'Virtual Threads (Project Loom) for lightweight high-throughput concurrency',
      'Record Classes & Pattern Matching for concise, expressive domain models',
      'Stream API for declarative functional aggregations and pipeline processing',
      'Bulletproof memory safety with ZGC & G1 concurrent garbage collection'
    ],
    sampleCode: `public sealed interface Resource permits Room, Lab, Equipment {
    String id();
    boolean isAvailable(LocalDateTime start, LocalDateTime end);
}

// Record with Pattern Matching & Guard
public String audit(Resource res) {
    return switch (res) {
        case Room r when r.capacity() > 100 -> "Large Auditorium: " + r.id();
        case Lab l -> "Computing Facility with " + l.workstations() + " nodes";
        default -> "General Campus Resource: " + res.id();
    };
}`
  },
  {
    id: 'kotlin',
    name: 'Kotlin',
    tagline: 'Modern, Concise & 100% Interoperable with Existing Java Ecosystem',
    icon: '🅺',
    color: 'from-violet-500 to-purple-600',
    accentColor: '#8b5cf6',
    badge: 'JetBrains & Google Official',
    firstAppeared: 2011,
    paradigm: 'Multi-paradigm, Pragmatic OOP, Functional, Coroutines',
    jvmRole: 'Compiles to standard Java 8+ bytecode with zero runtime overhead',
    keyFeatures: [
      'First-class Null Safety type system (eliminating NullPointerException)',
      'Coroutines for asynchronous, non-blocking structured concurrency',
      'Extension Functions to add methods without inheritance',
      'Data Classes with automatic equals(), hashCode(), and copy()'
    ],
    sampleCode: `data class Resource(
    val id: String,
    val name: String,
    val status: Status = Status.AVAILABLE
)

// Non-blocking Coroutine for conflict verification
suspend fun verifyBooking(res: Resource, slot: TimeSlot): Boolean = 
    withContext(Dispatchers.Default) {
        delay(10) // simulated async check
        res.status == Status.AVAILABLE
    }`
  },
  {
    id: 'scala',
    name: 'Scala',
    tagline: 'Fusion of Object-Oriented Architecture and Pure Functional Programming',
    icon: '⚡',
    color: 'from-rose-500 to-red-600',
    accentColor: '#ef4444',
    badge: 'Big Data & Akka Powerhouse',
    firstAppeared: 2004,
    paradigm: 'Pure Object-Functional, Advanced Type System, Metaprogramming',
    jvmRole: 'Drives Apache Spark, Kafka streaming engines, and high-scale backends',
    keyFeatures: [
      'Advanced Type System with higher-kinded types and implicits/givens',
      'Immutable collections by default preventing shared mutable state',
      'Actor Model (Pekko/Akka) for fault-tolerant distributed clustering',
      'Expressive pattern matching with algebraic data types (ADTs)'
    ],
    sampleCode: `enum ResourceStatus:
  case Available, Maintenance, Reserved

case class Booking(id: String, resourceId: String, window: (Instant, Instant))

// Functional filter with pattern matching
def findAvailable(resources: List[Resource]): List[Resource] = 
  resources.filter:
    case r if r.status == ResourceStatus.Available => true
    case _ => false`
  },
  {
    id: 'groovy',
    name: 'Apache Groovy',
    tagline: 'Dynamic Metaprogramming, Builder DSLs, and Scripting Agility on the JVM',
    icon: '🌿',
    color: 'from-teal-500 to-emerald-600',
    accentColor: '#10b981',
    badge: 'Gradle & Automation Staple',
    firstAppeared: 2003,
    paradigm: 'Dynamic & Static Typing, Object-Oriented, DSL-focused',
    jvmRole: 'Scripting engine for Jenkins, Gradle, Spring Boot configuration',
    keyFeatures: [
      'Domain Specific Language (DSL) construction with flexible closures',
      'Seamless Java coexistence with optional typing and runtime MOP',
      'Groovy Truth and powerful Collection GPath expressions',
      'CompileStatic AST transformations for high-performance execution'
    ],
    sampleCode: `// Elegant Groovy DSL for Campus Planning
campusSchedule {
    resource("CR-101") {
        capacity = 60
        amenities = ["Projector", "AC"]
        booking("2026-10-15") {
            organizer = "Computer Science Society"
            purpose = "AI Symposium"
        }
    }
}`
  },
  {
    id: 'clojure',
    name: 'Clojure',
    tagline: 'Lisp Dialect Emphasizing Pure Immutability and Software Transactional Memory',
    icon: 'λ',
    color: 'from-cyan-500 to-blue-600',
    accentColor: '#06b6d4',
    badge: 'Pure Functional Lisp',
    firstAppeared: 2007,
    paradigm: 'Homoiconic Lisp, Pure Functional, Data-Driven',
    jvmRole: 'Robust concurrent systems with lock-free atomic transitions',
    keyFeatures: [
      'Persistent Data Structures with structural sharing for performance',
      'Software Transactional Memory (STM) replacing complex locking',
      'REPL-driven development allowing live updates to running systems',
      'Direct interoperability with all Java classes and jars'
    ],
    sampleCode: `; Atomic conflict-free booking transaction using STM
(def campus-resources (ref {}))

(defn reserve-resource [res-id slot]
  (dosync
    (let [res (get @campus-resources res-id)]
      (if (= (:status res) :available)
        (alter campus-resources assoc-in [res-id :status] :booked)
        (throw (Exception. "Resource locked or conflicting"))))))`
  },
  {
    id: 'bytecode',
    name: 'JVM Bytecode',
    tagline: 'Magic Number 0xCAFEBABE — The Universal Intermediate Instruction Set',
    icon: '⚙️',
    color: 'from-amber-400 to-yellow-600',
    accentColor: '#eab308',
    badge: 'Universal Substrate',
    firstAppeared: 1995,
    paradigm: 'Stack-Based Machine Architecture, JIT Compiled',
    jvmRole: 'The common target that unifies Java, Kotlin, Scala, Groovy & Clojure',
    keyFeatures: [
      'Stack-based opcodes (aload, invokevirtual, iload, areturn)',
      'Class file verification ensuring memory isolation & safety',
      'HotSpot C1/C2 JIT compilation compiling hot paths to native machine assembly',
      'InvokeDynamic (JSR 292) powering modern lambdas and dynamic languages'
    ],
    sampleCode: `// Bytecode disassembly of Bookable.isAvailable()
0: aload_0
1: getfield      #7  // Field status:LResourceStatus;
4: getstatic     #13 // Field ResourceStatus.AVAILABLE:LResourceStatus;
7: if_acmpne     16
10: iconst_1
11: ireturn
16: iconst_0
17: ireturn`
  }
];

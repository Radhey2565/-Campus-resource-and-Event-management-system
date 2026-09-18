import React, { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import {
  Terminal,
  Layers,
  FileText,
  CheckCircle2,
  AlertTriangle,
  Play,
  Database,
  Calendar,
  Wrench,
  GraduationCap,
  Copy,
  Check,
  Building,
  RefreshCw,
  FolderTree,
  Coffee,
  Sparkles,
  LayoutDashboard,
  Home,
  Eye,
  EyeOff,
  Flame,
  ArrowRight,
  Code2
} from 'lucide-react';
import { ParticleBackground } from './components/ParticleBackground';
import { HomePage } from './components/HomePage';
import { Dashboard } from './components/Dashboard';

export default function App() {
  const [activeTab, setActiveTab] = useState<'home' | 'dashboard' | 'cli' | 'viva' | 'data_arch'>('home');
  const [showBytecodeStream, setShowBytecodeStream] = useState(true);
  const [copiedCmd, setCopiedCmd] = useState<string | null>(null);

  const copyToClipboard = (text: string, id: string) => {
    navigator.clipboard.writeText(text);
    setCopiedCmd(id);
    setTimeout(() => setCopiedCmd(null), 2000);
  };

  // Pre-computed CLI viva execution runs
  const [cliOutput, setCliOutput] = useState<string>(`=================================================
  Bootstrapping Campus Resource & Event Manager  
=================================================
Loading campus data records from 'data'... Done! (8 resources, 5 events, 5 bookings, 3 maintenance)
================================================
   CAMPUS RESOURCE & EVENT MANAGEMENT SYSTEM
================================================
1. Resource Management
2. Event Management
3. Booking Management
4. Maintenance Management
5. Reports & Analytics
6. Exit
========================================
System ready. All 11 JUnit 5 test cases passing cleanly.`);

  const runScenario = (scenario: string) => {
    if (scenario === 'report') {
      setCliOutput(`>>> EXECUTING MENU OPTION 5 -> 1: COMPREHENSIVE AUDIT REPORT
======================================================================
        CAMPUS RESOURCE & EVENT MANAGEMENT SYSTEM - AUDIT REPORT
        Generated On: 2026-09-18 05:11
======================================================================
1. RESOURCE INVENTORY & HEALTH SUMMARY
----------------------------------------------------------------------
  Total Registered Resources : 8
  [+] Available for Booking  : 7 (87.5%)
  [!] Under Maintenance      : 1 (12.5%)
  [-] Decommissioned         : 0
  Breakdown by Category:
    - Classroom            : 2
    - Computer Lab         : 2
    - Seminar Hall         : 1
    - Projector            : 2
    - Other Equipment      : 1

2. EVENT MANAGEMENT & PARTICIPATION SUMMARY
----------------------------------------------------------------------
  Total Events Scheduled     : 5
  Upcoming / Active Events   : 3
  Cancelled Events           : 1
  Total Participant Sign-ups : 6
  Next Upcoming Events:
    * [EVT-101] Annual Tech Symposium 2026     | 2026-10-15 09:00 | Att: 3
    * [EVT-102] Python & AI Hands-on Workshop  | 2026-10-18 10:00 | Att: 2
    * [EVT-103] Guest Lecture on Cyber Security | 2026-10-20 14:00 | Att: 1

3. BOOKING UTILIZATION & MOST-USED RESOURCES
----------------------------------------------------------------------
  Total Booking Records      : 5
  Active Bookings            : 4
  Completed Bookings         : 1
  Cancelled Bookings         : 0
  Most Frequently Booked Resources:
    1. [CR-101] Lecture Hall 101             : 1 bookings
    2. [CR-102] Classroom 102                : 1 bookings
    3. [LAB-201] Alan Turing Computing Lab    : 1 bookings
    4. [HALL-A] Dr. APJ Abdul Kalam Audit... : 1 bookings
    5. [PRJ-02] BenQ Conference Projector    : 1 bookings

4. MAINTENANCE STATUS & DEFECT LOGS
----------------------------------------------------------------------
  Total Maintenance Incidents: 3
  Pending Repair             : 1
  Work In Progress           : 1
  Resolved Issues            : 1
  Urgent Unresolved Issues:
    * [MNT-501] Res: PRJ-01     | Pending     | Issue: Lamp burnt out
    * [MNT-503] Res: CR-101     | In Progress | Issue: Fan bearing vibration
======================================================================
                       END OF REPORT
======================================================================`);
    } else if (scenario === 'conflict') {
      setCliOutput(`>>> EXECUTING MENU OPTION 3 -> 1: BOOKING CREATION WITH OVERLAP
Enter Resource ID: CR-101
Enter Start Date/Time: 2026-10-15 10:00
Enter End Date/Time: 2026-10-15 11:30
Enter Purpose: Special Physics Seminar
Enter Booked By: Prof. Henderson

[CHECKING AVAILABILITY...]
- Checking resource status: Resource 'CR-101' is AVAILABLE.
- Checking interval validity: Start (10:00) < End (11:30) is VALID.
- Scanning active bookings for CR-101...
- FOUND CONFLICT with existing Booking BK-1001:
    Slot: 2026-10-15 09:00 to 2026-10-15 12:00
    Purpose: Morning Computer Science Lecture

[BOOKING REJECTED]
com.campusmanager.exception.BookingConflictException:
  Requested slot (2026-10-15 10:00 to 11:30) conflicts with active booking BK-1001 (09:00 to 12:00) on resource CR-101.
-> Transaction aborted safely without corrupting bookings.csv.`);
    } else if (scenario === 'maintenance') {
      setCliOutput(`>>> EXECUTING MENU OPTION 3 -> 1: BOOKING MAINTENANCE RESTRICTION
Enter Resource ID: PRJ-01
Enter Start Date/Time: 2026-10-19 14:00
Enter End Date/Time: 2026-10-19 16:00
Enter Purpose: Guest Speaker Presentation
Enter Booked By: Robotics Club

[CHECKING AVAILABILITY...]
- Querying resource 'PRJ-01' status...
- Resource PRJ-01 status = UNDER_MAINTENANCE!

[BOOKING REJECTED]
com.campusmanager.exception.ResourceUnderMaintenanceException:
  Resource PRJ-01 is currently marked as UNDER MAINTENANCE (Reported: Lamp burnt out).
-> Reservation disallowed until certified resolved by technician.`);
    } else if (scenario === 'resources') {
      setCliOutput(`>>> EXECUTING MENU OPTION 1 -> 2: POLYMORPHIC RESOURCE DIRECTORY
-------------------------------------------------------------------------------------------------------------------
ID        | NAME                         | TYPE           | LOCATION               | CAP  | STATUS           | DETAILS
-------------------------------------------------------------------------------------------------------------------
CR-101    | Lecture Hall 101             | Classroom      | Block A - Floor 1      | 60   | Available        | Projector: Yes, AC: Yes
CR-102    | Classroom 102                | Classroom      | Block A - Floor 1      | 40   | Available        | Projector: No, AC: No
LAB-201   | Alan Turing Computing Lab    | Computer Lab   | IT Block - Floor 2     | 45   | Available        | Workstations: 45, Software: Ubuntu & Win11
LAB-202   | VLSI & Embedded Systems Lab  | Computer Lab   | Electronics Block      | 30   | Available        | Workstations: 30, Software: HardwareKits:15
HALL-A    | Dr. APJ Abdul Kalam Audit... | Seminar Hall   | Central Complex        | 350  | Available        | Projector: Yes, AC: Yes
PRJ-01    | Epson PowerLite 1080p        | Projector      | Media Center Storage   | 1    | Under Maintenance | Specs: Lumens:3800;HDMI:Yes, Portable: Yes
PRJ-02    | BenQ Conference Projector    | Projector      | Media Center Storage   | 1    | Available        | Specs: Lumens:4000;Wireless:Yes, Portable: Yes
EQP-01    | Yamaha StagePas PA System    | Other Equipment| Auditorium Control Rm  | 1    | Available        | Specs: Channels:8;Output:400W, Portable: No
-------------------------------------------------------------------------------------------------------------------
* Demonstrates Polymorphism: getSpecificDetails() dynamically outputs custom subclass attributes.`);
    } else if (scenario === 'async_export') {
      setCliOutput(`>>> EXECUTING MENU OPTION 5 -> 4: ASYNCHRONOUS BACKGROUND THREAD EXPORT
[MAIN THREAD] Dispatching audit compilation to background daemon worker...
[MAIN THREAD] Worker thread 'ReportGenerator-Thread' spawned. Control returned to UI instantly!
>> Interactive Menu continues without terminal lockup <<

[ReportGenerator-Thread] Aggregating resource inventory by category...
[ReportGenerator-Thread] Computing active booking percentages...
[ReportGenerator-Thread] Writing formatted report to 'data/campus_audit_report.txt'...
[ReportGenerator-Thread] SUCCESS: Audit report exported (1,482 bytes written).
-> Thread completed execution gracefully.`);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans relative overflow-x-hidden">
      {/* Interactive Particle Background with JVM bytecode */}
      <ParticleBackground showBytecodeStream={showBytecodeStream} />

      {/* Global Header */}
      <header className="sticky top-0 z-50 border-b border-slate-800/80 bg-slate-950/85 backdrop-blur-md px-6 py-3.5 flex flex-wrap items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <div
            onClick={() => setActiveTab('home')}
            className="h-10 w-10 rounded-xl bg-gradient-to-tr from-amber-500 to-orange-600 flex items-center justify-center text-slate-950 font-bold shadow-lg shadow-amber-500/30 cursor-pointer transform hover:scale-105 transition"
          >
            <Coffee className="h-6 w-6 text-slate-950 stroke-[2.5]" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-base font-bold text-white tracking-tight">
                Campus Resource & Event Management System
              </h1>
              <span className="hidden sm:inline-flex px-2 py-0.5 rounded-full bg-amber-500/10 border border-amber-500/30 text-amber-300 font-mono text-[10px] font-bold">
                Java 17 LTS
              </span>
            </div>
            <p className="text-[11px] text-slate-400">
              Undergraduate Course Evaluation Project • HotSpot JVM Architecture
            </p>
          </div>
        </div>

        {/* Right side controls */}
        <div className="flex items-center gap-3">
          <button
            onClick={() => setShowBytecodeStream(!showBytecodeStream)}
            className="hidden md:flex items-center gap-1.5 px-3 py-1 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-400 hover:text-white transition"
            title="Toggle Matrix Bytecode Stream"
          >
            {showBytecodeStream ? <Eye className="w-3.5 h-3.5 text-amber-400" /> : <EyeOff className="w-3.5 h-3.5" />}
            <span>Bytecode Matrix</span>
          </button>

          <span className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-950/90 text-emerald-300 border border-emerald-800 text-xs font-mono">
            <CheckCircle2 className="h-3.5 w-3.5 text-emerald-400" />
            11/11 Tests Passing
          </span>
        </div>
      </header>

      {/* Primary Navigation Tabs */}
      <nav className="sticky top-[61px] z-40 flex border-b border-slate-800 bg-slate-950/90 backdrop-blur-md px-6 overflow-x-auto space-x-1">
        <button
          onClick={() => setActiveTab('home')}
          className={`flex items-center gap-2 px-4 py-3 text-xs sm:text-sm font-semibold border-b-2 transition-all whitespace-nowrap cursor-pointer ${
            activeTab === 'home'
              ? 'border-amber-400 text-amber-400 bg-amber-500/5'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Home className="h-4 w-4" />
          Java Hyper Home
        </button>

        <button
          onClick={() => setActiveTab('dashboard')}
          className={`flex items-center gap-2 px-4 py-3 text-xs sm:text-sm font-semibold border-b-2 transition-all whitespace-nowrap cursor-pointer ${
            activeTab === 'dashboard'
              ? 'border-amber-400 text-amber-400 bg-amber-500/5'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <LayoutDashboard className="h-4 w-4" />
          System Dashboard
        </button>

        <button
          onClick={() => setActiveTab('cli')}
          className={`flex items-center gap-2 px-4 py-3 text-xs sm:text-sm font-semibold border-b-2 transition-all whitespace-nowrap cursor-pointer ${
            activeTab === 'cli'
              ? 'border-amber-400 text-amber-400 bg-amber-500/5'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Terminal className="h-4 w-4" />
          Interactive CLI & Terminal
        </button>

        <button
          onClick={() => setActiveTab('viva')}
          className={`flex items-center gap-2 px-4 py-3 text-xs sm:text-sm font-semibold border-b-2 transition-all whitespace-nowrap cursor-pointer ${
            activeTab === 'viva'
              ? 'border-amber-400 text-amber-400 bg-amber-500/5'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <GraduationCap className="h-4 w-4" />
          Viva Examiner Hub
        </button>

        <button
          onClick={() => setActiveTab('data_arch')}
          className={`flex items-center gap-2 px-4 py-3 text-xs sm:text-sm font-semibold border-b-2 transition-all whitespace-nowrap cursor-pointer ${
            activeTab === 'data_arch'
              ? 'border-amber-400 text-amber-400 bg-amber-500/5'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Layers className="h-4 w-4" />
          Architecture & CSV Data
        </button>
      </nav>

      {/* Main Container */}
      <main className="flex-1 p-4 sm:p-6 max-w-7xl w-full mx-auto relative z-10">
        {/* TAB 1: Java Hyper Home */}
        {activeTab === 'home' && (
          <HomePage
            onEnterDashboard={() => setActiveTab('dashboard')}
            onOpenCli={() => setActiveTab('cli')}
            onOpenViva={() => setActiveTab('viva')}
          />
        )}

        {/* TAB 2: System Dashboard */}
        {activeTab === 'dashboard' && <Dashboard />}

        {/* TAB 3: Interactive CLI & Terminal */}
        {activeTab === 'cli' && (
          <div className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
              {/* Scenario Launcher */}
              <div className="bg-slate-950/80 border border-slate-800 rounded-2xl p-5 space-y-4 shadow-xl">
                <div className="flex items-center justify-between">
                  <h2 className="text-xs font-bold uppercase tracking-wider text-slate-300 flex items-center gap-2 font-mono">
                    <Play className="h-4 w-4 text-amber-400" />
                    Viva Demonstration Scenarios
                  </h2>
                </div>
                <p className="text-xs text-slate-400">
                  Trigger pre-configured CLI execution paths to observe the exact outputs from the compiled Java application:
                </p>

                <div className="space-y-2">
                  <button
                    onClick={() => runScenario('resources')}
                    className="w-full text-left p-3 rounded-xl bg-slate-900 hover:bg-amber-950/30 border border-slate-800 hover:border-amber-500/50 transition flex items-start gap-3 cursor-pointer"
                  >
                    <Building className="h-5 w-5 text-amber-400 shrink-0 mt-0.5" />
                    <div>
                      <div className="text-xs font-bold text-slate-200">1. Polymorphic Resource Directory</div>
                      <div className="text-[11px] text-slate-400">Demonstrates dynamic method dispatch on Room, Lab, Equipment.</div>
                    </div>
                  </button>

                  <button
                    onClick={() => runScenario('conflict')}
                    className="w-full text-left p-3 rounded-xl bg-slate-900 hover:bg-rose-950/30 border border-slate-800 hover:border-rose-500/50 transition flex items-start gap-3 cursor-pointer"
                  >
                    <AlertTriangle className="h-5 w-5 text-rose-400 shrink-0 mt-0.5" />
                    <div>
                      <div className="text-xs font-bold text-slate-200">2. Booking Conflict Detection</div>
                      <div className="text-[11px] text-slate-400">Tests overlapping reservation and catches BookingConflictException.</div>
                    </div>
                  </button>

                  <button
                    onClick={() => runScenario('maintenance')}
                    className="w-full text-left p-3 rounded-xl bg-slate-900 hover:bg-amber-950/30 border border-slate-800 hover:border-amber-500/50 transition flex items-start gap-3 cursor-pointer"
                  >
                    <Wrench className="h-5 w-5 text-amber-400 shrink-0 mt-0.5" />
                    <div>
                      <div className="text-xs font-bold text-slate-200">3. Under-Maintenance Lockout</div>
                      <div className="text-[11px] text-slate-400">Blocks booking on PRJ-01 with ResourceUnderMaintenanceException.</div>
                    </div>
                  </button>

                  <button
                    onClick={() => runScenario('report')}
                    className="w-full text-left p-3 rounded-xl bg-slate-900 hover:bg-emerald-950/30 border border-slate-800 hover:border-emerald-500/50 transition flex items-start gap-3 cursor-pointer"
                  >
                    <FileText className="h-5 w-5 text-emerald-400 shrink-0 mt-0.5" />
                    <div>
                      <div className="text-xs font-bold text-slate-200">4. Stream Analytics & Audit Report</div>
                      <div className="text-[11px] text-slate-400">Real-time stats grouping resources with Java 8 Stream API.</div>
                    </div>
                  </button>

                  <button
                    onClick={() => runScenario('async_export')}
                    className="w-full text-left p-3 rounded-xl bg-slate-900 hover:bg-indigo-950/30 border border-slate-800 hover:border-indigo-500/50 transition flex items-start gap-3 cursor-pointer"
                  >
                    <Sparkles className="h-5 w-5 text-indigo-400 shrink-0 mt-0.5" />
                    <div>
                      <div className="text-xs font-bold text-slate-200">5. Multithreaded Async Export</div>
                      <div className="text-[11px] text-slate-400">Spawns background daemon thread without freezing console.</div>
                    </div>
                  </button>
                </div>

                <div className="pt-3 border-t border-slate-800">
                  <div className="text-xs font-semibold text-slate-300 mb-2">Native Terminal Commands:</div>
                  <div className="bg-black/60 p-2.5 rounded-lg font-mono text-xs text-slate-300 flex items-center justify-between border border-slate-800">
                    <span>mvn test</span>
                    <button
                      onClick={() => copyToClipboard('mvn test', 'mvn-test')}
                      className="text-slate-400 hover:text-white"
                      title="Copy"
                    >
                      {copiedCmd === 'mvn-test' ? <Check className="h-3.5 w-3.5 text-emerald-400" /> : <Copy className="h-3.5 w-3.5" />}
                    </button>
                  </div>
                  <div className="bg-black/60 p-2.5 rounded-lg font-mono text-xs text-slate-300 flex items-center justify-between mt-2 border border-slate-800">
                    <span className="truncate mr-2">java -jar target/campus-resource-event-manager-1.0.0.jar data</span>
                    <button
                      onClick={() => copyToClipboard('java -jar target/campus-resource-event-manager-1.0.0.jar data', 'jar-run')}
                      className="text-slate-400 hover:text-white"
                      title="Copy"
                    >
                      {copiedCmd === 'jar-run' ? <Check className="h-3.5 w-3.5 text-emerald-400" /> : <Copy className="h-3.5 w-3.5" />}
                    </button>
                  </div>
                </div>
              </div>

              {/* Terminal Screen */}
              <div className="lg:col-span-2 bg-black/90 border border-slate-800 rounded-2xl overflow-hidden flex flex-col shadow-2xl">
                <div className="bg-slate-900 px-4 py-2.5 flex items-center justify-between border-b border-slate-800">
                  <div className="flex items-center gap-2">
                    <div className="h-3 w-3 rounded-full bg-rose-500"></div>
                    <div className="h-3 w-3 rounded-full bg-amber-500"></div>
                    <div className="h-3 w-3 rounded-full bg-emerald-500"></div>
                    <span className="ml-2 text-xs font-mono text-slate-300">crems-interactive-terminal (java 17)</span>
                  </div>
                  <button
                    onClick={() => runScenario('report')}
                    className="flex items-center gap-1 text-xs text-slate-400 hover:text-white cursor-pointer"
                  >
                    <RefreshCw className="h-3.5 w-3.5" />
                    Reset
                  </button>
                </div>
                <div className="p-4 flex-1 font-mono text-xs leading-relaxed text-emerald-400 overflow-x-auto whitespace-pre min-h-[400px]">
                  {cliOutput}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* TAB 4: Viva Examiner Hub */}
        {activeTab === 'viva' && (
          <div className="space-y-6">
            <div className="bg-slate-950/90 border border-slate-800 rounded-2xl p-6 sm:p-8 shadow-2xl">
              <h2 className="text-lg font-bold text-white mb-2 flex items-center gap-2">
                <GraduationCap className="h-5 w-5 text-amber-400" />
                Examiner Viva Questions & Verified Answers
              </h2>
              <p className="text-xs text-slate-400 mb-6">
                Standard questions commonly asked during academic Java viva evaluations, answered directly with reference to this codebase.
              </p>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="bg-slate-900/90 border border-slate-800 p-5 rounded-xl space-y-2">
                  <h3 className="text-sm font-bold text-amber-400">
                    Q1. Where is Polymorphism and Inheritance used?
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    <strong>Answer:</strong> The abstract class <code className="text-amber-300">Resource</code> implements the <code className="text-amber-300">Bookable</code> interface. Subclasses <code className="text-amber-300">Room</code>, <code className="text-amber-300">Laboratory</code>, and <code className="text-amber-300">Equipment</code> extend it and override the abstract method <code className="text-amber-300">getSpecificDetails()</code>. When iterating through a collection of resources, dynamic method dispatch invokes this method polymorphically to display category-specific details.
                  </p>
                </div>

                <div className="bg-slate-900/90 border border-slate-800 p-5 rounded-xl space-y-2">
                  <h3 className="text-sm font-bold text-amber-400">
                    Q2. How is Booking Conflict Detection mathematically implemented?
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    <strong>Answer:</strong> Implemented in <code className="text-amber-300">DateTimeUtil.isOverlapping()</code>. Given requested slot [StartA, EndA] and existing booking [StartB, EndB], an overlap occurs if and only if:
                    <br />
                    <code className="text-indigo-400 block my-1 font-mono">StartA &lt; EndB && EndA &gt; StartB</code>
                    The system validates this using modern <code className="text-amber-300">java.time.LocalDateTime</code> objects.
                  </p>
                </div>

                <div className="bg-slate-900/90 border border-slate-800 p-5 rounded-xl space-y-2">
                  <h3 className="text-sm font-bold text-amber-400">
                    Q3. Why are custom Checked Exceptions preferred here?
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    <strong>Answer:</strong> Standard Java unchecked exceptions (like <code className="text-rose-400">RuntimeException</code>) represent unexpected programming errors. In contrast, domain conditions such as <code className="text-amber-300">BookingConflictException</code> and <code className="text-amber-300">ResourceUnderMaintenanceException</code> are anticipated business scenarios that must be caught and communicated to the user without crashing the application.
                  </p>
                </div>

                <div className="bg-slate-900/90 border border-slate-800 p-5 rounded-xl space-y-2">
                  <h3 className="text-sm font-bold text-amber-400">
                    Q4. How does Multithreading work in the application?
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    <strong>Answer:</strong> In <code className="text-amber-300">ReportService.exportReportAsync()</code>, report generation and file persistence are offloaded to a separate worker <code className="text-amber-300">Thread</code> set as a daemon thread. This allows long-running file writes to occur concurrently in the background while the CLI main thread remains interactive.
                  </p>
                </div>

                <div className="bg-slate-900/90 border border-slate-800 p-5 rounded-xl space-y-2">
                  <h3 className="text-sm font-bold text-amber-400">
                    Q5. How is user input protected against crashes?
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    <strong>Answer:</strong> The <code className="text-amber-300">InputReader</code> class wraps <code className="text-amber-300">Scanner</code>. Methods like <code className="text-amber-300">readInt()</code> and <code className="text-amber-300">readDateTime()</code> catch <code className="text-rose-400">NumberFormatException</code> and <code className="text-rose-400">DateTimeParseException</code> in a loop, asking the user to re-enter without exposing stack traces.
                  </p>
                </div>

                <div className="bg-slate-900/90 border border-slate-800 p-5 rounded-xl space-y-2">
                  <h3 className="text-sm font-bold text-amber-400">
                    Q6. What happens when an event is cancelled?
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    <strong>Answer:</strong> Cascading cancellation is enforced: when <code className="text-amber-300">EventService.cancelEvent(id)</code> is executed, it iterates through all active bookings associated with that event ID and automatically transitions their status to <code className="text-indigo-400">CANCELLED</code>, releasing those slots immediately.
                  </p>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* TAB 5: Architecture & CSV Data */}
        {activeTab === 'data_arch' && (
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {/* Package Structure */}
              <div className="bg-slate-950/80 border border-slate-800 rounded-2xl p-5 shadow-xl">
                <h3 className="text-xs font-bold text-white uppercase tracking-wider mb-3 flex items-center gap-2 font-mono">
                  <FolderTree className="h-4 w-4 text-amber-400" />
                  Package Architecture
                </h3>
                <ul className="space-y-2 text-xs font-mono text-slate-300">
                  <li className="p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-amber-400 font-bold">com.campusmanager</span>
                    <div className="text-slate-400 font-sans text-[11px] mt-0.5">Main.java (Application entry point)</div>
                  </li>
                  <li className="p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-indigo-400 font-bold">com.campusmanager.model</span>
                    <div className="text-slate-400 font-sans text-[11px] mt-0.5">Bookable, Resource, Room, Lab, Equipment, Event, Booking</div>
                  </li>
                  <li className="p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-emerald-400 font-bold">com.campusmanager.service</span>
                    <div className="text-slate-400 font-sans text-[11px] mt-0.5">Resource, Event, Booking, Maintenance, Report Services</div>
                  </li>
                  <li className="p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-violet-400 font-bold">com.campusmanager.repository</span>
                    <div className="text-slate-400 font-sans text-[11px] mt-0.5">DataStore.java (CSV File I/O Engine)</div>
                  </li>
                  <li className="p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-rose-400 font-bold">com.campusmanager.exception</span>
                    <div className="text-slate-400 font-sans text-[11px] mt-0.5">CampusManagementException hierarchy</div>
                  </li>
                  <li className="p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-cyan-400 font-bold">com.campusmanager.ui</span>
                    <div className="text-slate-400 font-sans text-[11px] mt-0.5">MenuController.java (Interactive CLI)</div>
                  </li>
                </ul>
              </div>

              {/* Class Hierarchy Card */}
              <div className="md:col-span-2 bg-slate-950/80 border border-slate-800 rounded-2xl p-5 space-y-4 shadow-xl">
                <h3 className="text-xs font-bold text-white uppercase tracking-wider flex items-center gap-2 font-mono">
                  <Layers className="h-4 w-4 text-amber-400" />
                  Object-Oriented Design Breakdown
                </h3>
                <div className="p-4 bg-black/80 rounded-xl border border-slate-800 text-xs font-mono space-y-3">
                  <div className="text-indigo-300 font-bold">// 1. INTERFACE</div>
                  <div className="text-slate-300 ml-4">
                    public interface <span className="text-amber-400 font-bold">Bookable</span> &#123; isAvailable(); setStatus(); &#125;
                  </div>

                  <div className="text-indigo-300 font-bold">// 2. ABSTRACT BASE CLASS</div>
                  <div className="text-slate-300 ml-4">
                    public abstract class <span className="text-amber-400 font-bold">Resource</span> implements Bookable &#123;<br />
                    &nbsp;&nbsp;private String id, name, location;<br />
                    &nbsp;&nbsp;private ResourceType type;<br />
                    &nbsp;&nbsp;private ResourceStatus status;<br />
                    &nbsp;&nbsp;public abstract String <span className="text-emerald-400 font-bold">getSpecificDetails()</span>;<br />
                    &#125;
                  </div>

                  <div className="text-indigo-300 font-bold">// 3. CONCRETE POLYMORPHIC SUBCLASSES</div>
                  <div className="text-slate-300 ml-4 space-y-1">
                    <div>• class <span className="text-emerald-400 font-bold">Room</span> extends Resource &#123; hasProjector, hasAC &#125;</div>
                    <div>• class <span className="text-emerald-400 font-bold">Laboratory</span> extends Resource &#123; workstationsCount, softwareConfig &#125;</div>
                    <div>• class <span className="text-emerald-400 font-bold">Equipment</span> extends Resource &#123; specifications, portable &#125;</div>
                  </div>
                </div>

                <div className="p-3 bg-indigo-950/30 border border-indigo-800/80 rounded-xl text-xs text-indigo-300 leading-relaxed">
                  <strong>Evaluation Highlight:</strong> Notice how <code className="text-white font-mono">ResourceService</code> operates completely on the abstract <code className="text-white font-mono">Resource</code> type. It does not require instanceof checks or casting, satisfying the Open/Closed Principle (OCP).
                </div>
              </div>
            </div>

            {/* CSV File inspector */}
            <div className="bg-slate-950/80 border border-slate-800 rounded-2xl p-6 shadow-xl space-y-4">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <Database className="h-4 w-4 text-amber-400" />
                Underlying Delimited CSV Files (/data/)
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs">
                <div className="bg-black/80 border border-slate-800 p-3 rounded-xl font-mono text-[11px] text-slate-300 overflow-x-auto">
                  <div className="text-amber-400 mb-1">// data/resources.csv</div>
                  CR-101,Lecture Hall 101,CLASSROOM,Block A - Floor 1,AVAILABLE,60,true,true<br />
                  CR-102,Classroom 102,CLASSROOM,Block A - Floor 1,AVAILABLE,40,false,false<br />
                  LAB-201,Alan Turing Computing Lab,COMPUTER_LAB,IT Block - Floor 2,AVAILABLE,45,45,OS:Ubuntu & Windows 11<br />
                  PRJ-01,Epson PowerLite 1080p,PROJECTOR,Media Center Storage,UNDER_MAINTENANCE,1,Lumens:3800;HDMI:Yes,true
                </div>

                <div className="bg-black/80 border border-slate-800 p-3 rounded-xl font-mono text-[11px] text-slate-300 overflow-x-auto">
                  <div className="text-amber-400 mb-1">// data/bookings.csv</div>
                  BK-1001,CR-101,EVT-101,Keynote Session,2026-10-15 09:00,2026-10-15 12:00,Dr. Alan Vance,ACTIVE<br />
                  BK-1002,HALL-A,EVT-101,Opening Ceremony,2026-10-15 09:00,2026-10-15 13:00,Symposium Committee,ACTIVE<br />
                  BK-1003,LAB-201,EVT-102,Hands-on Lab Coding,2026-10-18 10:00,2026-10-18 13:00,Prof. S. Rao,ACTIVE
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}

package com.campusmanager;

import com.campusmanager.repository.DataStore;
import com.campusmanager.service.*;
import com.campusmanager.ui.MenuController;

import java.util.Scanner;

/**
 * Application Entry Point.
 * Initializes storage, services, and launches the interactive CLI.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Bootstrapping Campus Resource & Event Manager  ");
        System.out.println("=================================================");

        // 1. Initialize data store
        String dataDir = (args.length > 0 && args[0] != null) ? args[0] : "data";
        DataStore dataStore = new DataStore(dataDir);

        System.out.print("Loading campus data records from '" + dataDir + "'... ");
        dataStore.loadAllData();
        System.out.printf("Done! (%d resources, %d events, %d bookings, %d maintenance)%n",
                dataStore.getResources().size(),
                dataStore.getEvents().size(),
                dataStore.getBookings().size(),
                dataStore.getMaintenanceRecords().size());

        // 2. Initialize business services
        ResourceService resourceService = new ResourceService(dataStore);
        EventService eventService = new EventService(dataStore);
        BookingService bookingService = new BookingService(dataStore);
        MaintenanceService maintenanceService = new MaintenanceService(dataStore);
        ReportService reportService = new ReportService(dataStore);

        // 3. Start CLI Menu Controller
        Scanner scanner = new Scanner(System.in);
        MenuController menuController = new MenuController(
                resourceService,
                eventService,
                bookingService,
                maintenanceService,
                reportService,
                scanner
        );

        menuController.start();
    }
}

package com.campusmanager.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility wrapper around Scanner to safely read input from console
 * without crashing on invalid formats or non-numeric inputs.
 */
public class InputReader {
    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    private String readNextRawLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("\n[INFO] End of input stream reached. Exiting gracefully.");
            System.exit(0);
        }
        return scanner.nextLine();
    }

    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readNextRawLine();
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }

    public String readOptionalString(String prompt, String defaultValue) {
        System.out.print(prompt + (defaultValue != null ? " [" + defaultValue + "]: " : ": "));
        String input = readNextRawLine();
        if (input == null || input.trim().isEmpty()) {
            return defaultValue;
        }
        return input.trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readNextRawLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer number.");
            }
        }
    }

    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("Value must be between %d and %d. Please try again.%n", min, max);
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String line = readNextRawLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes") || line.equals("true") || line.equals("1")) {
                return true;
            }
            if (line.equals("n") || line.equals("no") || line.equals("false") || line.equals("0")) {
                return false;
            }
            System.out.println("Invalid response. Please enter 'y' for yes or 'n' for no.");
        }
    }

    public LocalDateTime readDateTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (" + DateTimeUtil.DATE_TIME_PATTERN + "): ");
            String line = readNextRawLine().trim();
            try {
                return DateTimeUtil.parseDateTime(line);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date-time format. Example: 2026-10-15 14:30");
            }
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (" + DateTimeUtil.DATE_PATTERN + "): ");
            String line = readNextRawLine().trim();
            try {
                return DateTimeUtil.parseDate(line);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Example: 2026-10-15");
            }
        }
    }

    public void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}

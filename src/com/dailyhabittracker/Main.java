package com.dailyhabittracker;

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static HabitService service = new HabitService();

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("       DAILY HABIT TRACKER");
        System.out.println("=================================");

        showMenu();

        scanner.close();
    }

    public static void showMenu() {

        while (true) {

            System.out.println("\nHABIT MENU");
            System.out.println("1. Add habit");
            System.out.println("2. View habits");
            System.out.println("3. Mark or correct a day");
            System.out.println("4. View habit progress");
            System.out.println("5. View and save weekly report");
            System.out.println("0. Exit");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addHabitMenu();
                    break;

                case 2:
                    service.showHabits();
                    break;

                case 3:
                    markDayMenu();
                    break;

                case 4:
                    progressMenu();
                    break;

                case 5:
                    reportMenu();
                    break;

                case 0:
                    System.out.println("Thank you for using Daily Habit Tracker.");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addHabitMenu() {

        System.out.println("\nHABIT TYPES");
        System.out.println("1. Study");
        System.out.println("2. Health");

        int type = readInt("Choose habit type: ");

        if (type != 1 && type != 2) {
            System.out.println("Invalid habit type.");
            return;
        }

        String name = readText("Enter habit name: ");

        service.addHabit(name, type);
    }

    private static void markDayMenu() {

        service.showHabits();

        if (service.isEmpty()) {
            return;
        }

        int id = readInt("Enter habit ID: ");

        System.out.println("\nDAYS");
        System.out.println("1. Monday");
        System.out.println("2. Tuesday");
        System.out.println("3. Wednesday");
        System.out.println("4. Thursday");
        System.out.println("5. Friday");
        System.out.println("6. Saturday");
        System.out.println("7. Sunday");

        int day = readInt("Choose day: ");

        if (day < 1 || day > 7) {
            System.out.println("Invalid day. Choose 1 to 7.");
            return;
        }

        System.out.println("\nDAY STATUS");
        System.out.println("1. Done");
        System.out.println("2. Skipped");

        int status = readInt("Choose status: ");

        if (status != 1 && status != 2) {
            System.out.println("Invalid status. Choose 1 or 2.");
            return;
        }

        service.markDay(id, day, status);
    }

    private static void progressMenu() {

        service.showHabits();

        if (service.isEmpty()) {
            return;
        }

        int id = readInt("Enter habit ID: ");

        service.showProgress(id);
    }

    private static void reportMenu() {

        String report = service.createReport();

        System.out.println("\n" + report);

        String choice = readText("Save report to report.txt? (Y/N): ");

        if (choice.equalsIgnoreCase("Y")) {
            service.saveReport();
        }
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readText(String message) {

        System.out.print(message);

        return scanner.nextLine();
    }
}
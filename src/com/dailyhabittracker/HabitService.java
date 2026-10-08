package com.dailyhabittracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class HabitService {

    private ArrayList<Habit> habits;
    private HashSet<String> names;
    private FileManager fileManager;
    private int nextId;

    private final String[] days = {
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    };

    public HabitService() {
        fileManager = new FileManager();
        habits = fileManager.loadHabits();
        names = new HashSet<>();

        nextId = 1;

        for (Habit habit : habits) {
            names.add(normalize(habit.getName()));

            if (habit.getId() >= nextId) {
                nextId = habit.getId() + 1;
            }
        }
    }

    private String normalize(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    public boolean addHabit(String name, int type) {

        String cleanedName = name.trim();

        if (cleanedName.isEmpty()) {
            System.out.println("Habit name cannot be empty.");
            return false;
        }

        if (cleanedName.contains("|") || cleanedName.contains("\n")
                || cleanedName.contains("\r")) {
            System.out.println("Invalid characters in habit name.");
            return false;
        }

        String key = normalize(cleanedName);

        if (names.contains(key)) {
            System.out.println("Habit already exists.");
            return false;
        }

        Habit habit;

        if (type == 1) {
            habit = new StudyHabit(nextId, cleanedName);
        } else if (type == 2) {
            habit = new HealthHabit(nextId, cleanedName);
        } else {
            System.out.println("Invalid habit type.");
            return false;
        }

        habits.add(habit);

        if (!fileManager.saveHabits(habits)) {
            habits.remove(habits.size() - 1);
            return false;
        }

        names.add(key);

        System.out.println("Habit " + nextId + " saved with seven Unmarked days.");

        nextId++;

        return true;
    }

    public void showHabits() {

        if (habits.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("\nHabit List");

        for (Habit habit : habits) {
            System.out.println(
                    "ID: " + habit.getId()
                    + " | Type: " + habit.getTypeLabel()
                    + " | Name: " + habit.getName()
            );
        }
    }

    private Habit findHabit(int id) {

        for (Habit habit : habits) {
            if (habit.getId() == id) {
                return habit;
            }
        }

        return null;
    }

    public void markDay(int id, int day, int status) {

        Habit habit = findHabit(id);

        if (habit == null) {
            System.out.println("Habit not found.");
            return;
        }

        if (day < 1 || day > 7) {
            System.out.println("Day must be between 1 and 7.");
            return;
        }

        if (status != 1 && status != 2) {
            System.out.println("Status must be 1 or 2.");
            return;
        }

        int[] oldProgress = habit.getProgress();

        habit.markDay(day, status);

        if (!fileManager.saveHabits(habits)) {
            habit.markDay(day, oldProgress[day - 1]);
            return;
        }

        System.out.println(days[day - 1] + " marked as "
                + (status == 1 ? "Done" : "Skipped") + ".");
    }

    public void showProgress(int id) {

        Habit habit = findHabit(id);

        if (habit == null) {
            System.out.println("Habit not found.");
            return;
        }

        int[] progress = habit.getProgress();

        int done = 0;
        int skipped = 0;
        int unmarked = 0;

        int current = 0;
        int longest = 0;

        for (int value : progress) {

            if (value == 1) {
                done++;
                current++;

                if (current > longest) {
                    longest = current;
                }

            } else if (value == 2) {
                skipped++;
                current = 0;

            } else {
                unmarked++;
                current = 0;
            }
        }

        System.out.println("\nHabit Progress");
        System.out.println("ID: " + habit.getId());
        System.out.println("Name: " + habit.getName());
        System.out.println("Type: " + habit.getTypeLabel());

        System.out.println("Done: " + done);
        System.out.println("Skipped: " + skipped);
        System.out.println("Unmarked: " + unmarked);
        System.out.println("Longest Done streak: " + longest + " days");

        System.out.println("\nWeekly Status");

        for (int i = 0; i < 7; i++) {

            String status;

            if (progress[i] == 1) {
                status = "Done";
            } else if (progress[i] == 2) {
                status = "Skipped";
            } else {
                status = "Unmarked";
            }

            System.out.println((i + 1) + ". " + days[i] + " - " + status);
        }
    }

    public String createReport() {

        int studyCount = 0;
        int healthCount = 0;
        int totalDone = 0;

        StringBuilder report = new StringBuilder();

        report.append("DAILY HABIT TRACKER - WEEKLY REPORT\n");
        report.append("===================================\n\n");

        if (habits.isEmpty()) {
            report.append("No records found.\n");
            report.append("\nStudy habits: 0\n");
            report.append("Health habits: 0\n");
            report.append("Total Done entries: 0\n");

            return report.toString();
        }

        for (Habit habit : habits) {

            if (habit.getTypeLabel().equals("Study")) {
                studyCount++;
            } else {
                healthCount++;
            }

            int[] progress = habit.getProgress();

            int done = 0;
            int current = 0;
            int longest = 0;

            for (int value : progress) {

                if (value == 1) {
                    done++;
                    totalDone++;
                    current++;

                    if (current > longest) {
                        longest = current;
                    }

                } else {
                    current = 0;
                }
            }

            report.append("ID: ")
                    .append(habit.getId())
                    .append(" | ")
                    .append(habit.getTypeLabel())
                    .append(" | ")
                    .append(habit.getName())
                    .append("\n");

            report.append("Completed days: ")
                    .append(done)
                    .append("\n");

            report.append("Longest Done streak: ")
                    .append(longest)
                    .append(" days\n\n");
        }

        report.append("Study habits: ")
                .append(studyCount)
                .append("\n");

        report.append("Health habits: ")
                .append(healthCount)
                .append("\n");

        report.append("Total Done entries: ")
                .append(totalDone)
                .append("\n");

        return report.toString();
    }

    public void showReport() {

        String report = createReport();

        System.out.println();
        System.out.println(report);

        System.out.print("Save report to report.txt? (Y/N): ");

    }

    public boolean saveReport() {

        String report = createReport();

        if (fileManager.saveReport(report)) {
            System.out.println("Report saved successfully.");
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        return habits.isEmpty();
    }
}
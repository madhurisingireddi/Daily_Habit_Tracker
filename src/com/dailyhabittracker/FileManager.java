package com.dailyhabittracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class FileManager {

    private final Path dataFolder = Path.of("data");
    private final Path habitsFile = dataFolder.resolve("habits.txt");
    private final Path reportFile = dataFolder.resolve("report.txt");

    public FileManager() {
        try {
            Files.createDirectories(dataFolder);
        } catch (IOException e) {
            System.out.println("Unable to create data folder.");
        }
    }

    public ArrayList<Habit> loadHabits() {
        ArrayList<Habit> habits = new ArrayList<>();

        if (!Files.exists(habitsFile)) {
            return habits;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                habitsFile, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split("\\|", -1);

                if (fields.length != 4) {
                    System.out.println("Invalid record at habits.txt line " + lineNumber);
                    return new ArrayList<>();
                }

                int id = Integer.parseInt(fields[0]);
                String type = fields[1];
                String name = fields[2];

                if (id <= 0 || name.trim().isEmpty()) {
                    System.out.println("Invalid record at habits.txt line " + lineNumber);
                    return new ArrayList<>();
                }

                String[] values = fields[3].split(",", -1);

                if (values.length != 7) {
                    System.out.println("Invalid record at habits.txt line " + lineNumber);
                    return new ArrayList<>();
                }

                int[] progress = new int[7];

                for (int i = 0; i < 7; i++) {
                    progress[i] = Integer.parseInt(values[i]);

                    if (progress[i] < 0 || progress[i] > 2) {
                        System.out.println("Invalid record at habits.txt line " + lineNumber);
                        return new ArrayList<>();
                    }
                }

                if (type.equalsIgnoreCase("STUDY")) {
                    habits.add(new StudyHabit(id, name, progress));
                } else if (type.equalsIgnoreCase("HEALTH")) {
                    habits.add(new HealthHabit(id, name, progress));
                } else {
                    System.out.println("Invalid record at habits.txt line " + lineNumber);
                    return new ArrayList<>();
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading habits.txt");
            return new ArrayList<>();
        }

        return habits;
    }

    public boolean saveHabits(ArrayList<Habit> habits) {

        Path tempFile = dataFolder.resolve("habits_temp.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(
                tempFile,
                StandardCharsets.UTF_8)) {

            for (Habit habit : habits) {

                int[] progress = habit.getProgress();

                writer.write(
                        habit.getId()
                        + "|" + habit.getTypeLabel().toUpperCase()
                        + "|" + habit.getName()
                        + "|" + progress[0]
                        + "," + progress[1]
                        + "," + progress[2]
                        + "," + progress[3]
                        + "," + progress[4]
                        + "," + progress[5]
                        + "," + progress[6]
                );

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving habits.txt");
            return false;
        }

        try {
            Files.move(
                    tempFile,
                    habitsFile,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            System.out.println("Error replacing habits.txt");
            return false;
        }

        return true;
    }

    public boolean saveReport(String report) {

        try (BufferedWriter writer = Files.newBufferedWriter(
                reportFile,
                StandardCharsets.UTF_8)) {

            writer.write(report);

        } catch (IOException e) {
            System.out.println("Error saving report.txt");
            return false;
        }

        return true;
    }
}


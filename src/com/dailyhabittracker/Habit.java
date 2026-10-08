package com.dailyhabittracker;

public abstract class Habit {

    private int id;
    private String name;
    private int[] progress;

    public Habit(int id, String name) {
        this.id = id;
        this.name = name;
        this.progress = new int[7];
    }

    public Habit(int id, String name, int[] progress) {
        this.id = id;
        this.name = name;
        this.progress = progress.clone();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getProgress() {
        return progress.clone();
    }

    public void markDay(int day, int status) {
        progress[day - 1] = status;
    }

    public abstract String getTypeLabel();
}


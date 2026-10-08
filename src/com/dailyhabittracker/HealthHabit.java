package com.dailyhabittracker;

public class HealthHabit extends Habit {

    public HealthHabit(int id, String name) {
        super(id, name);
    }

    public HealthHabit(int id, String name, int[] progress) {
        super(id, name, progress);
    }

    @Override
    public String getTypeLabel() {
        return "Health";
    }
}

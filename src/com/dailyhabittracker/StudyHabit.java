package com.dailyhabittracker;

public class StudyHabit extends Habit {

    public StudyHabit(int id, String name) {
        super(id, name);
    }

    public StudyHabit(int id, String name, int[] progress) {
        super(id, name, progress);
    }

    @Override
    public String getTypeLabel() {
        return "Study";
    }
}

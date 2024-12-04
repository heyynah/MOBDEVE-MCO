package com.mobdeve.s20.teves.hannah.mco;

import java.util.ArrayList;
import java.util.List;

public class HomeDailiesData {
    long id;
    String time;
    String task;

    HomeDailiesData(long id, String time, String task) {
        this.id = id;
        this.time = time;
        this.task = task;
    }

    // Sample data
    public static List<HomeDailiesData> getData() {
        List<HomeDailiesData> list = new ArrayList<>();
        list.add(new HomeDailiesData(1, "12:30 PM", "Complete your Daily Commissions."));
        list.add(new HomeDailiesData(2, "12:30 PM", "Farm for artifacts."));
        list.add(new HomeDailiesData(3, "12:30 PM", "Farm for materials."));
        return list;
    }

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "HomeDailiesData{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", task='" + task + '\'' +
                '}';
    }

    public static void addTask(List<HomeDailiesData> list, long id, String time, String task) {
        list.add(new HomeDailiesData(id, time, task));
    }
}

package com.team195.json;

import java.util.List;

public class TrajectoryJson {
    public int id;
    public String name;
    public boolean reversed;
    public List<Pose2dJson> waypoints;
    public double maxVel;
    public double maxAccel;
    public double maxVoltage;
    public double maxCentripetalAccel;

    public TrajectoryJson(int id, String name, boolean reversed, List<Pose2dJson> waypoints, double maxVel, double maxAccel, double maxVoltage, double maxCentripetalAccel) {
        this.id = id;
        this.name = name;
        this.reversed = reversed;
        this.waypoints = waypoints;
        this.maxVel = maxVel;
        this.maxAccel = maxAccel;
        this.maxVoltage = maxVoltage;
        this.maxCentripetalAccel = maxCentripetalAccel;
    }

    public TrajectoryJson(int id, String name, boolean reversed, List<Pose2dJson> waypoints) {
        this(id, name, reversed, waypoints, -1, -1, -1, -1);
    }
}

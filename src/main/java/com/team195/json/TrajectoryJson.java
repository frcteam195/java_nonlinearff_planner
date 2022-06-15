package com.team195.json;

import com.google.gson.*;
import com.team254.lib.geometry.Pose2d;
import java.util.List;

public class TrajectoryJson {
    public int id;
    public String name;
    public boolean reversed;
    public List<Pose2dJson> waypoints;

    public TrajectoryJson(int id, String name, boolean reversed, List<Pose2dJson> waypoints) {
        this.id = id;
        this.name = name;
        this.reversed = reversed;
        this.waypoints = waypoints;
    }
}

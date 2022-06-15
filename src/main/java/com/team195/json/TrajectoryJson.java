package com.team195.json;

import com.google.gson.*;
import com.team254.lib.geometry.Pose2d;
import java.util.List;

public class TrajectoryJson {
    public int id;
    public String name;
    public boolean reversed;
    public List<Pose2d> waypoints;
}

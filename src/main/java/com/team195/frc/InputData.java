package com.team195.frc;

import com.team254.lib.geometry.Pose2d;

public class InputData {
    public double timestamp = 0;
    public Pose2d poseInches = Pose2d.identity();
    public boolean forceStop = false;
    public boolean beginTrajectory = false;
    public int trajectoryID = 0;
}

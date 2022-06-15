package com.team195.json;

public class Pose2dJson {
    public double x;
    public double y;
    public double theta;
    public String comment;

    public Pose2dJson(double x, double y, double theta, String comment)
    {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.comment = comment;
    }
}

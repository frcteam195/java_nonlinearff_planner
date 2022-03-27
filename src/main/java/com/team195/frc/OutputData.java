package com.team195.frc;

public class OutputData {
    public double leftMotorOutputRadPerSec = 0;
    public double leftMotorFeedForwardVoltage = 0;
    public double leftMotorAccelRadPerSec2 = 0;
    public double rightMotorOutputRadPerSec = 0;
    public double rightMotorFeedForwardVoltage = 0;
    public double rightMotorAccelRadPerSec2 = 0;
    public boolean trajectoryActive = false;
    public boolean trajectoryCompleted = false;

    public void set(double leftMotorOutputRadPerSec, double leftMotorFeedForwardVoltage,
                    double leftMotorAccelRadPerSec2, double rightMotorOutputRadPerSec,
                    double rightMotorFeedForwardVoltage, double rightMotorAccelRadPerSec2,
                    boolean trajectoryActive, boolean trajectoryCompleted)
    {
        this.leftMotorOutputRadPerSec = leftMotorOutputRadPerSec;
        this.leftMotorFeedForwardVoltage = leftMotorFeedForwardVoltage;
        this.leftMotorAccelRadPerSec2 = leftMotorAccelRadPerSec2;
        this.rightMotorOutputRadPerSec = rightMotorOutputRadPerSec;
        this.rightMotorFeedForwardVoltage = rightMotorFeedForwardVoltage;
        this.rightMotorAccelRadPerSec2 = rightMotorAccelRadPerSec2;
        this.trajectoryActive = trajectoryActive;
        this.trajectoryCompleted = trajectoryCompleted;
    }

    public void setZeros()
    {
        set(0, 0, 0, 0, 0, 0, false, false);
    }

    public OutputData()
    {
        setZeros();
    }
}

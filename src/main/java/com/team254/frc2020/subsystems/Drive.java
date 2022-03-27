package com.team254.frc2020.subsystems;

import com.team254.frc2020.Constants;
import com.team254.frc2020.planners.DriveMotionPlanner;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.trajectory.TrajectoryIterator;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.util.DriveOutput;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.ReflectingCSVWriter;
import com.team254.lib.util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Drive {

    // control states
    private DriveControlState mDriveControlState;

    private DriveMotionPlanner mMotionPlanner;
    private boolean mOverrideTrajectory = false;

    private Drive() {
        mPeriodicIO = new PeriodicIO();

        mMotionPlanner = new DriveMotionPlanner();
    }

    private PeriodicIO mPeriodicIO;
    private ReflectingCSVWriter<PeriodicIO> mCSVWriter = null;

    public static class PeriodicIO {
        // real outputs
        public double left_demand;
        public double right_demand;

        // INPUTS
        public double timestamp;
        public double left_voltage;
        public double right_voltage;
        public int left_position_ticks; // using us digital encoder
        public int right_position_ticks; // us digital encoder
        public double left_distance;
        public double right_distance;
        public int left_velocity_ticks_per_100ms; // using Talon FX
        public int right_velocity_ticks_per_100ms; // Talon FX
        public double left_velocity_in_per_sec;
        public double right_velocity_in_per_sec;
        public Rotation2d gyro_heading = Rotation2d.identity();

        // OUTPUTS
        public double left_accel;
        public double right_accel;
        public double left_feedforward;
        public double right_feedforward;
        public Pose2d error = Pose2d.identity();
        public TimedState<Pose2dWithCurvature> path_setpoint = new TimedState<Pose2dWithCurvature>(Pose2dWithCurvature.identity());
    }


    public synchronized void writePeriodicOutputs() {
//        if (mDriveControlState == DriveControlState.OPEN_LOOP) {
//            mLeftSide.getDriveTalons().forEach(t -> t.set(ControlMode.PercentOutput, mPeriodicIO.left_demand));
//            mRightSide.getDriveTalons().forEach(t -> t.set(ControlMode.PercentOutput, mPeriodicIO.right_demand));
//        } else if (mDriveControlState == DriveControlState.VELOCITY || mDriveControlState == DriveControlState.PATH_FOLLOWING) {
//            double kd = isHighGear() ? Constants.kDriveHighGearKd : Constants.kDriveLowGearKd;
//            mLeftSide.getDriveTalons().forEach(t -> t.set(ControlMode.Velocity, mPeriodicIO.left_demand, DemandType.ArbitraryFeedForward,
//                    mPeriodicIO.left_feedforward + kd * mPeriodicIO.left_accel / 1023.0));
//            mRightSide.getDriveTalons().forEach(t -> t.set(ControlMode.Velocity, mPeriodicIO.right_demand, DemandType.ArbitraryFeedForward,
//                    mPeriodicIO.right_feedforward + kd * mPeriodicIO.right_accel / 1023.0));
//        }
    }



    /**
     * @param rad_s of the output
     * @return ticks per 100 ms of the talonfx encoder
     */
    private double radiansPerSecondToTicksPer100ms(double rad_s) {
        return rad_s / (Math.PI * 2.0) / getRotationsPerTickVelocity() / 10.0;
    }


    /**
     * Configure talons for following via the ramsete controller
     */
    public synchronized void setRamseteVelocity(DriveSignal signal, DriveSignal feedforward) {
        if (mDriveControlState != DriveControlState.PATH_FOLLOWING) {
//            setBrakeMode(true);
            mDriveControlState = DriveControlState.PATH_FOLLOWING;
//            configureTalonPIDSlot();
        }
        mPeriodicIO.left_demand = signal.getLeft();
        mPeriodicIO.right_demand = signal.getRight();
        mPeriodicIO.left_feedforward = feedforward.getLeft();
        mPeriodicIO.right_feedforward = feedforward.getRight();
    }

    public synchronized void setTrajectory(TrajectoryIterator<TimedState<Pose2dWithCurvature>> trajectory) {
        if (mMotionPlanner != null) {
            mOverrideTrajectory = false;
            mMotionPlanner.reset();
            mMotionPlanner.setTrajectory(trajectory);
            mDriveControlState = DriveControlState.PATH_FOLLOWING;
        }
    }

    public boolean isDoneWithTrajectory() {
        if (mMotionPlanner == null || mDriveControlState != DriveControlState.PATH_FOLLOWING) {
            return false;
        }
        return mMotionPlanner.isDone() || mOverrideTrajectory;
    }

    public void overrideTrajectory(boolean value) {
        mOverrideTrajectory = value;
    }

    private void updatePathFollower() {
        if (mDriveControlState == DriveControlState.PATH_FOLLOWING) {
            final double now = 0.0;//Timer.getFPGATimestamp();
            final Pose2d robotFieldToVehicleNowDummyPose = new Pose2d();

            DriveOutput output = mMotionPlanner.update(now, robotFieldToVehicleNowDummyPose);

//            mPeriodicIO.error = mMotionPlanner.error();
//            mPeriodicIO.path_setpoint = mMotionPlanner.setpoint();

            if (!mOverrideTrajectory) {
                setRamseteVelocity(new DriveSignal(radiansPerSecondToTicksPer100ms(output.left_velocity), radiansPerSecondToTicksPer100ms(output.right_velocity)),
                        new DriveSignal(output.left_feedforward_voltage / 12.0, output.right_feedforward_voltage / 12.0));

                mPeriodicIO.left_accel = radiansPerSecondToTicksPer100ms(output.left_accel) / 1000.0;
                mPeriodicIO.right_accel = radiansPerSecondToTicksPer100ms(output.right_accel) / 1000.0;
            } else {
                setRamseteVelocity(DriveSignal.BRAKE, DriveSignal.BRAKE);
                mPeriodicIO.left_accel = mPeriodicIO.right_accel = 0.0;
            }
        } else {

        }
    }

    /**
     * @return conversion factor where ticks * getEncoderTicksPerRotation() = wheel rotations
     */
    public double getRotationsPerTickVelocity() { // talonfx
        boolean isHighGear = false;
        return isHighGear ? Constants.kDriveRotationsPerTickHighGear : Constants.kDriveRotationsPerTickLowGear;
    }

    public enum DriveControlState {
        OPEN_LOOP, // open loop voltage control,
        VELOCITY, // velocity control
        PATH_FOLLOWING
    }

}
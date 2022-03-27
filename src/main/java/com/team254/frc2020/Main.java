package com.team254.frc2020;

import com.team254.frc2020.paths.TrajectoryGenerator;
import com.team254.frc2020.planners.DriveMotionPlanner;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.trajectory.TimedView;
import com.team254.lib.trajectory.TrajectoryIterator;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.util.DriveOutput;

import java.util.concurrent.atomic.AtomicBoolean;

public final class Main {
    private Main() {}

    private static class InputData
    {
        public double timestamp = 0;
        public Pose2d pose = Pose2d.identity();
        public boolean forceStop = false;
        public boolean beginTrajectory = false;
        public int trajectoryID = 0;
    }

    private static class OutputData
    {
        public double leftMotorOutputRadPerSec = 0;
        public double leftMotorFeedForwardVoltage = 0;
        public double leftMotorAccelRadPerSec2 = 0;
        public double rightMotorOutputRadPerSec = 0;
        public double rightMotorFeedForwardVoltage = 0;
        public double rightMotorAccelRadPerSec2 = 0;
        public boolean trajectoryActive = false;
        public boolean trajectoryCompleted = false;
    }

    private static AtomicBoolean runThread = new AtomicBoolean(true);
    private static Thread mDrivePlannerThread;
    private static InputData mInputData = new InputData();
    private static OutputData mOutputData = new OutputData();

    public static void main(String... args)
    {
        TrajectoryGenerator.getInstance().generateTrajectories();

        mDrivePlannerThread = new Thread(() ->
        {
            DriveMotionPlanner motionPlanner = new DriveMotionPlanner();
            TrajectoryIterator<TimedState<Pose2dWithCurvature>> mTrajectory;
            while (runThread.get())
            {
                if (!mOutputData.trajectoryActive)
                {
                    if (mInputData.beginTrajectory)
                    {
                        motionPlanner.reset();
                        mTrajectory = new TrajectoryIterator<>(new TimedView<>(TrajectoryGenerator.getInstance().trajectoryLookupMap.get(mInputData.trajectoryID)));
                        motionPlanner.setTrajectory(mTrajectory);
                        mOutputData.trajectoryActive = true;
                        mOutputData.trajectoryCompleted = false;
                    }
                }

                if (mOutputData.trajectoryActive && !mOutputData.trajectoryCompleted)
                {
                    DriveOutput d = motionPlanner.update(mInputData.timestamp, mInputData.pose);
                    mOutputData.leftMotorOutputRadPerSec = d.left_velocity;
                    mOutputData.leftMotorFeedForwardVoltage = d.left_feedforward_voltage;
                    mOutputData.leftMotorAccelRadPerSec2 = d.left_accel;

                    mOutputData.rightMotorOutputRadPerSec = d.right_velocity;
                    mOutputData.rightMotorFeedForwardVoltage = d.right_feedforward_voltage;
                    mOutputData.rightMotorAccelRadPerSec2 = d.right_accel;
                    mOutputData.trajectoryActive = true;
                }
                else
                {
                    mOutputData.leftMotorOutputRadPerSec = 0;
                    mOutputData.leftMotorFeedForwardVoltage = 0;
                    mOutputData.leftMotorAccelRadPerSec2 = 0;
                    mOutputData.rightMotorOutputRadPerSec = 0;
                    mOutputData.rightMotorFeedForwardVoltage = 0;
                    mOutputData.rightMotorAccelRadPerSec2 = 0;
                    mOutputData.trajectoryActive = false;
                }

                if (motionPlanner.isDone() || mInputData.forceStop)
                {
                    mOutputData.trajectoryActive = false;
                    mOutputData.trajectoryCompleted = true;
                    mOutputData.leftMotorOutputRadPerSec = 0;
                    mOutputData.leftMotorFeedForwardVoltage = 0;
                    mOutputData.leftMotorAccelRadPerSec2 = 0;
                    mOutputData.rightMotorOutputRadPerSec = 0;
                    mOutputData.rightMotorFeedForwardVoltage = 0;
                    mOutputData.rightMotorAccelRadPerSec2 = 0;
                }
            }
        });
        mDrivePlannerThread.start();

        try
        {
            mDrivePlannerThread.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}

package com.team195.frc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.team254.frc2020.paths.TrajectoryGenerator;
import com.team254.frc2020.planners.DriveMotionPlanner;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.trajectory.TimedView;
import com.team254.lib.trajectory.TrajectoryIterator;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.util.DriveOutput;
import com.team195.protos.PlannerInput;
import com.team195.protos.PlannerOutput;
import com.team254.lib.util.Units;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Main {
    private Main() {}

    private static final AtomicBoolean runThread = new AtomicBoolean(true);
    private static final InputData mInputData = new InputData();
    private static final OutputData mOutputData = new OutputData();
    private static final PlannerOutput.Builder outputProtoBuilder = PlannerOutput.newBuilder();

    private static final byte[] mInputByteArr = new byte[16384];
    private static byte[] mOutputByteArr = null;

    private static InetAddress mIPAddress;
    private static DatagramSocket mClientSocket;
    private static DatagramPacket mSendPacket;
    private static DatagramPacket mReceivePacket;

    private static final int PORT_NUM = 5803;

    public static void main(String... args)
    {
        Arrays.fill(mInputByteArr, (byte)0);
        TrajectoryGenerator.getInstance().generateTrajectories();

        Thread mDrivePlannerThread = new Thread(() ->
        {
            DriveMotionPlanner motionPlanner = new DriveMotionPlanner();
            TrajectoryIterator<TimedState<Pose2dWithCurvature>> mTrajectory;
            while (runThread.get())
            {
                if (mClientSocket == null)
                {
                    try {
                        mClientSocket = new DatagramSocket(PORT_NUM);
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                        mClientSocket = null;
                        continue;
                    }
                }

                if (mIPAddress == null)
                {
                    try {
                        mIPAddress = InetAddress.getByName("10.1.95.5");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        mIPAddress = null;
                        continue;
                    }
                }

                if (mReceivePacket == null)
                {
                    mReceivePacket = new DatagramPacket(mInputByteArr, mInputByteArr.length, new InetSocketAddress(0).getAddress(), PORT_NUM);
                }

                try {
                    mClientSocket.receive(mReceivePacket);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    continue;
                }

                PlannerInput plannerNetInput;
                try
                {
                    plannerNetInput = PlannerInput.parseFrom(mReceivePacket.getData());
                }
                catch (InvalidProtocolBufferException e)
                {
                    e.printStackTrace();
                    plannerNetInput = null;
                }

                if (plannerNetInput != null) {
                    mInputData.timestamp = plannerNetInput.getTimestamp();
                    mInputData.poseInches = new Pose2d(
                            Units.meters_to_inches(plannerNetInput.getPose().getX()),
                            Units.meters_to_inches(plannerNetInput.getPose().getY()),
                            Rotation2d.fromRadians(plannerNetInput.getPose().getYaw())
                    );
                    mInputData.beginTrajectory = plannerNetInput.getBeginTrajectory();
                    mInputData.forceStop = plannerNetInput.getForceStop();
                    mInputData.trajectoryID = plannerNetInput.getTrajectoryId();

                    if (!mOutputData.trajectoryActive) {
                        if (mInputData.beginTrajectory) {
                            motionPlanner.reset();
                            mTrajectory = new TrajectoryIterator<>(new TimedView<>(TrajectoryGenerator.getInstance()
                                    .trajectoryLookupMap.get(mInputData.trajectoryID)));
                            motionPlanner.setTrajectory(mTrajectory);
                            mOutputData.trajectoryActive = true;
                            mOutputData.trajectoryCompleted = false;
                        }
                    }

                    if (mOutputData.trajectoryActive && !mOutputData.trajectoryCompleted) {
                        DriveOutput d = motionPlanner.update(mInputData.timestamp, mInputData.poseInches);
                        mOutputData.leftMotorOutputRadPerSec = d.left_velocity;
                        mOutputData.leftMotorFeedForwardVoltage = d.left_feedforward_voltage;
                        mOutputData.leftMotorAccelRadPerSec2 = d.left_accel;

                        mOutputData.rightMotorOutputRadPerSec = d.right_velocity;
                        mOutputData.rightMotorFeedForwardVoltage = d.right_feedforward_voltage;
                        mOutputData.rightMotorAccelRadPerSec2 = d.right_accel;
                        mOutputData.trajectoryActive = true;
                    } else {
                        boolean tmpTrajCompleted = mOutputData.trajectoryCompleted;
                        mOutputData.setZeros();
                        mOutputData.trajectoryCompleted = tmpTrajCompleted;
                    }

                    if (motionPlanner.isDone() || mInputData.forceStop) {
                        mOutputData.setZeros();
                        mOutputData.trajectoryCompleted = true;
                    }
                }
                else
                {
                    mOutputData.setZeros();
                }

                mOutputByteArr = outputProtoBuilder
                    .setLeftMotorOutputRadPerSec(mOutputData.leftMotorOutputRadPerSec)
                    .setLeftMotorFeedForwardVoltage(mOutputData.leftMotorFeedForwardVoltage)
                    .setLeftMotorAccelRadPerSec2(mOutputData.leftMotorAccelRadPerSec2)
                    .setRightMotorOutputRadPerSec(mOutputData.rightMotorOutputRadPerSec)
                    .setRightMotorFeedForwardVoltage(mOutputData.rightMotorFeedForwardVoltage)
                    .setRightMotorAccelRadPerSec2(mOutputData.rightMotorAccelRadPerSec2)
                    .setTrajectoryActive(mOutputData.trajectoryActive)
                    .setTrajectoryCompleted(mOutputData.trajectoryCompleted)
                    .build().toByteArray();

                mSendPacket = new DatagramPacket(mOutputByteArr, mOutputByteArr.length, mIPAddress, PORT_NUM);
                try {
                    mClientSocket.send(mSendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
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

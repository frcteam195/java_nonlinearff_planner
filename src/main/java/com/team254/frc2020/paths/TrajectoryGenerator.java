package com.team254.frc2020.paths;

import com.team195.json.Pose2dJson;
import com.team195.json.TrajectoryJson;
import com.team195.json.TrajectoryLoader;
import com.team254.frc2020.planners.DriveMotionPlanner;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Translation2d;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.CentripetalAccelerationConstraint;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.trajectory.timing.TimingConstraint;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class TrajectoryGenerator {
    // TODO tune
    private static final double kMaxVel = 150.0;
    private static final double kMaxAccel = 100.0;
    private static final double kMaxVoltage = 9.0;
    private static final double kMaxCentripetalAccel = 60.0;

    private static TrajectoryGenerator mInstance = new TrajectoryGenerator();
    private static final DriveMotionPlanner mMotionPlanner = new DriveMotionPlanner();
    private TrajectorySet mTrajectorySet = null;

    public static TrajectoryGenerator getInstance() {
        return mInstance;
    }

    private TrajectoryGenerator() {}

    public HashMap<Integer, Trajectory<TimedState<Pose2dWithCurvature>>> trajectoryLookupMap = new HashMap<>();

    public void generateTrajectories() {
        generateTrajectories(null);
    }

    public void generateTrajectories(ArrayList<TrajectoryJson> arrTraj)
    {
        if (mTrajectorySet == null) {
            System.out.println("Generating trajectories...");
            if (arrTraj != null) {
                System.out.println("Generating trajectories from the JSON array.");
                mTrajectorySet = new TrajectorySet(arrTraj);
            }
            else
            {
                System.out.println("Warning! JSON array was NULL!");
                System.out.println("Generating test trajectories only!");
                mTrajectorySet = new TrajectorySet();
            }
            System.out.println("Finished trajectory generation.");
        }
    }

    public TrajectorySet getTrajectorySet() {
        return mTrajectorySet;
    }

    public static Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
            boolean reversed,
            final List<Pose2dJson> waypoints,
            final List<TimingConstraint<Pose2dWithCurvature>> constraints,
            double max_vel,  // inches/s
            double max_accel,  // inches/s^2
            double max_voltage) {
        List<Pose2d> wp = new ArrayList<>();
        for (Pose2dJson w: waypoints) {
            wp.add(new Pose2d(w.x, w.y, Rotation2d.fromDegrees(w.theta)));
        }
        return mMotionPlanner.generateTrajectory(reversed, wp, constraints, max_vel, max_accel, max_voltage);
    }

//    public static Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
//            boolean reversed,
//            final List<Pose2d> waypoints,
//            final List<TimingConstraint<Pose2dWithCurvature>> constraints,
//            double max_vel,  // inches/s
//            double max_accel,  // inches/s^2
//            double max_voltage) {
//        return mMotionPlanner.generateTrajectory(reversed, waypoints, constraints, max_vel, max_accel, max_voltage);
//    }

//    public static Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
//            boolean reversed,
//            final List<Pose2d> waypoints,
//            final List<TimingConstraint<Pose2dWithCurvature>> constraints,
//            double start_vel,  // inches/s
//            double end_vel,  // inches/s
//            double max_vel,  // inches/s
//            double max_accel,  // inches/s^2
//            double max_voltage) {
//        return mMotionPlanner.generateTrajectory(reversed, waypoints, constraints, start_vel, end_vel, max_vel, max_accel, max_voltage);
//    }

    @SuppressWarnings("DuplicatedCode")
    public class TrajectorySet {
        //Test
        public final Trajectory<TimedState<Pose2dWithCurvature>> testTrajectory;
        public final Trajectory<TimedState<Pose2dWithCurvature>> testTrajectoryBack;

        private TrajectorySet()
        {
            //Test
            testTrajectory = getTestTrajectory();
            testTrajectoryBack = getTestTrajectoryBack();
        }

        private TrajectorySet(ArrayList<TrajectoryJson> arrTraj)
        {
            testTrajectory = getTestTrajectory();
            testTrajectoryBack = getTestTrajectoryBack();



            for (TrajectoryJson tj: arrTraj) {
                 Trajectory<TimedState<Pose2dWithCurvature>> generated_trajectory =
                         generateTrajectory(tj.reversed, tj.waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)), kMaxVel, kMaxAccel, kMaxVoltage);

                 System.out.println("Putting Trajectory ID " + tj.id + " in the map.");
                 // System.out.println(generated_trajectory);

                 trajectoryLookupMap.put(tj.id, generated_trajectory);

                 // System.out.println(trajectoryLookupMap);
            }
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getTestTrajectory() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(new Pose2dJson(0, 0, 180, ""));
            waypoints.add(new Pose2dJson(-120, 120, 90, ""));
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getTestTrajectoryBack() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(new Pose2dJson(-120, 120, 90, ""));
            waypoints.add(new Pose2dJson(0, 0, 180, ""));
            return generateTrajectory(true, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }
    }
}
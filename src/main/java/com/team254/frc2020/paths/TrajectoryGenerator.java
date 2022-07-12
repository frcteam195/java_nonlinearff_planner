package com.team254.frc2020.paths;

import com.team195.json.Pose2dJson;
import com.team195.json.TrajectoryJson;
import com.team254.frc2020.planners.DriveMotionPlanner;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.CentripetalAccelerationConstraint;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.trajectory.timing.TimingConstraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrajectoryGenerator {
    // TODO tune default values
    private static final double kMaxVel = 150.0;
    private static final double kMaxAccel = 100.0;
    private static final double kMaxVoltage = 9.0;
    private static final double kMaxCentripetalAccel = 60.0;

    private static final TrajectoryGenerator mInstance = new TrajectoryGenerator();
    private static final DriveMotionPlanner mMotionPlanner = new DriveMotionPlanner();

    private TrajectorySet mTrajectorySet = null;

    public static TrajectoryGenerator getInstance() {
        return mInstance;
    }

    private TrajectoryGenerator() {}

    public HashMap<Integer, Trajectory<TimedState<Pose2dWithCurvature>>> trajectoryLookupMap = new HashMap<>();

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

            for (TrajectoryJson tj: arrTraj)
            {
                //////////////////////////////////////////////////////////////////////////
                //If the json has particular constraints, use those, else, use the defaults
                double maxVel = tj.maxVel > 0 ? tj.maxVel : kMaxVel;
                double maxAccel = tj.maxAccel > 0 ? tj.maxAccel : kMaxAccel;
                double maxVoltage = tj.maxVoltage > 0 ? tj.maxVoltage : kMaxVoltage;
                double maxCentripetalAccel = tj.maxCentripetalAccel > 0 ? tj.maxCentripetalAccel : kMaxCentripetalAccel;
                //////////////////////////////////////////////////////////////////////////

                 Trajectory<TimedState<Pose2dWithCurvature>> generated_trajectory =
                         generateTrajectory(tj.reversed, tj.waypoints, List.of(new CentripetalAccelerationConstraint(maxCentripetalAccel)), maxVel, maxAccel, maxVoltage);

                 System.out.println("Putting Trajectory ID " + tj.id + " in the map.");
                 trajectoryLookupMap.put(tj.id, generated_trajectory);
            }
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getTestTrajectory() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(new Pose2dJson(0, 0, 180, ""));
            waypoints.add(new Pose2dJson(-120, 120, 90, ""));
            return generateTrajectory(false, waypoints, List.of(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getTestTrajectoryBack() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(new Pose2dJson(-120, 120, 90, ""));
            waypoints.add(new Pose2dJson(0, 0, 180, ""));
            return generateTrajectory(true, waypoints, List.of(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }
    }
}
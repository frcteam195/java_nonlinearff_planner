package com.team254.frc2020.paths;

import com.team254.frc2020.planners.DriveMotionPlanner;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Translation2d;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.CentripetalAccelerationConstraint;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.trajectory.timing.TimingConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TrajectoryGenerator {
    // TODO tune
    private static final double kMaxVel = 150.0;
    private static final double kMaxAccel = 100.0;
    private static final double kMaxVoltage = 9.0;
    private static final double kMaxCentripetalAccel = 60.0;

    private static TrajectoryGenerator mInstance = new TrajectoryGenerator();
    private final DriveMotionPlanner mMotionPlanner;
    private TrajectorySet mTrajectorySet = null;

    public static TrajectoryGenerator getInstance() {
        return mInstance;
    }

    private TrajectoryGenerator() {
        mMotionPlanner = new DriveMotionPlanner();
    }

    public HashMap<Integer, Trajectory<TimedState<Pose2dWithCurvature>>> trajectoryLookupMap = new HashMap<>();

    public void generateTrajectories() {
        if (mTrajectorySet == null) {
            System.out.println("Generating trajectories...");
            mTrajectorySet = new TrajectorySet();
            trajectoryLookupMap.put(0, mTrajectorySet.start1ToPickupBall3);
            trajectoryLookupMap.put(1, mTrajectorySet.pickupBall3ToPickupBall2and4);
            trajectoryLookupMap.put(2, mTrajectorySet.pickupBall4ToPickupBall1);
            System.out.println("Finished trajectory generation");
        }
    }

    public TrajectorySet getTrajectorySet() {
        return mTrajectorySet;
    }

    public Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
            boolean reversed,
            final List<Pose2d> waypoints,
            final List<TimingConstraint<Pose2dWithCurvature>> constraints,
            double max_vel,  // inches/s
            double max_accel,  // inches/s^2
            double max_voltage) {
        return mMotionPlanner.generateTrajectory(reversed, waypoints, constraints, max_vel, max_accel, max_voltage);
    }

    public Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
            boolean reversed,
            final List<Pose2d> waypoints,
            final List<TimingConstraint<Pose2dWithCurvature>> constraints,
            double start_vel,  // inches/s
            double end_vel,  // inches/s
            double max_vel,  // inches/s
            double max_accel,  // inches/s^2
            double max_voltage) {
        return mMotionPlanner.generateTrajectory(reversed, waypoints, constraints, start_vel, end_vel, max_vel, max_accel, max_voltage);
    }

    public final Pose2d kStartPose1 = new Pose2d(7.6708, -6.223, Rotation2d.fromDegrees(-90));  //302,-245
    public final Pose2d kStartPose2 = new Pose2d(6.223, -3.175, Rotation2d.fromDegrees(135));   //
    public final Pose2d kBall1 = new Pose2d(4.953, -2.032, Rotation2d.fromDegrees(0));   //
    public final Pose2d kBall2 = new Pose2d(5.08, -6.3754, Rotation2d.fromDegrees(0));  //200,251
    public final Pose2d kBall3 = new Pose2d(7.5692, -7.9502, Rotation2d.fromDegrees(0));    //298,-313
    public final Pose2d kBall4 = new Pose2d(1.0668, 7.112, Rotation2d.fromDegrees(0));  //

    public final Pose2d kBall3Pickup = new Pose2d(7.6454, -7.0612, Rotation2d.fromDegrees(-92));   //301,-278

    public final Pose2d kBall2Pickup = new Pose2d(5.588, -6.7818, Rotation2d.fromDegrees(140));  //220,-267

    public final Pose2d kBall4Pickup = new Pose2d(1.524, -6.604, Rotation2d.fromDegrees(-135));  //60,-260

    public final Pose2d kBall4PickupAlternate = new Pose2d(1.651, -7.0612, Rotation2d.fromDegrees(145));  //65,-278

    public final Pose2d kBall1PickupViaPoint1 = new Pose2d(1.397, -4.9276, Rotation2d.fromDegrees(50));  //55,-194
    public final Pose2d kBall1Pickup = new Pose2d(4.826, -2.413, Rotation2d.fromDegrees(69));  //190,-95

    public final Pose2d kBall1PickupViaPoint1Alternate = new Pose2d(1.651, -2.667, Rotation2d.fromDegrees(90));  //65,-105
    public final Pose2d kBall1PickupAlternate = new Pose2d(4.572, -1.905, Rotation2d.fromDegrees(-15));  //180,-75



    @SuppressWarnings("DuplicatedCode")
    public class TrajectorySet {
        public final Trajectory<TimedState<Pose2dWithCurvature>> testTrajectory;
        public final Trajectory<TimedState<Pose2dWithCurvature>> testTrajectoryBack;
        public final Trajectory<TimedState<Pose2dWithCurvature>> start1ToPickupBall3;
        public final Trajectory<TimedState<Pose2dWithCurvature>> pickupBall3ToPickupBall2and4;
        public final Trajectory<TimedState<Pose2dWithCurvature>> pickupBall4ToPickupBall1;

        private TrajectorySet() {
            testTrajectory = getTestTrajectory();
            testTrajectoryBack = getTestTrajectoryBack();
            start1ToPickupBall3 = getStart1ToPickupBall3();
            pickupBall3ToPickupBall2and4 = getPickupBall3ToPickupBall2and4();
            pickupBall4ToPickupBall1 = getPickupBall4ToPickupBall1();
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getTestTrajectory() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(new Pose2d(Translation2d.identity(), Rotation2d.fromDegrees(180)));
            waypoints.add(new Pose2d(-120, 120, Rotation2d.fromDegrees(90)));
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getTestTrajectoryBack() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(new Pose2d(-120, 120, Rotation2d.fromDegrees(90)));
            waypoints.add(new Pose2d(Translation2d.identity(), Rotation2d.fromDegrees(180)));
            return generateTrajectory(true, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getStart1ToPickupBall3() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kStartPose1);
            waypoints.add(kBall3Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getPickupBall3ToPickupBall2and4() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kBall3Pickup);
            waypoints.add(kBall2Pickup);
            waypoints.add(kBall4PickupAlternate);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        private Trajectory<TimedState<Pose2dWithCurvature>> getPickupBall4ToPickupBall1() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kBall4PickupAlternate);
            waypoints.add(kBall1PickupViaPoint1Alternate);
            waypoints.add(kBall1PickupAlternate);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }
    }
}
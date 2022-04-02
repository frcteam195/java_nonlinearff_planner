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
    private static final DriveMotionPlanner mMotionPlanner = new DriveMotionPlanner();
    private TrajectorySet mTrajectorySet = null;

    public static TrajectoryGenerator getInstance() {
        return mInstance;
    }

    private TrajectoryGenerator() {}

    public HashMap<Integer, Trajectory<TimedState<Pose2dWithCurvature>>> trajectoryLookupMap = new HashMap<>();

    public void generateTrajectories() {
        if (mTrajectorySet == null) {
            System.out.println("Generating trajectories...");
            mTrajectorySet = new TrajectorySet();
            trajectoryLookupMap.put(0, mTrajectorySet.auto1_start1ToPickupBall3);
            trajectoryLookupMap.put(1, mTrajectorySet.auto1_pickupBall3ToPickupBall2and7);
            trajectoryLookupMap.put(2, mTrajectorySet.auto2_start2ToPickupBall1);
            trajectoryLookupMap.put(3, mTrajectorySet.auto3_start1ToPickupBall3);
            trajectoryLookupMap.put(4, mTrajectorySet.auto3_pickupBall3ToPickupBall2);
            trajectoryLookupMap.put(5, mTrajectorySet.auto3_pickupBall2ToPickupBall7);
            trajectoryLookupMap.put(6, mTrajectorySet.auto3_pickupBall7ToFinalShoot);
            System.out.println("Finished trajectory generation");
        }
    }

    public TrajectorySet getTrajectorySet() {
        return mTrajectorySet;
    }

    public static Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
            boolean reversed,
            final List<Pose2d> waypoints,
            final List<TimingConstraint<Pose2dWithCurvature>> constraints,
            double max_vel,  // inches/s
            double max_accel,  // inches/s^2
            double max_voltage) {
        return mMotionPlanner.generateTrajectory(reversed, waypoints, constraints, max_vel, max_accel, max_voltage);
    }

    public static Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(
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

    public static final Pose2d kBall1 = new Pose2d(194.6035433, -80.35708661, Rotation2d.fromDegrees(0));  //200,-251
    public static final Pose2d kBall2 = new Pose2d(199.053937, -250.30315, Rotation2d.fromDegrees(0));  //200,-251
    public static final Pose2d kBall3 = new Pose2d(298.0893701, -312.7901575, Rotation2d.fromDegrees(0));    //298,-313
    public static final Pose2d kBall7 = new Pose2d(42.03031496, -280.783465, Rotation2d.fromDegrees(0));  //42,280

    public static class Auto1_5ball_Points {
        public static final Pose2d kStartPose1 = new Pose2d(297.9799213, -253.3216535, Rotation2d.fromDegrees(-88.49998937578972));  //302,-245
        public static final Pose2d kBall3Pickup = new Pose2d(301, -275, Rotation2d.fromDegrees(-92));   //301,-270
        public static final Pose2d kBall3PickupStartPath2 = new Pose2d(301, -275, Rotation2d.fromDegrees(-180));   //301,-270
        public static final Pose2d kBall2Pickup = new Pose2d(220, -267, Rotation2d.fromDegrees(140));  //220,-267
        public static final Pose2d kBall7Pickup = new Pose2d(68, -265, Rotation2d.fromDegrees(-145));  //60,-260

        public static Trajectory<TimedState<Pose2dWithCurvature>> getStart1ToPickupBall3() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kStartPose1);
            waypoints.add(kBall3Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        public static Trajectory<TimedState<Pose2dWithCurvature>> getPickupBall3ToPickupBall2and7() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kBall3PickupStartPath2);
            waypoints.add(kBall2Pickup);
            waypoints.add(kBall7Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

    }

    public static class Auto2_2ball_Points {
        public static final Pose2d kStartPose2 = new Pose2d(238.7240157, -120.2307087, Rotation2d.fromDegrees(136.500));   //245,-125
        public static final Pose2d kBall1Pickup = new Pose2d(215, -95, Rotation2d.fromDegrees(135));  //220,-267

        public static Trajectory<TimedState<Pose2dWithCurvature>> getStart2ToPickupBall1() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kStartPose2);
            waypoints.add(kBall1Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }
    }

    public static class Auto3_5ball_Alternate_Points {
        public static final Pose2d kStartPose1 = new Pose2d(297.9799213, -253.3216535, Rotation2d.fromDegrees(-88.49998937578972));  //302,-245
        public static final Pose2d kBall3Pickup = new Pose2d(301, -275, Rotation2d.fromDegrees(-92));   //301,-270
        public static final Pose2d kBall3PickupStartPath2 = new Pose2d(301, -275, Rotation2d.fromDegrees(-180));   //301,-270
        public static final Pose2d kBall2Pickup = new Pose2d(220, -267, Rotation2d.fromDegrees(140));  //220,-267
        public static final Pose2d kBall7Pickup = new Pose2d(68, -265, Rotation2d.fromDegrees(-145));  //60,-260
        public static final Pose2d kFinalShootBallPose = new Pose2d(135, -215, Rotation2d.fromDegrees(-145));  //60,-260

        public static Trajectory<TimedState<Pose2dWithCurvature>> getStart1ToPickupBall3() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kStartPose1);
            waypoints.add(kBall3Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        public static Trajectory<TimedState<Pose2dWithCurvature>> getPickupBall3ToPickupBall2() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kBall3PickupStartPath2);
            waypoints.add(kBall2Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        public static Trajectory<TimedState<Pose2dWithCurvature>> getPickupBall2ToPickupBall7() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kBall2Pickup);
            waypoints.add(kBall7Pickup);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }

        public static Trajectory<TimedState<Pose2dWithCurvature>> getPickupBall7ToFinalShoot() {
            List<Pose2d> waypoints = new ArrayList<>();
            waypoints.add(kBall7Pickup);
            waypoints.add(kFinalShootBallPose);
            return generateTrajectory(false, waypoints, Arrays.asList(new CentripetalAccelerationConstraint(kMaxCentripetalAccel)),
                    kMaxVel, kMaxAccel, kMaxVoltage);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public class TrajectorySet {
        //Test
        public final Trajectory<TimedState<Pose2dWithCurvature>> testTrajectory;
        public final Trajectory<TimedState<Pose2dWithCurvature>> testTrajectoryBack;

        //Auto 1
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto1_start1ToPickupBall3;
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto1_pickupBall3ToPickupBall2and7;

        //Auto 2
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto2_start2ToPickupBall1;

        //Auto 3
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto3_start1ToPickupBall3;
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto3_pickupBall3ToPickupBall2;
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto3_pickupBall2ToPickupBall7;
        public final Trajectory<TimedState<Pose2dWithCurvature>> auto3_pickupBall7ToFinalShoot;



        private TrajectorySet() {
            //Test
            testTrajectory = getTestTrajectory();
            testTrajectoryBack = getTestTrajectoryBack();

            //Auto 1
            auto1_start1ToPickupBall3 = Auto1_5ball_Points.getStart1ToPickupBall3();
            auto1_pickupBall3ToPickupBall2and7 = Auto1_5ball_Points.getPickupBall3ToPickupBall2and7();

            //Auto 2
            auto2_start2ToPickupBall1 = Auto2_2ball_Points.getStart2ToPickupBall1();

            //Auto 3
            auto3_start1ToPickupBall3 = Auto3_5ball_Alternate_Points.getStart1ToPickupBall3();
            auto3_pickupBall3ToPickupBall2 = Auto3_5ball_Alternate_Points.getPickupBall3ToPickupBall2();
            auto3_pickupBall2ToPickupBall7 = Auto3_5ball_Alternate_Points.getPickupBall2ToPickupBall7();
            auto3_pickupBall7ToFinalShoot = Auto3_5ball_Alternate_Points.getPickupBall7ToFinalShoot();
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
    }
}
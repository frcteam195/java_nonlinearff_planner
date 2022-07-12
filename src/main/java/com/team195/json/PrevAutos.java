package com.team195.json;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class PrevAutos {
    public static void saveJSONTrajectories()
    {
        //Auto 1
        Auto1_5ball_Points.getStart1ToPickupBall3();
        Auto1_5ball_Points.getPickupBall3ToPickupBall2and7();
        Auto1_5ball_Points.getPickupBall7ToFinalShootPose();

        //Auto 2
        Auto2_2ball_Points.getStart2ToPickupBall1();

        //Auto 3
        Auto3_1ball_Points.getStart3ToPickupBall2();
        Auto3_1ball_Points.getPickupBall2ToFieldLine();

        //Auto 4
        Auto4_1ball_Points.getStart3ToPickupBall2();
        Auto4_1ball_Points.getPickupBall2ToFieldLine();

        //Auto 5
        Auto5_5ball_Alternate_Points.getStart1ToPickupBall3();
        Auto5_5ball_Alternate_Points.getPickupBall3ToPickupBall2();
        Auto5_5ball_Alternate_Points.getPickupBall2ToPickupBall7();
        Auto5_5ball_Alternate_Points.getPickupBall7ToFinalShoot();

        //Auto 6
        Auto6_5ball_Points.getStartToPickupBall3();
        Auto6_5ball_Points.getPickupBall3ToPickupAndShootBall2();
        Auto6_5ball_Points.getPickupAndShootBall2ToPickupBall7();
        Auto6_5ball_Points.getPickupBall7ToFinalShootPose();
    }

    public static final Pose2dJson kBall1 = new Pose2dJson(194.6035433, -80.35708661, 0, "");  //200,-251
    public static final Pose2dJson kBall2 = new Pose2dJson(199.053937, -250.30315, 0, "");  //200,-251
    public static final Pose2dJson kBall3 = new Pose2dJson(298.0893701, -312.7901575, 0, "");    //298,-313
    public static final Pose2dJson kBall7 = new Pose2dJson(42.03031496, -280.783465, 0, "");  //42,280

    public static class Auto6_5ball_Points {
        public static final Pose2dJson kStartPose1 = new Pose2dJson(297.9799213, -253.3216535, -88.49998937578972, "");  //302,-245
        public static final Pose2dJson kBall3Pickup = new Pose2dJson(301, -286, -92, "");   //301,-270    }
        public static final Pose2dJson kBall3PickupStartPath2 = new Pose2dJson(301, -286, -180, "");
        public static final Pose2dJson kStartPath2ToBall2 = new Pose2dJson(201, -250, -205, "");
        public static final Pose2dJson kBall2PickupAndShoot = new Pose2dJson(150, -230, -200, "");
        public static final Pose2dJson kBall2PickupAndShootStartPath3 = new Pose2dJson(150, -230, -180, "");
        // public static final Pose2dJson kBall2PickupAndShootStartPath3 = new Pose2dJson(150, -230, -180, "");
        public static final Pose2dJson kBall7Pickup = new Pose2dJson(63, -265, -140, "");
        public static final Pose2dJson kFinalShootPose = new Pose2dJson(77, -255, -140, "");


        public static void getStartToPickupBall3() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kStartPose1);
            waypoints.add(kBall3Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(12, "Auto6_Start1ToPickupBall3", false, waypoints));
        }

        public static void getPickupBall3ToPickupAndShootBall2() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall3PickupStartPath2);
            waypoints.add(kStartPath2ToBall2);
            waypoints.add(kBall2PickupAndShoot);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(13, "Auto6_PickupBall3ToPickupAndShootBall2", false, waypoints));
        }

        public static void getPickupAndShootBall2ToPickupBall7() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall2PickupAndShootStartPath3);
            waypoints.add(kBall7Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(14, "Auto6_PickupAndShootBall2ToPickupBall7", false, waypoints));
        }

        public static void getPickupBall7ToFinalShootPose() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall7Pickup);
            waypoints.add(kFinalShootPose);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(15, "Auto6_PickupBall7ToFinalShootPose", true, waypoints));
        }
    }

    public static class Auto1_5ball_Points {
        public static final Pose2dJson kStartPose1 = new Pose2dJson(297.9799213, -253.3216535, -88.49998937578972, "");  //302,-245
        public static final Pose2dJson kBall3Pickup = new Pose2dJson(301, -286, -92, "");   //301,-270
        public static final Pose2dJson kBall3PickupStartPath2 = new Pose2dJson(301, -286, -180, "");   //301,-270
        public static final Pose2dJson kBall2Pickup = new Pose2dJson(220, -267, 140, "");  //220,-267
        public static final Pose2dJson kBall7Pickup = new Pose2dJson(63, -265, -145, "");  //60,-260
        public static final Pose2dJson kFinalShootPose = new Pose2dJson(77, -255, -145, "");

        public static void getStart1ToPickupBall3() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kStartPose1);
            waypoints.add(kBall3Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(0, "Auto1_Start1ToPickupBall3", false, waypoints));
        }

        public static void getPickupBall3ToPickupBall2and7() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall3PickupStartPath2);
            waypoints.add(kBall2Pickup);
            waypoints.add(kBall7Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(1, "Auto1_PickupBall3ToPickupBall2and7", false, waypoints));
        }

        public static void getPickupBall7ToFinalShootPose() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall7Pickup);
            waypoints.add(kFinalShootPose);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(11, "Auto1_PickupBall7ToFinalShootPose", true, waypoints));
        }

    }

    public static class Auto2_2ball_Points {
        public static final Pose2dJson kStartPose2 = new Pose2dJson(238.7240157, -120.2307087, 136.500, "");   //245,-125
        public static final Pose2dJson kBall1Pickup = new Pose2dJson(205, -85, 135, "");  //220,-267

        public static void getStart2ToPickupBall1() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kStartPose2);
            waypoints.add(kBall1Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(2, "Auto2_Start2ToPickupBall1", false, waypoints));
        }
    }

    public static class Auto3_1ball_Points {
        public static final Pose2dJson kStartPose3 = new Pose2dJson(268, -220, -156.000, "");   //rviz 258.523622, -217.8472441
        public static final Pose2dJson kBall2Pickup = new Pose2dJson(161, -186, 162, "");
        public static final Pose2dJson kFieldLineWait = new Pose2dJson(252, -100, 30, "");

        public static void getStart3ToPickupBall2() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kStartPose3);
            waypoints.add(kBall2Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(3, "Auto3_Start3ToPickupBall2", false, waypoints));
        }

        public static void getPickupBall2ToFieldLine() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall2Pickup);
            waypoints.add(kFieldLineWait);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(4, "Auto3_PickupBall2ToFieldLine", false, waypoints));
        }
    }

    public static class Auto4_1ball_Points {
        public static final Pose2dJson kStartPose3 = new Pose2dJson(268, -220, -156.000, "");   //rviz 258.523622, -217.8472441
        public static final Pose2dJson kBall2Pickup = new Pose2dJson(161, -186, 162, "");
        public static final Pose2dJson kHangarWaypoint = new Pose2dJson(68, -113, 90, "");
        public static final Pose2dJson kFieldLineWait = new Pose2dJson(230, -37, 12, "");

        public static void getStart3ToPickupBall2() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kStartPose3);
            waypoints.add(kBall2Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(5, "Auto4_Start3ToPickupBall2", false, waypoints));
        }

        public static void getPickupBall2ToFieldLine() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall2Pickup);
            waypoints.add(kHangarWaypoint);
            waypoints.add(kFieldLineWait);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(6, "Auto4_PickupBall2ToFieldLine", false, waypoints));
        }
    }

    public static class Auto5_5ball_Alternate_Points {
        public static final Pose2dJson kStartPose1 = new Pose2dJson(297.9799213, -253.3216535, -88.49998937578972, "");  //302,-245
        public static final Pose2dJson kBall3Pickup = new Pose2dJson(301, -275, -92, "");   //301,-270
        public static final Pose2dJson kBall3PickupStartPath2 = new Pose2dJson(301, -275, -180, "");   //301,-270
        public static final Pose2dJson kBall2Pickup = new Pose2dJson(220, -267, 140, "");  //220,-267
        public static final Pose2dJson kBall7Pickup = new Pose2dJson(68, -265, -145, "");  //60,-260
        public static final Pose2dJson kFinalShootBallPose = new Pose2dJson(135, -215, -145, "");  //60,-260

        public static void getStart1ToPickupBall3() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kStartPose1);
            waypoints.add(kBall3Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(7, "Auto5_Start1ToPickupBall3", false, waypoints));
        }

        public static void getPickupBall3ToPickupBall2() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall3PickupStartPath2);
            waypoints.add(kBall2Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(8, "Auto5_PickupBall3ToPickupBall2", false, waypoints));
        }

        public static void getPickupBall2ToPickupBall7() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall2Pickup);
            waypoints.add(kBall7Pickup);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(9, "Auto5_PickupBall2ToPickupBall7", false, waypoints));
        }

        public static void getPickupBall7ToFinalShoot() {
            List<Pose2dJson> waypoints = new ArrayList<>();
            waypoints.add(kBall7Pickup);
            waypoints.add(kFinalShootBallPose);
            TrajectoryLoader.SaveTrajectory(new TrajectoryJson(10, "Auto5_PickupBall7ToFinalShoot", false, waypoints));
        }
    }
}

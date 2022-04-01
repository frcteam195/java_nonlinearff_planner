package com.team254.frc2020;

import com.team254.lib.util.Units;

/**
 * A list of constants used by the rest of the robot code. This includes physics
 * constants as well as constants determined through calibration.
 * <p>
 * Port assignments should match up with the spreadsheet here:
 * https://docs.google.com/spreadsheets/d/1U1r9AyXk8nuGuACa36iQRRekL6HqLXF7dZ437_qiD98/edit?usp=sharing
 */
public class Constants {
    // Drive ratio.
    public static final double kDriveHighGearReduction = 50.0 / 11.0 * 44.0 / 30.0; //6.6666666667
    public static final double kGearRatioScalar = (1.0 / (50.0 / 10.0 * 44.0 / 30.0)) / (1.0 / kDriveHighGearReduction);
    // Wheel parameters.
    public static final double kDriveWheelTrackWidthInches = 25.625; //tuned 3/2
    public static final double kDriveWheelDiameterInches = 4.0025; //tuned 3/2
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kDriveWheelRadiusMeters = Units.inches_to_meters(kDriveWheelDiameterInches);
    public static final double kDriveWheelTrackRadiusWidthMeters = kDriveWheelTrackWidthInches / 2.0 * 0.0254;
    public static final double kTrackScrubFactor = 1.0;

    // robot dynamics TODO tune
    public static final double kDriveVIntercept = 0.378582; // V
    public static final double kDriveLinearKv = 0.113699 / 2.0 * Constants.kDriveWheelDiameterInches; // V / rad/s
    public static final double kFalcon500StallTorque = 4.69; // N*m
    public static final double kAssumedTorqueEfficiency = 0.95;
    public static final double kRobotLinearInertia = 68.946; //62.051; // kg TODO
    public static final double kNumMotors = 4;
    public static final double kDriveAnalyticalLinearKa = 12.0 /* V */ / ((kDriveHighGearReduction * kFalcon500StallTorque * kAssumedTorqueEfficiency * kNumMotors) / (kRobotLinearInertia * kDriveWheelRadiusMeters * kDriveWheelRadiusMeters));
    public static final double kDriveLinearKa = 0.010351 / 2.0 * Constants.kDriveWheelDiameterInches * kGearRatioScalar; // V / rad/s^2
    public static final double kDriveAngularKa = 0.008630 / 2.0 * Constants.kDriveWheelDiameterInches * kGearRatioScalar; // V per rad/s^2
    public static final double kRobotAngularInertia = kDriveAngularKa / kDriveLinearKa *
            kDriveWheelTrackRadiusWidthMeters * kDriveWheelTrackRadiusWidthMeters * kRobotLinearInertia;  // kg m^2
    public static final double kRobotAngularDrag = 15.0; // N*m / (rad/sec)

    // path following TODO tune?
    public static final double kPathKX = 4.0; // units/s per unit of error
    public static final double kPathLookaheadTime = 0.4; // seconds to look ahead along the path for steering
    public static final double kPathMinLookaheadDistance = 24.0; // inches

}
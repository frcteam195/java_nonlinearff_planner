package com.team195.frc.constants;

/**
 * A list of constants used by the rest of the robot code. This include physics constants as well as constants
 * determined through calibrations.
 */
public class Constants {
    public static final double kLooperDt = 0.010;

    public static final int kCANTimeoutMs = 10; //use for on the fly updates
    public static final int kLongCANTimeoutMs = 100; //use for constructors
    public static final int kTalonRetryCount = 3; //use for constructors

    // Control Board
    public static final boolean kUseGamepadForDriving = false;
    public static final boolean kUseGamepadForButtons = true;

    public static final int kDriveGamepadPort = 0;
    public static final int kButtonGamepadPort = 2;
    public static final int kMainThrottleJoystickPort = 0;
    public static final int kMainTurnJoystickPort = 1;
    public static final double kJoystickThreshold = 0.5;
    public static final double kJoystickJogThreshold = 0.4;
    public static final double kJoystickTriggerThreshold = 0.3;
    public static final double kJoystickDeadband = 0.04;


    //Thread prioritization - 5 is default
    public static final int kRobotThreadPriority = Thread.NORM_PRIORITY;
    public static final int kControllerThreadPriority = 9;
    public static final int kLooperThreadPriority = Thread.MAX_PRIORITY;
    public static final int kConnectionMonitorThreadPriority = 7;
    public static final int kLEDThreadPriority = Thread.MIN_PRIORITY;
    public static final int kConsoleReporterThreadPriority = Thread.NORM_PRIORITY;

    public static final int LOG_OSC_REPORTER_PORT = 5805;
    public static final int AUTO_SELECTOR_PORT = 5806;
    public static final int DASHJOY_RECEIVER_PORT = 5809;
    public static final int CKCO_TRANSPORT_PORT = 5808;
    public static final String COPROCESSOR_IP = "10.1.95.19";
    public static final String RIO_IP = "10.1.95.2";


    public static final boolean TUNING_PIDS = true;
    public static final boolean DEBUG = false;
    public static final boolean LOGGING_ENABLED = true;
    public static final boolean REPORTING_ENABLED = false;
    public static final boolean REPORT_TO_DRIVERSTATION_INSTEAD_OF_CONSOLE = false;

}
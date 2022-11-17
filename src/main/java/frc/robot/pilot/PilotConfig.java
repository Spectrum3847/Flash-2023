package frc.robot.pilot;

import frc.robot.swerve.SwerveConfig;

/** Constants used by the Pilot Gamepad */
public class PilotConfig {
    public static final int port = 0;

    public static final double throttleExp = 1.2;
    public static final double throttleScaler = SwerveConfig.maxVelocity * 0.8;
    public static final boolean yInvert = true;
    public static final boolean xInvert = true;
    public static final double throttleDeadband = 0.1;
    public static final double steeringExp = throttleExp;
    public static final double steeringScaler = SwerveConfig.maxAngularVelocity;
    public static final boolean steeringInvert = true;
    public static final double steeringDeadband = 0.1;
}

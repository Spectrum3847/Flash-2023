package frc.robot.pilot;

import frc.robot.swerve.SwerveConfig;

/** Constants used by the Pilot Gamepad */
public class PilotConstants {
    public static final int port = 0;

    public static final double throttleExp = 1.2;
    public static final double throttleScaler = SwerveConfig.maxSpeed;
    public static final boolean yInvert = true;
    public static final boolean xInvert = false;
    public static final double throttleDeadband = 0.15;
    public static final double steeringExp = throttleExp;
    public static final double steeringScaler = SwerveConfig.maxAngularVelocity;
    public static final boolean steeringInvert = true;
    public static final double steeringDeadband = throttleDeadband;
}

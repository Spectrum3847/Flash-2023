package frc.robot.pilot;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Robot;

/** Constants used by the Pilot Gamepad */
public class PilotConfig {
    public static final int port = 0;

    public final double throttleDeadband = 0.15;
    public final double throttleExp = 1.5;
    public final double throttleScaler;
    public final boolean xInvert = true;
    public final boolean yInvert = true;

    public final double steeringDeadband = 0.15;
    public final double steeringExp = throttleExp;
    public final double steeringScaler;
    public final boolean steeringInvert = true;

    public final Translation2d intakeCoRmeters = new Translation2d(0, 0);

    public PilotConfig() {
        throttleScaler = Robot.swerve.config.tuning.maxVelocity * 0.5;
        steeringScaler = Robot.swerve.config.tuning.maxAngularVelocity * 0.4;
    }
}

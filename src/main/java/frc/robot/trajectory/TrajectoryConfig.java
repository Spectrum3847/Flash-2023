package frc.robot.trajectory;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.swerve.SwerveConfig;

public class TrajectoryConfig {
    /* Swerve Conroller Constants */
    public static final double kMaxSpeed = 2.7;
    public static final double kMaxAccel = 2.4; // 2 worked but took too long
    public static final double kMaxAngularSpeedRadiansPerSecond = SwerveConfig.maxAngularVelocity;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared =
            SwerveConfig.maxAngularVelocity;

    public static final double kPXController = 0.6;
    public static final double kDXController = 0;
    public static final double kPYController = kPXController;
    public static final double kDYController = kDXController;
    public static final double kPThetaController = 5;
    public static final double kDThetaController = 0.01;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                    kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
}

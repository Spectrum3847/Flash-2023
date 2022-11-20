package frc.robot.vision;

import edu.wpi.first.math.util.Units;

@SuppressWarnings("unused")
public final class VisionConfig {
    public final String CAMERA_NAME = "gloworm";
    public final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(16.178);
    public final double CAMERA_PITCH_RADIANS =
            Units.degreesToRadians(26.138); // angle from looking straight forward

    // testing

    private final double TARGET_HEIGHT_METERS = Units.inchesToMeters(36.2); // 36.2 inches

    /* Location of camera relative to robot center */
    // private static final Transform2d CAMERA_TO_ROBOT =
    // new Transform2d(new Translation2d(Units.inchesToMeters(x), 0.0), new Rotation2d(0.0));

    public VisionConfig() {}
}

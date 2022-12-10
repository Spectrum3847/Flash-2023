package frc.robot.swerve.configs;

import edu.wpi.first.math.util.Units;
import frc.robot.RobotConfig.Motors;
import frc.robot.swerve.configs.GyroConfig.GyroType;
import frc.robot.swerve.configs.PhysicalConfig.AngleSensorType;

public class Flash2023 {

    /* Physical Configs */
    static final double trackWidth = Units.inchesToMeters(17.5);
    static final double wheelBase = Units.inchesToMeters(20.5);
    static final double wheelDiameter = Units.inchesToMeters(3.8195);
    static final double driveGearRatio = (8.16 / 1.0);
    static final double angleGearRatio = (12.8 / 1.0);
    static final boolean driveMotorInvert = false;
    static final boolean angleMotorInvert = false;
    static final boolean angleSensorInvert = false;

    // Tuning Config
    /* Angle Motor PID Values */
    static final double angleKP = 0.6;
    static final double angleKD = 12;

    /* Drive Motor PID Values */
    static final double driveKP = 0.1;
    static final double driveKD = 0.0;

    /* Drive Motor Characterization Values */
    static final double driveKS = (0.605 / 12); // /12 to convert from volts to %output
    static final double driveKV = (1.72 / 12);
    static final double driveKA = (0.193 / 12);

    /* Swerve Profiling Values */
    static final double maxVelocity =
            ((6380 / 60) / angleGearRatio) * wheelDiameter * Math.PI * 0.95; // meters per // second
    static final double maxAccel = maxVelocity * 1.5; // take 1/2 sec to get to max speed.
    static final double maxAngularVelocity =
            maxVelocity / Math.hypot(trackWidth / 2.0, wheelBase / 2.0);
    static final double maxAngularAcceleration = Math.pow(maxAngularVelocity, 2);

    /* Gyro Config */
    static final GyroType type = GyroType.PIGEON2;
    static final int port = 1;

    public static final PhysicalConfig physical =
            new PhysicalConfig(
                    trackWidth,
                    wheelBase,
                    wheelDiameter,
                    driveGearRatio,
                    angleGearRatio,
                    driveMotorInvert,
                    angleMotorInvert,
                    angleSensorInvert,
                    AngleSensorType.CANCoder);

    public static final TuningConfig tuning =
            new TuningConfig(
                    angleKP,
                    angleKD,
                    driveKP,
                    driveKD,
                    driveKS,
                    driveKV,
                    driveKA,
                    maxVelocity,
                    maxAccel,
                    maxAngularVelocity,
                    maxAngularAcceleration);

    public static final GyroConfig gyro = new GyroConfig(type, port);

    /* Module Configs */
    static final ModuleConfig Mod0 =
            new ModuleConfig(Motors.driveMotor0, Motors.angleMotor0, 3, 182.54, physical, tuning);

    static final ModuleConfig Mod1 =
            new ModuleConfig(Motors.driveMotor1, Motors.angleMotor1, 13, 88.69, physical, tuning);

    static final ModuleConfig Mod2 =
            new ModuleConfig(Motors.driveMotor2, Motors.angleMotor2, 23, 352.4, physical, tuning);

    static final ModuleConfig Mod3 =
            new ModuleConfig(Motors.driveMotor3, Motors.angleMotor3, 33, 350.59, physical, tuning);

    public static final ModuleConfig[] modules = new ModuleConfig[] {Mod0, Mod1, Mod2, Mod3};

    public static final SwerveConfig config = new SwerveConfig(physical, tuning, gyro, modules);
}

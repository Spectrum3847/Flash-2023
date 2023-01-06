package frc.robot.swerve.configs;

import frc.robot.RobotConfig.Motors;

public class Infrared8515 {
    /* Angle Offsets */
    static final double Mod0AngleOffset = 12.5683 + 180;
    static final double Mod1AngleOffset = 170.15625;
    static final double Mod2AngleOffset = 74.8828 + 180;
    static final double Mod3AngleOffset = 306.73828;

    public static final GyroConfig gyro = Infrared3847.gyro;
    public static final PhysicalConfig physical = Infrared3847.physical;
    public static final TuningConfig tuning = Infrared3847.tuning;

    /* Module Configs */
    static final ModuleConfig Mod0 =
            new ModuleConfig(
                    Motors.driveMotor0, Motors.angleMotor0, 3, Mod0AngleOffset, physical, tuning);

    static final ModuleConfig Mod1 =
            new ModuleConfig(
                    Motors.driveMotor1, Motors.angleMotor1, 13, Mod1AngleOffset, physical, tuning);

    static final ModuleConfig Mod2 =
            new ModuleConfig(
                    Motors.driveMotor2, Motors.angleMotor2, 23, Mod2AngleOffset, physical, tuning);

    static final ModuleConfig Mod3 =
            new ModuleConfig(
                    Motors.driveMotor3, Motors.angleMotor3, 33, Mod3AngleOffset, physical, tuning);

    public static final ModuleConfig[] modules = new ModuleConfig[] {Mod0, Mod1, Mod2, Mod3};

    public static final SwerveConfig config = new SwerveConfig(physical, tuning, gyro, modules);
}

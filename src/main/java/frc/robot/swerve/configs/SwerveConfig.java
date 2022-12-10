// Created by Spectrum3847
package frc.robot.swerve.configs;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

public class SwerveConfig {
    public final PhysicalConfig physical;
    public final TuningConfig tuning;
    public final GyroConfig gyro;
    public final ModuleConfig[] modules;
    public final SwerveDriveKinematics swerveKinematics;

    // Remove later
    public final double MK4_L2_driveGearRatio = (6.75 / 1.0);
    public final double MK4i_L2_angleGearRatio = (50.0 / 14.0) * (60.0 / 10.0);

    public SwerveConfig(
            PhysicalConfig physical, TuningConfig tuning, GyroConfig gyro, ModuleConfig[] modules) {
        this.physical = physical;
        this.tuning = tuning;
        this.gyro = gyro;
        this.modules = modules;

        swerveKinematics =
                new SwerveDriveKinematics(
                        physical.frontLeftLocation,
                        physical.frontRightLocation,
                        physical.backLeftLocation,
                        physical.backRightLocation);
    }
}

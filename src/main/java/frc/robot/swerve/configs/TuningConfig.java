package frc.robot.swerve.configs;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class TuningConfig {
    public final double angleKP;
    public final double angleKD;
    public final double driveKP;
    public final double driveKD;
    public final double driveKS;
    public final double driveKV;
    public final double driveKA;
    public final double maxVelocity;
    public final double maxAcceleration;
    public final double maxAngularVelocity;
    public final double MaxAngularAcceleration;

    /* Swerve Current Limiting */
    public final int angleContinuousCurrentLimit = 20;
    public final int anglePeakCurrentLimit = 30;
    public final double anglePeakCurrentDuration = 0.1;
    public final boolean angleEnableCurrentLimit = true;

    public final int driveContinuousCurrentLimit = 40;
    public final int drivePeakCurrentLimit = 40;
    public final double drivePeakCurrentDuration = 0.0;
    public final boolean driveEnableCurrentLimit = true;

    /* Neutral Modes */
    public final NeutralMode angleNeutralMode = NeutralMode.Coast;
    public final NeutralMode driveNeutralMode = NeutralMode.Coast;

    public TuningConfig(
            double angleKP,
            double angleKD,
            double driveKP,
            double driveKD,
            double driveKS,
            double driveKV,
            double driveKA,
            double maxVelocity,
            double maxAcceleration,
            double maxAngularVelocity,
            double maxAngularAcceleration) {
        this.angleKP = angleKP;
        this.angleKD = angleKD;
        this.driveKP = driveKP;
        this.driveKD = driveKD;
        this.driveKS = driveKS;
        this.driveKV = driveKV;
        this.driveKA = driveKA;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxAngularVelocity = maxAngularVelocity;
        this.MaxAngularAcceleration = maxAngularAcceleration;
    }
}

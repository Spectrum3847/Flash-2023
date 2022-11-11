// Created by Spectrum3847

// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.SpectrumLib.util.Conversions;

public class Swerve extends SubsystemBase {
    public SwerveConfig config;
    public Gyro gyro;
    public Odometry odometry;
    public SwerveModule[] mSwerveMods;
    public double pidTurn = 0;
    public double drive_x = 0;
    public double drive_y = 0;
    public double drive_rotation = 0;
    ChassisSpeeds lastRequestedVelocity = new ChassisSpeeds(0, 0, 0);

    public Swerve() {
        setName("Swerve");
        config = new SwerveConfig();
        odometry = new Odometry(this);
        gyro = new Gyro();

        mSwerveMods =
                new SwerveModule[] {
                    new SwerveModule(0, SwerveConfig.Mod0.config),
                    new SwerveModule(1, SwerveConfig.Mod1.config),
                    new SwerveModule(2, SwerveConfig.Mod2.config),
                    new SwerveModule(3, SwerveConfig.Mod3.config)
                };

        resetSteeringToAbsolute();
    }

    @Override
    public void periodic() {
        odometry.update();
    }

    public void drive(
            Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        // If pidTurn is getting a value override the drivers steering control
        if (pidTurn != 0) {
            rotation = pidTurn;
        }

        if (Math.abs(rotation) < 0.03) {
            rotation = 0;
        }
        ChassisSpeeds speeds;
        if (fieldRelative) {
            speeds =
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                            translation.getX(), translation.getY(), rotation, gyro.getYaw());
        } else {
            speeds = new ChassisSpeeds(translation.getX(), translation.getY(), rotation);
        }

        // speeds = limitAcceleration(speeds);
        SwerveModuleState[] swerveModuleStates =
                SwerveConfig.swerveKinematics.toSwerveModuleStates(speeds);

        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveConfig.maxSpeed);

        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
        drive_y = translation.getY();
        drive_x = translation.getX();
        drive_rotation = rotation;
    }

    public void useOutput(double output) {
        pidTurn = output * SwerveConfig.maxAngularVelocity;
    }

    // Used for control loops that give a rotational velocity directly
    public void setRotationalVelocity(double rotationalVelocity) {
        pidTurn = rotationalVelocity;
    }

    // Reset AngleMotors to Absolute
    public void resetSteeringToAbsolute() {
        for (SwerveModule mod : mSwerveMods) {
            mod.resetToAbsolute();
        }
    }

    /* Used by SwerveFollowCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConfig.maxSpeed);

        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    public void brakeMode(boolean enabled) {
        for (SwerveModule mod : mSwerveMods) {
            if (enabled) {
                mod.mDriveMotor.setNeutralMode(NeutralMode.Brake);
            } else {
                mod.mDriveMotor.setNeutralMode(NeutralMode.Coast);
            }
        }
    }

    public SwerveModuleState[] getStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : mSwerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] states = new SwerveModulePosition[4];
        for (SwerveModule mod : mSwerveMods) {
            states[mod.moduleNumber] = mod.getPosition();
        }
        return states;
    }

    // Characterization methods
    public void resetFalconPositions() {
        for (SwerveModule mod : mSwerveMods) {
            mod.mDriveMotor.setSelectedSensorPosition(0);
        }
    }

    public double getLeftPositionMeters() {
        return Conversions.FalconToMeters(
                mSwerveMods[0].mDriveMotor.getSelectedSensorPosition(),
                SwerveConfig.wheelCircumference,
                SwerveConfig.driveGearRatio);
    }

    public double getRightPositionMeters() {
        return Conversions.FalconToMeters(
                mSwerveMods[1].mDriveMotor.getSelectedSensorPosition(),
                SwerveConfig.wheelCircumference,
                SwerveConfig.driveGearRatio);
    }

    public double getLeftMetersPerSec() {
        return Conversions.falconToMPS(
                mSwerveMods[0].mDriveMotor.getSelectedSensorVelocity(),
                SwerveConfig.wheelCircumference,
                SwerveConfig.driveGearRatio);
    }

    public double getRightMetersPerSec() {
        return Conversions.falconToMPS(
                mSwerveMods[1].mDriveMotor.getSelectedSensorVelocity(),
                SwerveConfig.wheelCircumference,
                SwerveConfig.driveGearRatio);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        mSwerveMods[0].mDriveMotor.setVoltage(leftVolts);
        mSwerveMods[2].mDriveMotor.setVoltage(leftVolts);
        mSwerveMods[1].mDriveMotor.setVoltage(rightVolts);
        mSwerveMods[3].mDriveMotor.setVoltage(rightVolts);
    }

    public void stop() {
        for (SwerveModule mod : mSwerveMods) {
            mod.mDriveMotor.stopMotor();
            mod.mAngleMotor.stopMotor();
        }
    }
}

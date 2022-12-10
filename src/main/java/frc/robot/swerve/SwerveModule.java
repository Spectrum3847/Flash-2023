// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.SpectrumLib.util.Conversions;
import frc.robot.swerve.angleSensors.AngleSensorIO;
import frc.robot.swerve.angleSensors.CANencoder;
import frc.robot.swerve.angleSensors.ThriftyEncoder;
import frc.robot.swerve.configs.ModuleConfig;
import frc.robot.swerve.configs.SwerveConfig;

public class SwerveModule extends SubsystemBase {
    private ModuleConfig moduleConfig;
    public int moduleNumber;
    private double angleOffset;
    private WPI_TalonFX mAngleMotor;
    private WPI_TalonFX mDriveMotor;
    private AngleSensorIO angleEncoder;
    private double lastAngle;
    private SwerveConfig config;
    private SwerveModuleState mSwerveModState = new SwerveModuleState();
    private SwerveModulePosition mSwerveModPosition = new SwerveModulePosition();
    private Rotation2d mAbsoluteAngle = new Rotation2d();

    SimpleMotorFeedforward feedforward;

    public SwerveModule(int moduleNumber, SwerveConfig config) {
        this.moduleNumber = moduleNumber;
        this.config = config;
        this.moduleConfig = config.modules[moduleNumber];
        feedforward =
                new SimpleMotorFeedforward(
                        config.tuning.driveKS, config.tuning.driveKV, config.tuning.driveKA);
        angleOffset = moduleConfig.angleOffset;
        /* Angle Encoder Config */
        switch (config.physical.angleSensorType) {
            case CANCoder:
                angleEncoder = new CANencoder(moduleConfig.absAngleSensorID);
                break;
            case ThriftyEncoder:
                angleEncoder = new ThriftyEncoder(moduleConfig.absAngleSensorID);
                break;
        }

        /* Angle Motor Config */
        mAngleMotor = new WPI_TalonFX(moduleConfig.angleMotorID);
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new WPI_TalonFX(moduleConfig.driveMotorID);
        configDriveMotor();

        lastAngle = getFalconAngle();
    }

    @Override
    public void periodic() {
        mSwerveModState = getCANState();
        mSwerveModPosition = getCANPosition();
        mAbsoluteAngle = checkAbsoluteAngle();
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        Rotation2d currentAngle = getState().angle;
        desiredState = SwerveModuleState.optimize(desiredState, currentAngle);
        // Calculate the correct angle to steer the wheel
        // Correct for CTRE controller not being continuous
        Rotation2d desiredAngle = desiredState.angle;

        double delta = desiredAngle.getDegrees() - currentAngle.getDegrees();
        if (delta > 180) {
            delta = (delta - 360);
        } else if (delta < -180) {
            delta = (delta + 360);
        }

        double outputAngle = getFalconAngle() + delta;

        if ((Math.abs(desiredState.speedMetersPerSecond) < (config.tuning.maxVelocity * 0.01))) {
            outputAngle = lastAngle;
        }

        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(outputAngle, config.physical.angleGearRatio));
        lastAngle = outputAngle;

        // Calculate the velocity of the module
        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / config.tuning.maxVelocity;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity =
                    Conversions.MPSToFalcon(
                            desiredState.speedMetersPerSecond,
                            config.physical.wheelCircumference,
                            config.physical.driveGearRatio);
            mDriveMotor.set(
                    ControlMode.Velocity,
                    velocity,
                    DemandType.ArbitraryFeedForward,
                    feedforward.calculate(desiredState.speedMetersPerSecond));
        }
    }

    public void resetToAbsolute() {
        double offset = angleOffset;
        double absolutePosition =
                Conversions.degreesToFalcon(
                        getAbsoluteAngle().getDegrees() - offset, config.physical.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(moduleConfig.swerveAngleFXConfig);
        mAngleMotor.setInverted(config.physical.angleMotorInvert);
        mAngleMotor.setNeutralMode(config.tuning.angleNeutralMode);
        resetToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(moduleConfig.swerveDriveFXConfig);
        mDriveMotor.setInverted(config.physical.driveMotorInvert);
        mDriveMotor.setNeutralMode(config.tuning.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    private Rotation2d checkAbsoluteAngle() {
        return angleEncoder.get();
    }

    public Rotation2d getAbsoluteAngle() {
        return mAbsoluteAngle;
    }

    public double getTargetAngle() {
        return lastAngle;
    }

    public double getFalconAngle() {
        return Conversions.falconToDegrees(
                mAngleMotor.getSelectedSensorPosition(), config.physical.angleGearRatio);
    }

    private SwerveModuleState getCANState() {
        double velocity =
                Conversions.falconToMPS(
                        mDriveMotor.getSelectedSensorVelocity(),
                        config.physical.wheelCircumference,
                        config.physical.driveGearRatio);
        Rotation2d angle =
                Rotation2d.fromDegrees(
                        Conversions.falconToDegrees(
                                mAngleMotor.getSelectedSensorPosition(),
                                config.physical.angleGearRatio));
        return new SwerveModuleState(velocity, angle);
    }

    public SwerveModuleState getState() {
        return mSwerveModState;
    }

    private SwerveModulePosition getCANPosition() {
        double position =
                Conversions.FalconToMeters(
                        mDriveMotor.getSelectedSensorPosition(),
                        config.physical.wheelCircumference,
                        config.physical.driveGearRatio);
        return new SwerveModulePosition(position, mSwerveModState.angle);
    }

    public SwerveModulePosition getPosition() {
        return mSwerveModPosition;
    }

    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mDriveMotor.setNeutralMode(NeutralMode.Brake);
            mAngleMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            mDriveMotor.setNeutralMode(NeutralMode.Coast);
            mAngleMotor.setNeutralMode(NeutralMode.Coast);
        }
    }

    public void stop() {
        mDriveMotor.stopMotor();
        mAngleMotor.stopMotor();
    }

    public void setVoltage(double voltage) {
        mDriveMotor.setVoltage(voltage);
    }
}

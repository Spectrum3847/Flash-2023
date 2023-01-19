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
import frc.robot.swerve.angleSensors.CanCoder;
import frc.robot.swerve.angleSensors.ThriftyEncoder;

public class SwerveModule extends SubsystemBase {
    public int moduleNumber;
    private double angleOffset;
    private WPI_TalonFX mAngleMotor;
    private WPI_TalonFX mDriveMotor;
    private AngleSensorIO angleEncoder;
    private double lastAngle;
    private SwerveConfig swerveConfig;
    private SwerveModuleState mSwerveModState = new SwerveModuleState();
    private SwerveModulePosition mSwerveModPosition = new SwerveModulePosition();
    private Rotation2d mAbsoluteAngle = new Rotation2d();

    SimpleMotorFeedforward feedforward;

    public SwerveModule(int moduleNumber, SwerveConfig swerveConfig, ModuleConfig config) {
        this.moduleNumber = moduleNumber;
        this.swerveConfig = swerveConfig;
        feedforward =
                new SimpleMotorFeedforward(
                        SwerveConfig.driveKS, SwerveConfig.driveKV, SwerveConfig.driveKA);
        angleOffset = config.angleOffset;
        /* Angle Encoder Config */
        if (config.isTTBsensor) {
            angleEncoder = new ThriftyEncoder(config.AbsAngleSensor);
        } else {
            angleEncoder = new CanCoder(config.AbsAngleSensor);
        }

        /* Angle Motor Config */
        mAngleMotor = new WPI_TalonFX(config.angleMotorID);
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new WPI_TalonFX(config.driveMotorID);
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

        if ((Math.abs(desiredState.speedMetersPerSecond) < (swerveConfig.maxVelocity * 0.01))) {
            outputAngle = lastAngle;
        }

        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(outputAngle, swerveConfig.angleGearRatio));
        lastAngle = outputAngle;

        // Calculate the velocity of the module
        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / swerveConfig.maxVelocity;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity =
                    Conversions.MPSToFalcon(
                            desiredState.speedMetersPerSecond,
                            swerveConfig.wheelCircumference,
                            swerveConfig.driveGearRatio);
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
                        getAbsoluteAngle().getDegrees() - offset, swerveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(swerveConfig.swerveAngleFXConfig);
        mAngleMotor.setInverted(swerveConfig.angleMotorInvert);
        mAngleMotor.setNeutralMode(swerveConfig.angleNeutralMode);
        resetToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(swerveConfig.swerveDriveFXConfig);
        mDriveMotor.setInverted(swerveConfig.driveMotorInvert);
        mDriveMotor.setNeutralMode(swerveConfig.driveNeutralMode);
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
                mAngleMotor.getSelectedSensorPosition(), swerveConfig.angleGearRatio);
    }

    private SwerveModuleState getCANState() {
        double velocity =
                Conversions.falconToMPS(
                        mDriveMotor.getSelectedSensorVelocity(),
                        swerveConfig.wheelCircumference,
                        swerveConfig.driveGearRatio);
        Rotation2d angle =
                Rotation2d.fromDegrees(
                        Conversions.falconToDegrees(
                                mAngleMotor.getSelectedSensorPosition(),
                                swerveConfig.angleGearRatio));
        return new SwerveModuleState(velocity, angle);
    }

    public SwerveModuleState getState() {
        return mSwerveModState;
    }

    private SwerveModulePosition getCANPosition() {
        double position =
                Conversions.FalconToMeters(
                        mDriveMotor.getSelectedSensorPosition(),
                        swerveConfig.wheelCircumference,
                        swerveConfig.driveGearRatio);
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

// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.SpectrumLib.swerve.SwerveModuleConfig;
import frc.SpectrumLib.util.Conversions;

public class SwerveModule extends SubsystemBase {
    public int moduleNumber;
    private double angleOffset;
    private WPI_TalonFX mAngleMotor;
    private WPI_TalonFX mDriveMotor;
    private WPI_CANCoder angleEncoder;
    private double lastAngle;
    private SwerveConfig swerveConfig;
    private SwerveModuleState mSwerveModState;
    private SwerveModulePosition mSwerveModPosition;
    private Rotation2d mCANcoderAngle;

    SimpleMotorFeedforward feedforward =
            new SimpleMotorFeedforward(
                    SwerveConfig.driveKS, SwerveConfig.driveKV, SwerveConfig.driveKA);

    public SwerveModule(
            int moduleNumber, SwerveConfig swerveConfig, SwerveModuleConfig moduleConfig) {
        this.moduleNumber = moduleNumber;
        this.swerveConfig = swerveConfig;
        angleOffset = moduleConfig.angleOffset;
        /* Angle Encoder Config */
        angleEncoder = new WPI_CANCoder(moduleConfig.cancoderID);
        configAngleEncoder();

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
        mCANcoderAngle = getCANcoderAngle();
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        // Custom optimize command, since default WPILib optimize assumes continuous controller
        // which CTRE is not
        Rotation2d currentAngle = getState().angle;
        desiredState = SwerveModuleState.optimize(desiredState, currentAngle);

        // Prevent rotating module if speed is less then 1%
        // Prevents Jittering.
        Rotation2d desiredAngle = desiredState.angle;

        double delta = desiredAngle.getDegrees() - currentAngle.getDegrees();
        if (delta > 180) {
            delta = (delta - 360);
        } else if (delta < -180) {
            delta = (delta + 360);
        }

        double output = getFalconAngle() + delta;

        if ((Math.abs(desiredState.speedMetersPerSecond) < (SwerveConfig.maxVelocity * 0.01))) {
            output = lastAngle;
        }

        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(output, SwerveConfig.angleGearRatio));
        lastAngle = output;

        // Velocity
        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConfig.maxVelocity;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity =
                    Conversions.MPSToFalcon(
                            desiredState.speedMetersPerSecond,
                            SwerveConfig.wheelCircumference,
                            SwerveConfig.driveGearRatio);
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
                        getAbsoluteAngle().getDegrees() - offset, SwerveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }

    private void configAngleEncoder() {
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(swerveConfig.swerveCanCoderConfig);
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 249);
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 20);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(swerveConfig.swerveAngleFXConfig);
        mAngleMotor.setInverted(SwerveConfig.angleMotorInvert);
        mAngleMotor.setNeutralMode(SwerveConfig.angleNeutralMode);
        resetToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(swerveConfig.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveConfig.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveConfig.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    private Rotation2d getCANcoderAngle() {
        Rotation2d position = Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
        return position;
    }

    public Rotation2d getAbsoluteAngle() {
        return mCANcoderAngle;
    }

    public double getTargetAngle() {
        return lastAngle;
    }

    public double getFalconAngle() {
        return Conversions.falconToDegrees(
                mAngleMotor.getSelectedSensorPosition(), SwerveConfig.angleGearRatio);
    }

    private SwerveModuleState getCANState() {
        double velocity =
                Conversions.falconToMPS(
                        mDriveMotor.getSelectedSensorVelocity(),
                        SwerveConfig.wheelCircumference,
                        SwerveConfig.driveGearRatio);
        Rotation2d angle =
                Rotation2d.fromDegrees(
                        Conversions.falconToDegrees(
                                mAngleMotor.getSelectedSensorPosition(),
                                SwerveConfig.angleGearRatio));
        return new SwerveModuleState(velocity, angle);
    }

    public SwerveModuleState getState() {
        return mSwerveModState;
    }

    private SwerveModulePosition getCANPosition() {
        double position =
                Conversions.FalconToMeters(
                        mDriveMotor.getSelectedSensorPosition(),
                        SwerveConfig.wheelCircumference,
                        SwerveConfig.driveGearRatio);
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

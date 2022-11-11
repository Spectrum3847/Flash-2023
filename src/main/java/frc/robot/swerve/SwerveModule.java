// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.SpectrumLib.swerve.CTREModuleState;
import frc.SpectrumLib.swerve.SwerveModuleConfig;
import frc.SpectrumLib.util.Conversions;
import frc.robot.Robot;

public class SwerveModule {
    public int moduleNumber;
    private double angleOffset;
    public WPI_TalonFX mAngleMotor;
    public WPI_TalonFX mDriveMotor;
    private WPI_CANCoder angleEncoder;
    private double lastAngle;

    SimpleMotorFeedforward feedforward =
            new SimpleMotorFeedforward(
                    SwerveConfig.driveKS, SwerveConfig.driveKV, SwerveConfig.driveKA);

    public SwerveModule(int moduleNumber, SwerveModuleConfig moduleConfig) {
        this.moduleNumber = moduleNumber;
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

        lastAngle = getState().angle.getDegrees();
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        desiredState =
                CTREModuleState.optimize(
                        desiredState,
                        getState().angle); // Custom optimize command, since default WPILib optimize
        // assumes continuous controller which CTRE is not

        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConfig.maxSpeed;
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

        double angle =
                (Math.abs(desiredState.speedMetersPerSecond) <= (SwerveConfig.maxSpeed * 0.01))
                        ? lastAngle
                        : desiredState.angle
                                .getDegrees(); // Prevent rotating module if speed is less then 1%.
        // Prevents Jittering.
        mAngleMotor.set(
                ControlMode.Position,
                Conversions.degreesToFalcon(angle, SwerveConfig.angleGearRatio));
        lastAngle = angle;
    }

    public void resetToAbsolute() {
        double offset = angleOffset;
        double absolutePosition =
                Conversions.degreesToFalcon(
                        getCanCoder().getDegrees() - offset, SwerveConfig.angleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }

    private void configAngleEncoder() {
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(Robot.swerve.config.swerveCanCoderConfig);
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 200);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(Robot.swerve.config.swerveAngleFXConfig);
        mAngleMotor.setInverted(SwerveConfig.angleMotorInvert);
        mAngleMotor.setNeutralMode(SwerveConfig.angleNeutralMode);
        resetToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(Robot.swerve.config.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveConfig.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveConfig.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    public Rotation2d getCanCoder() {
        Rotation2d position = Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
        return position;
    }

    public void slowCANcoderStatusFrames() {
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 200);
    }

    public void fastCANcoderSatusFrames() {
        angleEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 10);
    }

    public SwerveModuleState getState() {
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

    public SwerveModulePosition getPosition() {
        double position =
                Conversions.FalconToMeters(
                        mDriveMotor.getSelectedSensorPosition(),
                        SwerveConfig.wheelCircumference,
                        SwerveConfig.driveGearRatio);
        Rotation2d angle =
                Rotation2d.fromDegrees(
                        Conversions.falconToDegrees(
                                mAngleMotor.getSelectedSensorPosition(),
                                SwerveConfig.angleGearRatio));
        return new SwerveModulePosition(position, angle);
    }
}

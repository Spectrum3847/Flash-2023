// Created by Spectrum3847

// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.swerve.configs.Flash2023;
import frc.robot.swerve.configs.Gamma2021;
import frc.robot.swerve.configs.Infrared3847;
import frc.robot.swerve.configs.Infrared8515;
import frc.robot.swerve.configs.SwerveConfig;
import frc.robot.swerve.gyros.GyroIO;
import frc.robot.swerve.gyros.Pigeon1;
import frc.robot.swerve.gyros.Pigeon2;

public class Swerve extends SubsystemBase {
    public SwerveConfig config;
    protected GyroIO gyro;
    protected Odometry odometry;
    public SwerveTelemetry telemetry;
    protected SwerveModule[] mSwerveMods;
    private SwerveModuleState[] SwerveModDesiredStates;

    public Swerve() {
        setName("Swerve");

        // Check robot type and make the config file
        switch (Robot.config.getRobotType()) {
            case GAMMA2021:
                config = Gamma2021.config;
                break;
            case INFRARED3847:
                config = Infrared3847.config;
                break;
            case INFRARED8515:
                config = Infrared8515.config;
                break;
            default:
                config = Flash2023.config;
                break;
        }

        switch (config.gyro.type) {
            case PIGEON1:
                gyro = new Pigeon1();
                break;
            case PIGEON2:
            default:
                gyro = new Pigeon2();
                break;
        }

        mSwerveMods =
                new SwerveModule[] {
                    new SwerveModule(0, config),
                    new SwerveModule(1, config),
                    new SwerveModule(2, config),
                    new SwerveModule(3, config)
                };
        odometry = new Odometry(this);
        telemetry = new SwerveTelemetry(this);

        // Set the initial module states to zero
        drive(0, 0, 0, true, false, new Translation2d());
    }

    @Override
    public void periodic() {
        odometry.update();
        telemetry.logModuleStates("SwerveModuleStates/Measured", getStates());
        telemetry.logModuleStates("SwerveModuleStates/Desired", getDesiredStates());
        telemetry.logModuleAbsolutePositions();
    }

    /**
     * Used to drive the swerve robot, should be called from commands that require swerve.
     *
     * @param fwdPositive Velocity of the robot fwd/rev, Forward Positive meters per second
     * @param leftPositive Velocity of the robot left/right, Left Positive meters per secound
     * @param omegaRadiansPerSecond Rotation Radians per second
     * @param fieldRelative If the robot should drive in field relative
     * @param isOpenLoop If the robot should drive in open loop
     * @param centerOfRotationMeters The center of rotation in meters
     */
    public void drive(
            double fwdPositive,
            double leftPositive,
            double omegaRadiansPerSecond,
            boolean fieldRelative,
            boolean isOpenLoop,
            Translation2d centerOfRotationMeters) {

        ChassisSpeeds speeds;
        if (fieldRelative) {
            speeds =
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                            fwdPositive, leftPositive, omegaRadiansPerSecond, getHeading());
        } else {
            speeds = new ChassisSpeeds(fwdPositive, leftPositive, omegaRadiansPerSecond);
        }

        SwerveModDesiredStates =
                config.swerveKinematics.toSwerveModuleStates(speeds, centerOfRotationMeters);

        // LOOK INTO THE OTHER CONSTRUCTOR FOR desaturateWheelSpeeds to see if it is better
        SwerveDriveKinematics.desaturateWheelSpeeds(
                SwerveModDesiredStates, config.tuning.maxVelocity);

        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(SwerveModDesiredStates[mod.moduleNumber], isOpenLoop);
        }
    }

    /** Reset AngleMotors to Absolute This is used to reset the angle motors to absolute position */
    public void resetSteeringToAbsolute() {
        for (SwerveModule mod : mSwerveMods) {
            mod.resetToAbsolute();
        }
    }

    /**
     * Reset the Heading to any angle
     *
     * @param heading Rotation2d representing the current heading of the robot
     */
    public void resetHeading(Rotation2d heading) {
        odometry.resetHeading(heading);
    }

    /**
     * Reset the pose2d of the robot
     *
     * @param pose
     */
    public void resetOdometry(Pose2d pose) {
        odometry.resetOdometry(pose);
    }

    /**
     * Get the heading of the robot
     *
     * @return current heading using the offset from Odometry class
     */
    public Rotation2d getHeading() {
        return odometry.getHeading();
    }

    /**
     * Ge the Pose of the odemotry class
     *
     * @return
     */
    public Pose2d getPoseMeters() {
        return odometry.getPoseMeters();
    }

    /**
     * Used by SwerveFollowCommand in Auto, assumes closed loop control
     *
     * @param desiredStates Meters per second and radians per second
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, config.tuning.maxVelocity);

        SwerveModDesiredStates = desiredStates;
        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    /**
     * Set both the drive and angle motor on each module to brake mode if enabled = true
     *
     * @param enabled true = brake mode, false = coast mode
     */
    public void setBrakeMode(boolean enabled) {
        for (SwerveModule mod : mSwerveMods) {
            mod.setBrakeMode(enabled);
        }
    }

    /**
     * Stop the drive and angle motor of each module And set desired states to 0 meters per second
     * and current module angles
     */
    public void stop() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : mSwerveMods) {
            mod.stop();
            states[mod.moduleNumber] =
                    new SwerveModuleState(0, Rotation2d.fromDegrees(mod.getTargetAngle()));
        }
        SwerveModDesiredStates = states;
    }

    /**
     * Gets the states of the modules from the modules directly, called once a loop
     *
     * @return the current module states
     */
    public SwerveModuleState[] getStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : mSwerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModuleState[] getDesiredStates() {
        return SwerveModDesiredStates;
    }

    /**
     * Get the module positions from the modules directly
     *
     * @return the current module positions
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for (SwerveModule mod : mSwerveMods) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        mSwerveMods[0].setVoltage(leftVolts);
        mSwerveMods[2].setVoltage(leftVolts);
        mSwerveMods[1].setVoltage(rightVolts);
        mSwerveMods[3].setVoltage(rightVolts);
    }

    public Object getAbsoluteAngle(boolean b) {
        return null;
    }
}

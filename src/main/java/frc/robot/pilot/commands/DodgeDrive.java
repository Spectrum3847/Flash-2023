// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.pilot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;
import java.util.function.DoubleSupplier;

public class DodgeDrive extends CommandBase {
    private double rotation;
    private Translation2d translation;

    private Swerve swerve;
    private DoubleSupplier ySupplier;
    private DoubleSupplier xSupplier;
    private DoubleSupplier rSupplier;
    private Translation2d centerOfRotationMeters;

    /** Creates a new DodgeDrive. */
    public DodgeDrive() {
        swerve = Robot.swerve;
        centerOfRotationMeters = new Translation2d();
        xSupplier = Robot.pilotGamepad::getDriveX;
        ySupplier = Robot.pilotGamepad::getDriveY;
        rSupplier = Robot.pilotGamepad::getDriveR;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    @Override
    public void execute() {

        double xAxis = xSupplier.getAsDouble();
        double yAxis = ySupplier.getAsDouble();
        double rAxis = rSupplier.getAsDouble();

        translation = new Translation2d(yAxis, xAxis);
        rotation = rAxis;

        Rotation2d gyroAngle = swerve.gyro.getYaw();
        Rotation2d translationAngle = translation.getAngle();

        Rotation2d heading = translationAngle.minus(gyroAngle);
        double angle = heading.getDegrees();
        if (rotation != 0) {
            if (angle < 45 || angle >= 315) {
                // negative rotation is clockwise
                // positive rotation is counter-clockwise
                if (rotation < 0) {
                    centerOfRotationMeters = SwerveConfig.frontRightLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.frontLeftLocation;
                }
            } else if (angle >= 45 && angle < 135) {
                if (rotation < 0) {
                    centerOfRotationMeters = SwerveConfig.frontLeftLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.backLeftLocation;
                }
            } else if (angle >= 135 && angle < 225) {
                if (rotation < 0) {
                    centerOfRotationMeters = SwerveConfig.backLeftLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.backRightLocation;
                }
            } else if (angle >= 225 && angle < 315) {
                if (rotation < 0) {
                    centerOfRotationMeters = SwerveConfig.backRightLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.frontRightLocation;
                }
            }
        }
        swerve.drive(translation, rotation, true, false, centerOfRotationMeters);
    }

    public void end(boolean interrupted) {
        swerve.stop();
    }
}

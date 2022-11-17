// Created by Spectrum3847
// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;
import java.util.function.DoubleSupplier;

public class SwerveDrive extends CommandBase {

    private double rotation;
    private Translation2d translation;
    private boolean fieldRelative;
    private boolean openLoop;

    private Swerve swerve;
    private DoubleSupplier ySupplier;
    private DoubleSupplier xSupplier;
    private DoubleSupplier rSupplier;
    private Translation2d centerOfRotationMeters;

    /**
     * Creates a SwerveDrive command that allows for simple x and y translation of the robot and r
     * rotation.
     *
     * @param fieldRelative
     * @param ySupplier
     * @param xSupplier
     */
    public SwerveDrive(
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier rSupplier,
            boolean fieldRelative,
            boolean openLoop,
            Translation2d centerOfRotationMeters) {
        this.swerve = Robot.swerve;
        addRequirements(swerve);
        this.fieldRelative = fieldRelative;
        this.openLoop = openLoop;
        this.ySupplier = ySupplier;
        this.xSupplier = xSupplier;
        this.rSupplier = rSupplier;
        this.centerOfRotationMeters = centerOfRotationMeters;
    }

    public SwerveDrive(
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier rSupplier,
            boolean fieldRelative,
            boolean openLoop) {
        this(xSupplier, ySupplier, rSupplier, fieldRelative, openLoop, new Translation2d());
    }

    public SwerveDrive(
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier rSupplier,
            boolean fieldRelative) {
        this(xSupplier, ySupplier, rSupplier, fieldRelative, false, new Translation2d());
    }

    public SwerveDrive(
            DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier rSupplier) {
        this(xSupplier, ySupplier, rSupplier, true, false);
    }

    public void intialize() {}

    @Override
    public void execute() {
        double xAxis = xSupplier.getAsDouble();
        double yAxis = ySupplier.getAsDouble();
        double rAxis = rSupplier.getAsDouble();

        translation = new Translation2d(yAxis, xAxis);
        rotation = rAxis;
        swerve.drive(translation, rotation, fieldRelative, openLoop, centerOfRotationMeters);
    }

    public void end(boolean interrupted) {
        swerve.stop();
    }
}

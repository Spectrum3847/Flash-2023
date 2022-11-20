package frc.robot.pilot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;
import java.util.function.DoubleSupplier;

public class DodgeDrive extends CommandBase {
    private Translation2d translation;
    private boolean centerHasBeenSet = false;

    private Swerve swerve;
    private DoubleSupplier leftPositiveSupplier;
    private DoubleSupplier fwdPositiveSupplier;
    private DoubleSupplier ccwPositiveSupplier;
    private Translation2d centerOfRotationMeters;

    /** Creates a new DodgeDrive. */
    public DodgeDrive() {
        swerve = Robot.swerve;
        centerOfRotationMeters = new Translation2d();
        fwdPositiveSupplier = Robot.pilotGamepad::getDriveFwdPositive;
        leftPositiveSupplier = Robot.pilotGamepad::getDriveLeftPositive;
        ccwPositiveSupplier = Robot.pilotGamepad::getDriveCCWPositive;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    @Override
    public void execute() {

        double fwdPositive = fwdPositiveSupplier.getAsDouble();
        double leftPositive = leftPositiveSupplier.getAsDouble();
        double ccwPositive = ccwPositiveSupplier.getAsDouble();

        translation = new Translation2d(fwdPositive, leftPositive);

        Rotation2d gyroAngle = Robot.pose.getHeading();
        Rotation2d translationAngle = translation.getAngle();

        Rotation2d heading = translationAngle.minus(gyroAngle);
        double angle = heading.getDegrees();
        if (ccwPositive != 0 && !centerHasBeenSet) {
            if (angle < 45 || angle >= 315) {
                // negative rotation is clockwise
                // positive rotation is counter-clockwise
                if (ccwPositive > 0) {
                    centerOfRotationMeters = SwerveConfig.frontRightLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.frontLeftLocation;
                }
            } else if (angle >= 45 && angle < 135) {
                if (ccwPositive > 0) {
                    centerOfRotationMeters = SwerveConfig.frontLeftLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.backLeftLocation;
                }
            } else if (angle >= 135 && angle < 225) {
                if (ccwPositive > 0) {
                    centerOfRotationMeters = SwerveConfig.backLeftLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.backRightLocation;
                }
            } else if (angle >= 225 && angle < 315) {
                if (ccwPositive > 0) {
                    centerOfRotationMeters = SwerveConfig.backRightLocation;
                } else {
                    centerOfRotationMeters = SwerveConfig.frontRightLocation;
                }
            }
            Robot.log.logger.recordOutput("CoR", centerOfRotationMeters);
            centerHasBeenSet = true;
        }
        swerve.drive(fwdPositive, leftPositive, ccwPositive, true, false, centerOfRotationMeters);
    }

    public void end(boolean interrupted) {
        swerve.stop();
    }
}

package frc.robot.auton.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class AutonCommands {

    public static Command checkFirstPath(PathPlannerTrajectory traj, boolean isFirstPath) {
        return new InstantCommand(
                () -> {
                    // Reset odometry for the first path you run during auto
                    if (true) {
                        Robot.swerve.resetOdometry(traj.getInitialHolonomicPose());
                    }
                },
                Robot.swerve);
    }

    public static Command setBrakeMode() {
        return new RunCommand(() -> Robot.swerve.setBrakeMode(true));
    }

    public static Command setCoastMode() {
        return new RunCommand(() -> Robot.swerve.setBrakeMode(false));
    }

    public static Command setGryoDegrees(double deg) {
        return new InstantCommand(() -> Robot.swerve.resetHeading(Rotation2d.fromDegrees(deg)))
                .andThen(
                        new PrintCommand(
                                "Gyro Degrees: " + Robot.swerve.getHeading().getDegrees()));
    }
}

package frc.robot.auton.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;

public class AutonCommands {

    public static Command intializePathFollowing(PathPlannerTrajectory path) {
        return new SequentialCommandGroup(
                AutonCommands.setBrakeMode()
                        .withTimeout(0.25), // set brake mode and pause for 1/4 second
                AutonCommands.intializeGyroAngle(path), // set gyro to initial heading
                AutonCommands.resetOdometry(path) // reset odometry to the initial position
                );
    }

    public static Command setBrakeMode() {
        return new RunCommand(() -> Robot.swerve.brakeMode(true));
    }

    public static Command setCoastMode() {
        return new RunCommand(() -> Robot.swerve.brakeMode(false));
    }

    public static Command setGryoDegrees(double deg) {
        return new InstantCommand(() -> Robot.swerve.resetHeading(Rotation2d.fromDegrees(deg)))
                .andThen(
                        new PrintCommand(
                                "Gyro Degrees: " + Robot.swerve.getHeading().getDegrees()));
    }

    public static Command intializeGyroAngle(PathPlannerTrajectory path) {
        PathPlannerState s = (PathPlannerState) path.getStates().get(0);
        return setGryoDegrees(s.holonomicRotation.getDegrees());
    }

    public static Command resetOdometry(PathPlannerTrajectory path) {
        Pose2d tempPose = path.getInitialPose();
        PathPlannerState s = (PathPlannerState) path.getStates().get(0);
        Pose2d tempPose2 = new Pose2d(tempPose.getTranslation(), s.holonomicRotation);
        return new InstantCommand(() -> Robot.swerve.odometry.resetOdometry(tempPose2));
    }
}

package frc.robot.trajectories;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.swerve.commands.PilotSwerve;

public class TrajectoriesCommands {

    public static Command snapPilotDrive(double goalAngle) {
        return resetThetaController()
                .andThen(new PilotSwerve(() -> Robot.trajectories.calculteTheta(goalAngle)))
                .withName("snapPilotDrive");
    }

    public static Command resetThetaController() {
        return new InstantCommand(() -> Robot.trajectories.resetTheta(), Robot.trajectories);
    }
}

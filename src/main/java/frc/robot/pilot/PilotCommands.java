package frc.robot.pilot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.swerve.commands.SwerveDrive;
import frc.robot.trajectories.TrajectoriesCommands;

/** Add your docs here. */
public class PilotCommands {

    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilotGamepad.setDefaultCommand(rumblePilot(0).withName("DisablePilotRumble"));
    }

    // Field Oriented Drive
    public static Command pilotSwerve() {
        return new SwerveDrive(
                        () -> Robot.pilotGamepad.getDriveX(),
                        () -> Robot.pilotGamepad.getDriveY(),
                        () -> Robot.pilotGamepad.getDriveR(),
                        true,
                        false)
                .withName("PilotSwerve");
    }

    // Robot Oriented Drive
    public static Command fpvPilotSwerve() {
        return new SwerveDrive(
                        () -> Robot.pilotGamepad.getDriveX(),
                        () -> Robot.pilotGamepad.getDriveY(),
                        () -> Robot.pilotGamepad.getDriveR(),
                        false,
                        false)
                .withName("fpvPilotSwerve");
    }

    // Drive while aiming to a specific angle, uses theta controller from Trajectories
    public static Command aimPilotDrive(double goalAngle) {
        return TrajectoriesCommands.resetThetaController()
                .andThen(
                        new SwerveDrive(
                                () -> Robot.pilotGamepad.getDriveX(),
                                () -> Robot.pilotGamepad.getDriveY(),
                                Robot.trajectories.calculteThetaSupplier(goalAngle),
                                true,
                                false))
                .withName("AimPilotDrive");
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command rumblePilot(double intensity) {
        return new RunCommand(() -> Robot.pilotGamepad.rumble(intensity), Robot.pilotGamepad)
                .withName("RumblePilot");
    }
}

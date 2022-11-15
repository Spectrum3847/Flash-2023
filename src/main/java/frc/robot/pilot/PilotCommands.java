package frc.robot.pilot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.swerve.commands.SwerveDrive;
import frc.robot.trajectories.TrajectoriesCommands;
import java.util.function.DoubleSupplier;

/** Should contain commands used only by the pilot controller and the rumble pilot command */
public class PilotCommands {

    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilotGamepad.setDefaultCommand(rumblePilot(0).withName("DisablePilotRumble"));
    }

    /** Field Oriented Drive */
    public static Command pilotSwerve() {
        return new SwerveDrive(
                        () -> Robot.pilotGamepad.getDriveX(),
                        () -> Robot.pilotGamepad.getDriveY(),
                        () -> Robot.pilotGamepad.getDriveR(),
                        true,
                        false)
                .withName("PilotSwerve");
    }

    /** Robot Oriented Drive */
    public static Command fpvPilotSwerve() {
        return new SwerveDrive(
                        () -> Robot.pilotGamepad.getDriveX(),
                        () -> Robot.pilotGamepad.getDriveY(),
                        () -> Robot.pilotGamepad.getDriveR(),
                        false,
                        false)
                .withName("fpvPilotSwerve");
    }

    /** Drive while aiming to a specific angle, uses theta controller from Trajectories */
    public static Command aimPilotDrive(double goalAngle) {
        return aimPilotDrive(() -> goalAngle);
    }

    /** Reset the Theata Controller and then run the SwerveDrive command and pass a goal Supplier */
    public static Command aimPilotDrive(DoubleSupplier goalAngleSupplier) {
        return TrajectoriesCommands.resetThetaController()
                .andThen(
                        new SwerveDrive(
                                () -> Robot.pilotGamepad.getDriveX(),
                                () -> Robot.pilotGamepad.getDriveY(),
                                Robot.trajectories.calculteThetaSupplier(goalAngleSupplier),
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

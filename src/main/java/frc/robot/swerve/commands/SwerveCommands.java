package frc.robot.swerve.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class SwerveCommands {
    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(new PilotSwerve(true, false));
    }

    public static Command FPVswerve() {
        return new PilotSwerve(false, false);
    }
}

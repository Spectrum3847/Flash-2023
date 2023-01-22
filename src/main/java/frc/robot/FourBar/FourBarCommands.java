package frc.robot.FourBar;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class FourBarCommands {
    public static void setupDefaultCommand() {}

    public static Command setManualOutput(double speed) {
        return new RunCommand(() -> Robot.elevator.setManualOutput(speed), Robot.intake);
    }
}

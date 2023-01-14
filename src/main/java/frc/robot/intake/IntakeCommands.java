package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

// above all copied from PilotCommands.java

public class IntakeCommands {
    public static void setupDefaultCommand() {}

    public static double addIntakeValue(double intakeValue) {
        intakeValue += 1;
        return intakeValue;
    }

    public static Command setOutput(double value) {
        return new RunCommand(() -> Robot.intake.setManualOutput(value), Robot.intake);
    }
}

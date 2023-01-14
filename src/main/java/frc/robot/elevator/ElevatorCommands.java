package frc.robot.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

// above all copied from PilotCommands.java

public class ElevatorCommands {
    public static void setupDefaultCommand() {}

    public static double addElevatorValue(double elevatorValue) {
        elevatorValue += 1;
        return elevatorValue;
    }

    public static Command setOutput(double value) {
        return new RunCommand(() -> Robot.elevator.setManualOutput(value), Robot.elevator);
    }
}

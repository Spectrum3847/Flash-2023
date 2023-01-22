package frc.robot.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.SpectrumLib.util.Conversions;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

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

    // below doesn't work:((
    public static Command setMMPosition(double position) {
        position = Elevator.inchesToMeters(position);
        position = Elevator.metersToFalcon(position, 4.03391, 7.75);
        // double newspeed = Conversions.RPMToFalcon(1, 1.0); // speed is 3 for now
        // Robot.elevator.setManualOutput(newspeed); // added to get it to move
        double newpos = position;
        RobotTelemetry.print("running");
        return new RunCommand(() -> Robot.elevator.setMMPosition(newpos), Robot.elevator);
        // set diameter to 1.2815, gear ratio to 62/8
    }

    public static Command setManualOutput(double speed) {
        double newspeed = Conversions.RPMToFalcon(speed, 1.0);
        return new RunCommand(() -> Robot.elevator.setManualOutput(newspeed), Robot.elevator);
    }

    public static Command setEncoder(double position) {
        return new RunCommand(() -> Robot.elevator.setEncoder(position), Robot.elevator);
    }
}

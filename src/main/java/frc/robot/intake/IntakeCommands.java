package frc.robot.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

public class IntakeCommands {
    
    public static void setupDefaultCommand() {
        Robot.intake.setDefaultCommand(stopAllMotors());
    }

    public static Command intake(){
        return setLauncher(1.0).alongWith(setUpperRoller(1.0));
    }

    public static Command setLowerRoller(double value) {
        return new RunCommand(() -> Robot.intake.setLowerRoller(value), Robot.intake);
    }

    public static Command setUpperRoller(double value) {
        return new RunCommand(() -> Robot.intake.setUpperRoller(value), Robot.intake);
    }

    public static Command setLauncher(double value) {
        return new RunCommand(() -> Robot.intake.setLauncher(value), Robot.intake);
    }

    public static Command stopAllMotors(){
        return new RunCommand(() -> Robot.intake.stopAll(), Robot.intake);
    }
}

package frc.robot.launcher;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

// above all copied from PilotCommands.java

public class LauncherCommands {
    public static void setupDefaultCommand() {
        Robot.launcher.setVelocity(5000);
        // Robot.launcher.motorLeader.set(TalonFXControlMode.Velocity, 500);
    }

    public static double addLauncherValue(double launcherValue) {
        launcherValue += 1;
        return launcherValue;
    }

    public static Command setOutput(double value) {
        return new RunCommand(() -> Robot.launcher.setManualOutput(value), Robot.launcher);
    }
}

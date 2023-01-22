package frc.robot.launcher;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.SpectrumLib.util.Conversions;
import frc.robot.Robot;

// above all copied from PilotCommands.java

public class LauncherCommands {
    public static void setupDefaultCommand() {
        // Robot.launcher.setVelocity(500);
        // Robot.launcher.motorLeader.set(TalonFXControlMode.Velocity, 500);
    }

    public static double addLauncherValue(double launcherValue) {
        launcherValue += 1;
        return launcherValue;
    }

    public static Command setOutput(double value) {
        return new RunCommand(() -> Robot.launcher.setManualOutput(value), Robot.launcher);
    }

    public static Command setVelocity(double velocity) {
        // double error = velocity - Robot.launcher.getRPM();
        // double addon = error * LauncherConfig.kP;
        // velocity = velocity + addon;
        velocity = velocity * 1.5;
        double newvel = Conversions.RPMToFalcon(velocity, 1.0);
        // double newvel2 = newvel;

        // velocity = Conversions.RPMToFalcon(3000, 1.0);
        // double error = 3000 - Robot.launcher.getRPM();
        // double addon = error * 2;
        // velocity = addon + velocity;
        // double newvel = velocity;
        return new RunCommand(() -> Robot.launcher.setVelocity(newvel)); // newvel
    }

    // call the below later the in the repeat every 20 ms part
    public static double getLauncherVelocity(double targetVelocity) {
        return (Robot.launcher.getRotationsPerSec() + (LauncherConfig.kF * targetVelocity)) * 10;
        // for this to work, you have to edit PilotGamepad and Robot target speed
    }
}

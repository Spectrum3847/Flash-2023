package frc.robot.vision;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

public class VisionCommands {

    public static Command printYawInfo() {
        return new InstantCommand(
                () -> {
                    RobotTelemetry.print(
                            "Yaw (D): "
                                    + Robot.vision.getYaw()
                                    + "|| gyro (D): "
                                    + Robot.swerve.getHeading().getDegrees()
                                    + " || Aiming at: "
                                    + (Robot.vision.getYaw() + Robot.swerve.getHeading().getDegrees()));
                },
                Robot.vision);
    }
}

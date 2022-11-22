package frc.robot.vision;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

public class VisionCommands {

    public static Command printYawInfo() {
        return new InstantCommand(
                () ->
                        RobotTelemetry.print(
                                "Yaw (D): "
                                        + Robot.vision.getYaw()
                                        + "|| gyro (D): "
                                        + Robot.swerve.getHeading().getDegrees()
                                        + " || Aiming at: "
                                        + (Robot.vision.getYaw()
                                                + Robot.swerve.getHeading().getDegrees())));
    }

    public static Command printEstimatedPoseInfo() {
        return new InstantCommand(
                () ->
                        RobotTelemetry.print(
                                "Estimated Pose: | X: "
                                        + Robot.vision
                                                .currentPose
                                                .getFirst()
                                                .getTranslation()
                                                .getX()
                                        + " | Y: "
                                        + Robot.vision
                                                .currentPose
                                                .getFirst()
                                                .getTranslation()
                                                .getY()
                                        + " | Z: "
                                        + Robot.vision
                                                .currentPose
                                                .getFirst()
                                                .getTranslation()
                                                .getZ()
                                        + " | Rotation (D): "
                                        + Units.radiansToDegrees(
                                                Robot.vision
                                                        .currentPose
                                                        .getFirst()
                                                        .getRotation()
                                                        .getZ())
                                        + " | Latency: "
                                        + Robot.vision.currentPose.getSecond().doubleValue()));
    }
}

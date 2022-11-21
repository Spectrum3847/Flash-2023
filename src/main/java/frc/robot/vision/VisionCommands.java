package frc.robot.vision;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Robot;

public class VisionCommands {

    public static Command printYawInfo() {
        return new PrintCommand(
                "Yaw (D): "
                        + Robot.vision.getYaw()
                        + "|| gyro (D): "
                        + Robot.swerve.getHeading().getDegrees()
                        + " || Aiming at: "
                        + (Robot.vision.getYaw() + Robot.swerve.getHeading().getDegrees()));
    }

    public static Command printEstimatedPoseInfo() {
        Pair<Pose3d, Double> estimatedPose = Robot.vision.getEstimatedPose();
        return new PrintCommand(
                "Estimated Pose: | X: "
                        + estimatedPose.getFirst().getTranslation().getX()
                        + " | Y: "
                        + estimatedPose.getFirst().getTranslation().getY()
                        + " | Z: "
                        + estimatedPose.getFirst().getTranslation().getZ()
                        + " | Rotation (D): "
                        + Units.radiansToDegrees(estimatedPose.getFirst().getRotation().getZ())
                        + " | Latency: "
                        + estimatedPose.getSecond().doubleValue());
    }
}
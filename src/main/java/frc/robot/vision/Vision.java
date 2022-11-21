package frc.robot.vision;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.vision.RobotPoseEstimator.PoseStrategy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

@SuppressWarnings("unused")
public class Vision extends SubsystemBase {
    public final VisionConfig config;
    public final RobotPoseEstimator poseEstimator;
    public final PhotonCamera[] cameras;

    private ArrayList<Pair<PhotonCamera, Transform3d>> cameraPairs;
    private double yaw, pitch, area, poseAmbiguity, captureTime;
    private int targetId;

    // testing
    private final DecimalFormat df = new DecimalFormat();
    private double lastYaw = 0;
    private boolean targetFound = true;

    public Vision() {
        setName("Vision");
        config = new VisionConfig();
        cameras = 
            new PhotonCamera[] {
                VisionConfig.LL.config.camera
                /* Add cameras here */
            };
        // setting up the pair(s) of camera and their 3d transformation from the center of the robot to give to the pose estimator
        for(int i = 0; i < cameras.length; i++) {
            cameraPairs.add(getCameraPair(getCameraConfig(i)));
        }

        poseEstimator =
                new RobotPoseEstimator(
                        VisionConfig.tagMap, VisionConfig.strategy, cameraPairs);

        // printing purposes
        df.setMaximumFractionDigits(2);
    }

    @Override
    public void periodic() {
        // get targets & basic data from a single camera
        PhotonPipelineResult results = cameras[0].getLatestResult();
        if (results.hasTargets()) {
            PhotonTrackedTarget target = results.getBestTarget();
            // negate it because the target.getYaw is the yaw of the robot from the target which is
            // the opposite direction. Or photonvision yaw is CW+ CCW-
            yaw = -target.getYaw();
            pitch = target.getPitch();
            area = target.getArea(); 
            targetId = target.getFiducialId();
            poseAmbiguity = target.getPoseAmbiguity();
            captureTime = Timer.getFPGATimestamp() - (results.getLatencyMillis() / 1000d);

            // printing
            if (lastYaw == 0) {
                lastYaw = yaw;
            }

            if (Math.round(lastYaw) != Math.round(yaw)) {
                // printDebug(targetId, yaw, pitch, area, poseAmbiguity, captureTime);
            }

            lastYaw = yaw;
            targetFound = true;
        } else {
            // no target found
            yaw = 0.0;
            if (targetFound) {
                RobotTelemetry.print("Lost target");
                targetFound = false;
            }
        }
    }

    // pose estimating
    public Pair<Pose3d, Double> getEstimatedPose() {
        return poseEstimator.update();
    }

    // aiming
    public double getRadiansToTarget() {
        RobotTelemetry.print(
                "Yaw (D): "
                        + yaw
                        + "|| gyro (D): "
                        + Robot.swerve.getHeading().getDegrees()
                        + " || Aiming at: "
                        + (yaw + Robot.swerve.getHeading().getDegrees()));
        return Units.degreesToRadians(yaw) + Robot.swerve.getHeading().getRadians();
    }

    private void printDebug(
            int targetId,
            double yaw,
            double pitch,
            double area,
            double poseAmbiguity,
            double captureTime) {
        RobotTelemetry.print(
                "Target ID: "
                        + targetId
                        + " | Target Yaw: "
                        + df.format(yaw)
                        + " | Pitch: "
                        + df.format(pitch)
                        + " | Area: "
                        + df.format(area)
                        + " | Pose Ambiguity: "
                        + poseAmbiguity
                        + " | Capture Time: "
                        + df.format(captureTime));
    }

    public double getYaw() {
        return yaw;
    }

    public Pair<PhotonCamera, Transform3d> getCameraPair(CameraConfig config) { 
        return new Pair<PhotonCamera, Transform3d>(
            config.camera, 
            new Transform3d(
                new Translation3d(
                    config.cameraToRobotX, 
                    config.cameraToRobotY, 
                    config.cameraToRobotZ), 
                new Rotation3d(
                    config.cameraRollRadians, config.cameraPitchRadians, config.cameraYawRadians)));
        
    }

    public CameraConfig getCameraConfig(int iteration) {
        switch (iteration) {
            case 0: 
                return VisionConfig.LL.config;
            /* add camera configs here */
            default:
                RobotTelemetry.print("Something went wrong trying to get camera config");
                return VisionConfig.LL.config;
        }
    }
}

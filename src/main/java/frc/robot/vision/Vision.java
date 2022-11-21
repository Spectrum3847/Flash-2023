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
import java.util.HashMap;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

@SuppressWarnings("unused")
public class Vision extends SubsystemBase {
    public VisionConfig config;
    public final PhotonCamera camera;
    public RobotPoseEstimator poseEstimator;

    private double yaw, pitch, area, poseAmbiguity, captureTime;
    private int targetId;

    // testing
    private DecimalFormat df = new DecimalFormat();
    private double lastYaw = 0;
    private boolean targetFound = true;

    // there is probably a better way to interact with config
    public Vision() {
        setName("Vision");
        config = new VisionConfig();
        camera = new PhotonCamera(config.CAMERA_NAME);

        // setting up the pair(s) of camera and their 3d transformation from the center of the robot
        config.cameraPair =
                new Pair<PhotonCamera, Transform3d>(
                        camera,
                        new Transform3d(
                                new Translation3d(
                                        config.CAMERA_TO_ROBOT_X_METERS,
                                        config.CAMERA_TO_ROBOT_Y_METERS,
                                        config.CAMERA_TO_ROBOT_Z_METERS),
                                new Rotation3d(0, config.CAMERA_PITCH_RADIANS, 0)));
        config.cameras.add(config.cameraPair);

        poseEstimator =
                new RobotPoseEstimator(
                        config.tagMap, PoseStrategy.LOWEST_AMBIGUITY, config.cameras);

        // printing purposes
        df.setMaximumFractionDigits(2);
    }

    @Override
    public void periodic() {
        // get targets & basic data
        PhotonPipelineResult results = camera.getLatestResult();
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
}

package frc.robot.vision;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

import java.text.DecimalFormat;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

@SuppressWarnings("unused")
public class Vision extends SubsystemBase {
    public VisionConfig config;
    public final PhotonCamera camera;

    private double yaw, pitch, area, poseAmbiguity, captureTime;
    private int targetId;

    // testing
    private DecimalFormat df = new DecimalFormat();
    private double lastCaptureTime = 0;
    private double lastYaw = 0;
    private boolean targetFound = true;

    public Vision() {
        setName("Vision");
        config = new VisionConfig();
        camera = new PhotonCamera(config.CAMERA_NAME);

        // testing
        df.setMaximumFractionDigits(2);
    }

    @Override
    public void periodic() {
        // get targets & basic data
        PhotonPipelineResult results = camera.getLatestResult();
        if (results.hasTargets()) {
            PhotonTrackedTarget target = results.getBestTarget();
            yaw = target.getYaw();
            pitch = target.getPitch();
            area = target.getArea();
            targetId = target.getFiducialId();
            poseAmbiguity = target.getPoseAmbiguity();
            captureTime = Timer.getFPGATimestamp() - (results.getLatencyMillis() / 1000d);

            if (lastCaptureTime == 0) {
                lastCaptureTime = captureTime;
            }
            if (lastYaw == 0) {
                lastYaw = yaw;
            }

            if (Math.round(lastYaw) != Math.round(yaw)) {
                // printDebug(targetId, yaw, pitch, area, poseAmbiguity, captureTime);
            }

            lastCaptureTime = captureTime;
            lastYaw = yaw;
            targetFound = true;
        } else {
            yaw = 0.0;
            if (targetFound) {
                RobotTelemetry.print("Lost target");
                targetFound = false;
            }
        }
    }

    public double getRadiansToTarget() {
        RobotTelemetry.print("Yaw (D): " + yaw + "|| gyro (D): " + Robot.swerve.getHeading().getDegrees());
        return Units.degreesToRadians(yaw) + Robot.swerve.getHeading().getDegrees();
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
}

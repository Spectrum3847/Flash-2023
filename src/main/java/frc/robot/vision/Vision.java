package frc.robot.vision;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import java.text.DecimalFormat;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class Vision extends SubsystemBase {
    public VisionConfig config;
    public final PhotonCamera camera;
    private double yaw = 0.0;

    // testing
    private DecimalFormat df = new DecimalFormat();
    private double previousCaptureTime = 0;
    private double previousYaw = 0;

    public Vision() {
        setName("Vision");
        config = new VisionConfig();
        camera = new PhotonCamera(config.CAMERA_NAME);

        // testing
        df.setMaximumFractionDigits(2);
    }

    @Override
    public void periodic() {
        // get targets
        PhotonPipelineResult results = camera.getLatestResult();
        if (results.hasTargets()) {
            PhotonTrackedTarget target = results.getBestTarget();
            yaw = target.getYaw();
            double pitch = target.getPitch();
            double area = target.getArea();
            int targetId = target.getFiducialId();
            double poseAmbiguity = target.getPoseAmbiguity();
            double captureTime = Timer.getFPGATimestamp() - (results.getLatencyMillis() / 1000d);

            if (previousCaptureTime == 0) {
                previousCaptureTime = captureTime;
            }
            if (previousYaw == 0) {
                previousYaw = yaw;
            }

            if (Math.round(previousYaw) != Math.round(yaw)) {
                printDebug(targetId, yaw, pitch, area, poseAmbiguity, captureTime);
            }

            previousCaptureTime = captureTime;
            previousYaw = yaw;
        } else {
            yaw = 0.0;
            System.out.println("Lost target");
        }
    }

    public double getRadiansToTarget() {
        return Units.degreesToRadians(yaw) + Robot.swerve.gyro.getRadians();
    }

    private void printDebug(
            int targetId,
            double yaw,
            double pitch,
            double area,
            double poseAmbiguity,
            double captureTime) {
        System.out.println(
                "Target ID: "
                        + targetId
                        + " | Yaw: "
                        + df.format(Units.radiansToDegrees(getRadiansToTarget()))
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

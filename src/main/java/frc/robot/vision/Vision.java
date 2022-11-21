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
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

@SuppressWarnings("unused")
public class Vision extends SubsystemBase {
    public final VisionConfig config;
    public final RobotPoseEstimator poseEstimator;
    public final PhotonCamera[] cameras;

    private ArrayList<Pair<PhotonCamera, Transform3d>> cameraPairs;

    public Vision() {
        setName("Vision");
        config = new VisionConfig();
        cameras =
                new PhotonCamera[] {VisionConfig.LL.config.camera
                    /* Add cameras here */
                };
        // setting up the pair(s) of camera and their 3d transformation from the center of the robot
        // to give to the pose estimator
        for (int i = 0; i < cameras.length; i++) {
            cameraPairs.add(getCameraPair(getCameraConfig(i)));
        }

        poseEstimator =
                new RobotPoseEstimator(VisionConfig.tagMap, VisionConfig.strategy, cameraPairs);
    }

    @Override
    public void periodic() {}

    // pose estimation
    public Pair<Pose3d, Double> getEstimatedPose() {
        return poseEstimator.update();
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
                                config.cameraRollRadians,
                                config.cameraPitchRadians,
                                config.cameraYawRadians)));
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

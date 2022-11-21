package frc.robot.vision;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.apriltag.AprilTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.photonvision.PhotonCamera;

@SuppressWarnings("unused")
public final class VisionConfig {
    public static Map<Integer, Pose3d> tagMap;
    public static Pair<PhotonCamera, Transform3d> cameraPair;
    public static ArrayList<Pair<PhotonCamera, Transform3d>> cameras;

    /* April Tag setup */   
    public static final AprilTag tag0 =
            new AprilTag(0, new Pose3d(1.515, 7.286, 1.07, new Rotation3d(0, 0, -Math.PI / 2)));    
    

    public static final String CAMERA_NAME = "gloworm";

    /* Robot coordinate plane || away is +x, left is +y, up is +z */
    public static final double CAMERA_TO_ROBOT_X_METERS = Units.inchesToMeters(5.841);
    public static final double CAMERA_TO_ROBOT_Y_METERS = Units.inchesToMeters(-0.5);
    public static final double CAMERA_TO_ROBOT_Z_METERS =
            Units.inchesToMeters(16.178); // height from camera to robot center
    public static final double CAMERA_PITCH_RADIANS =
            Units.degreesToRadians(-26.138); // angle from looking straight forward -direction is looking up



    public VisionConfig() {
        cameras = new ArrayList<Pair<PhotonCamera, Transform3d>>();

        //eventually move to vision.java
        // setting up the map of April Tags
        // change it to be able to use multiple tags (in an array)
        tagMap = new HashMap<Integer, Pose3d>();
        tagMap.put(tag0.ID, tag0.pose);        
    }
}

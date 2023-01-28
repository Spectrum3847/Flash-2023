package frc.robot.vision;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import java.text.DecimalFormat;

public class Vision extends SubsystemBase {
    public PhotonVision photonVision;

    private NetworkTable table;
    private NetworkTableEntry tx, ty, ta;
    private DoubleArraySubscriber botPoseSub;
    private Transform3d botPoseTransform3d;
    private Pair<Pose3d, Double> photonVisionPose;

    // testing
    private final DecimalFormat df = new DecimalFormat();

    public Vision() {
        setName("Vision");
        table = NetworkTableInstance.getDefault().getTable("limelight");
        /* Creating bot pose sub */
        botPoseSub = table.getDoubleArrayTopic("botpose").subscribe(new double[] {});

        /* Limelight NetworkTable Retrieval */
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");

        /* PhotonVision Setup -- uncomment if running PhotonVision*/
        // photonVision = new PhotonVision();

        // printing purposes
        df.setMaximumFractionDigits(2);
    }

    @Override
    public void periodic() {
        /* PhotonVision Pose Estimation Retrieval */
        if (photonVision != null) {
            photonVision.update();
            photonVisionPose = photonVision.currentPose;
            /* Adding PhotonVision estimate to pose */
            if (photonVision.isValidPose()) {
                Robot.pose.addVisionMeasurement(
                        photonVisionPose.getFirst().toPose2d(), photonVision.getTimestampSeconds());
            }
        }
        
        /* Limelight Pose Estimation */
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);

        double[] dv = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        double[] robotPose = table.getEntry("botpose").getDoubleArray(dv);
        double[] subbedPose = botPoseSub.get();

        if (robotPose.length > 0) {
            SmartDashboard.putString("BotX", df.format(robotPose[0]));
            SmartDashboard.putString("BotY", df.format(robotPose[1]));
            SmartDashboard.putString("BotZ", df.format(robotPose[2]));
            SmartDashboard.putString("Bot1", df.format(robotPose[3]));
            SmartDashboard.putString("Bot2", df.format(robotPose[4]));
            SmartDashboard.putString("Bot3", df.format(robotPose[5]));

            /* Creating Transform3d object from raw values -- Rotation values could be in degrees which need to be converted*/
            botPoseTransform3d =
                    new Transform3d(
                            new Translation3d(robotPose[0], robotPose[1], robotPose[2]),
                            new Rotation3d(robotPose[3], robotPose[4], robotPose[5]));

            SmartDashboard.putNumberArray("RobotPose", subbedPose);
        }

        SmartDashboard.putString("tagX", df.format(x));
        SmartDashboard.putString("tagY", df.format(y));
        SmartDashboard.putString("tagArea", df.format(area));


    }
}

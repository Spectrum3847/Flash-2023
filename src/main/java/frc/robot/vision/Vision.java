package frc.robot.vision;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoubleArrayTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import java.text.DecimalFormat;

public class Vision extends SubsystemBase {
    public PhotonVision photonVision;
    public Pose2d botPose;

    private NetworkTable table;
    private NetworkTableEntry tx, ty, ta;
    private DoubleArraySubscriber poseSub;
    private Pose3d botPose3d;
    private Pair<Pose3d, Double> photonVisionPose;
    private boolean allianceColor = true; // temporary solution -- true is blue || false is red

    // testing
    private final DecimalFormat df = new DecimalFormat();

    public Vision() {
        setName("Vision");
        botPose = new Pose2d(0, 0, new Rotation2d(0));
        botPose3d = new Pose3d(0, 0, 0, new Rotation3d(0, 0, 0));
        table = NetworkTableInstance.getDefault().getTable("limelight");
        /* Creating bot pose sub using set alliance color */
        poseSub = chooseAlliance().subscribe(new double[] {});
        table.getEntry("ledMode")
                .setValue(0); // 0 will use the LED Mode set in the pipeline || 1 is force off

        /* Limelight NetworkTable Retrieval */
        tx = table.getEntry("tx"); // offset from camera in degrees
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");

        /* PhotonVision Setup -- uncomment if running PhotonVision*/
        // photonVision = new PhotonVision();

        // printing purposes
        df.setMaximumFractionDigits(2);
    }

    @Override
    public void periodic() {
        /* Limelight Pose Estimation */
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);
        double[] subbedPose = poseSub.get();

        if (subbedPose.length > 0) {
            SmartDashboard.putString("BotX", df.format(subbedPose[0]));
            SmartDashboard.putString("BotY", df.format(subbedPose[1]));
            SmartDashboard.putString("BotZ", df.format(subbedPose[2]));
            SmartDashboard.putString("Bot1", df.format(subbedPose[3]));
            SmartDashboard.putString("Bot2", df.format(subbedPose[4]));
            SmartDashboard.putString("Bot3", df.format(subbedPose[5]));

            /* Creating Transform3d object from raw values -- Rotation values could be in degrees which need to be converted*/
            botPose3d =
                    new Pose3d(
                            new Translation3d(subbedPose[0], subbedPose[1], subbedPose[2]),
                            new Rotation3d(subbedPose[3], subbedPose[4], subbedPose[5]));
        }

        SmartDashboard.putString("tagX", df.format(x));
        SmartDashboard.putString("tagY", df.format(y));
        SmartDashboard.putString("tagArea", df.format(area));

        botPose = botPose3d.toPose2d();

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
    }

    /**
     * Returns the corresponding limelight pose for the current alliance color set in Vision.java
     *
     * @return NetworkTableEntry either botpose_wpiblue (blue driverstation origin) or
     *     botpose_wpired (red driverstation origin)
     */
    public DoubleArrayTopic chooseAlliance() {
        if (allianceColor) {
            return table.getDoubleArrayTopic("botpose_wpiblue");
        } else {
            return table.getDoubleArrayTopic("botpose_wpired");
        }
    }

    public void printDebug() {
        RobotTelemetry.print(
                poseSub.getTopic().toString()
                        + ": \n\tX: "
                        + botPose3d.getX()
                        + " || Y: "
                        + botPose3d.getY()
                        + " || Z: "
                        + botPose3d.getZ()
                        + " || Roll: "
                        + botPose3d.getRotation().getX()
                        + " || Pitch: "
                        + botPose3d.getRotation().getY()
                        + " || Yaw: "
                        + botPose3d.getRotation().getZ());
    }
}

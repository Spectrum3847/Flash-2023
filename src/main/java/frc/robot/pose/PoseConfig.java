package frc.robot.pose;

public class PoseConfig {

    // Increase these numbers to trust your model's state estimates less.
    public final double kPositionStdDevX = 0.1;
    public final double kPositionStdDevY = 0.1;
    public final double kPositionStdDevTheta = 10;

    // Increase these numbers to trust global measurements from vision less.
    public final double kVisionStdDevX = 0.05;
    public final double kVisionStdDevY = 0.05;
    public final double kVisionStdDevTheta = 5;
}

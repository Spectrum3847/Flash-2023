package frc.robot.swerve;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SwerveTelemetry {
    protected ShuffleboardTab tab;

    public SwerveTelemetry() {
        tab = Shuffleboard.getTab("Swerve");
    }

    public void testMode() {}
}

package frc.robot.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    public VisionConfig config;

    public Vision() {
        setName("Vision");
        config = new VisionConfig();
    }
}

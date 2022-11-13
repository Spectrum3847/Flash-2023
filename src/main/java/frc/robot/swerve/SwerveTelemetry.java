package frc.robot.swerve;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import java.util.ArrayList;
import java.util.List;
import org.littletonrobotics.junction.Logger;

public class SwerveTelemetry {
    protected ShuffleboardTab tab;

    public SwerveTelemetry() {
        tab = Shuffleboard.getTab("Swerve");
    }

    public void testMode() {}

    public void logModuleStates(String key, SwerveModuleState[] states) {
        List<Double> dataArray = new ArrayList<Double>();
        for (int i = 0; i < 4; i++) {
            dataArray.add(states[i].angle.getRadians());
            dataArray.add(states[i].speedMetersPerSecond);
        }
        Logger.getInstance()
                .recordOutput(key, dataArray.stream().mapToDouble(Double::doubleValue).toArray());
    }
}

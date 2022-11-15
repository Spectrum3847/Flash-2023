package frc.robot.swerve;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

public class SwerveTelemetry {
    protected ShuffleboardTab tab;
    private Swerve swerve;

    public SwerveTelemetry(Swerve swerve) {
        this.swerve = swerve;
        tab = Shuffleboard.getTab("Swerve");
    }

    public void testMode() {}

    public void logModuleStates(String key, SwerveModuleState[] states) {
        Robot.log.logger.recordOutput(key, states);
    }

    public void logModuleAbsolutePositions() {
        for (SwerveModule mod : swerve.mSwerveMods) {
            Robot.log.logger.recordOutput(
                    "Mod " + mod.moduleNumber + " Absolute", mod.getCanCoderAngle().getDegrees());
        }
    }
}

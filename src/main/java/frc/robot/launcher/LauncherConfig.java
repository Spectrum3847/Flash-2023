package frc.robot.launcher;

import frc.SpectrumLib.subsystems.rollerMech.RollerMechConfig;
import frc.robot.RobotConfig.Motors;

public class LauncherConfig extends RollerMechConfig {
    public static final String name = "Launcher";
    public final int diameterInches = 4;

    public static final int launcherMotorID = Motors.launcherMotor;

    public double kP = 0.0;
    public double kI = 0.0;
    public double kD = 0.0;
    public double kF = 0.0;
    public double kIz = 0.0;

    public LauncherConfig() {
        super(name);
    }
}

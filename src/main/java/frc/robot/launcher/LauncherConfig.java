package frc.robot.launcher;

import frc.SpectrumLib.subsystems.rollerMech.RollerMechConfig;
import frc.robot.RobotConfig.Motors;

public class LauncherConfig extends RollerMechConfig {
    public static final String name = "Launcher";
    public final int diameterInches = 4;

    public static final int launcherMotorID = Motors.launcherMotor;

    public static final double kP = 1; // 2 goes too slowly then too fast,
    public static final double kI = 0.0;
    public static final double kD = 0;
    public static final double kF = 0.1079; // not used
    public static final double kIz = 0.0;

    public LauncherConfig() {
        super(name);
    }
}

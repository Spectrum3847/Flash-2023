package frc.robot.launcher;

import frc.SpectrumLib.subsystems.rollerMech.RollerMechConfig;

public class LauncherConfig extends RollerMechConfig {
    public static final String name = "Launcher";
    public final int diameterInches = 4;

    public LauncherConfig() {
        super(name);
    }
}

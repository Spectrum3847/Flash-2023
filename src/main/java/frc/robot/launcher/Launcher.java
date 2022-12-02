package frc.robot.launcher;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.SpectrumLib.subsystems.rollerMech.RollerMechSubsystem;
import frc.robot.RobotConfig;

public class Launcher extends RollerMechSubsystem {
    public static LauncherConfig config = new LauncherConfig();

    public Launcher() {
        super(config);
        motorLeader = new WPI_TalonFX(RobotConfig.Motors.launcherMotor);
        setupFalconLeader();
    }
}

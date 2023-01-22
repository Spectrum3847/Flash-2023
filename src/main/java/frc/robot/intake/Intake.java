package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.SpectrumLib.subsystems.rollerMech.RollerMechSubsystem;
import frc.robot.RobotConfig;

public class Intake extends RollerMechSubsystem {
    public static IntakeConfig config = new IntakeConfig();

    public Intake() {
        super(config);
        motorLeader = new WPI_TalonFX(RobotConfig.Motors.intakeMotor);
        setupFalconLeader();
    }
}

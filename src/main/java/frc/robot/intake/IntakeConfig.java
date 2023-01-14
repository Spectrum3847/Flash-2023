package frc.robot.intake;

import frc.SpectrumLib.subsystems.rollerMech.RollerMechConfig;
import frc.robot.RobotConfig.Motors;

public class IntakeConfig extends RollerMechConfig {
    public static final String name = "Intake";
    public final int diameterInches = 4;

    public static final int intakeMotorID = Motors.intakeMotor;

    public double kP = 0.0;
    public double kI = 0.0;
    public double kD = 0.0;
    public double kF = 0.0;
    public double kIz = 0.0;

    public IntakeConfig() {
        super(name);
    }
}

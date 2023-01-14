package frc.robot.elevator;

import frc.SpectrumLib.subsystems.linearMech.LinearMechConfig;
import frc.robot.RobotConfig.Motors;

public class ElevatorConfig extends LinearMechConfig {
    public static final String name = "Elevator";
    public final int diameterInches = 4;

    public static final int elevatorMotorID = Motors.elevatorMotor;

    public double kP = 0.0;
    public double kI = 0.0;
    public double kD = 0.0;
    public double kF = 0.0;
    public double kIz = 0.0;

    public ElevatorConfig() {
        super(name);
    }
}

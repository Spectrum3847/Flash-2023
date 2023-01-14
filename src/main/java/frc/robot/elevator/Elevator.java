package frc.robot.elevator;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.SpectrumLib.subsystems.linearMech.LinearMechSubsystem;
import frc.robot.RobotConfig;

public class Elevator extends LinearMechSubsystem {
    public static ElevatorConfig config = new ElevatorConfig();

    public Elevator() {
        super(config);
        motorLeader = new WPI_TalonFX(RobotConfig.Motors.elevatorMotor);
        setupFalconLeader();
    }
}

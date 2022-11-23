package frc.robot.test;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.SpectrumLib.util.Conversions;
import frc.SpectrumLib.util.Util;
import frc.robot.RobotTelemetry;

public class CheckMotorVelocity extends CommandBase {
    private WPI_TalonFX motor;
    private double velocity;
    private double targetVelocity;
    private String name;
    private double gearRatio;

    public CheckMotorVelocity(
            String motorName, WPI_TalonFX motor, double targetVelocity, double gearRatio) {
        this.motor = motor;
        this.targetVelocity = targetVelocity;
        this.name = motorName;
        this.gearRatio = gearRatio;
    }

    @Override
    public void initialize() {
        velocity = motor.getSelectedSensorVelocity();
        targetVelocity = Conversions.RPMToFalcon(velocity, gearRatio);
        RobotTelemetry.print(
                name
                        + " current is in range: "
                        + Util.closeTo(velocity, targetVelocity, velocity * 0.02));
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

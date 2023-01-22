package frc.robot.Intake;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    int lowerRoller = 42;
    int upperRoller = 43;
    int launcher = 44;
    VictorSPX lowerRollerMotor;
    VictorSPX upperRollerMotor;
    VictorSPX launcherMotor;

    public Intake(){
        super();
        lowerRollerMotor = new VictorSPX(lowerRoller);
        upperRollerMotor = new VictorSPX(upperRoller);
        launcherMotor = new VictorSPX(launcher);
    }

    public void setLowerRoller(double speed){
        lowerRollerMotor.set(VictorSPXControlMode.PercentOutput, speed);
    }

    public void setUpperRoller(double speed){
        upperRollerMotor.set(VictorSPXControlMode.PercentOutput, speed);
    }

    public void setLauncher(double speed){
        launcherMotor.set(VictorSPXControlMode.PercentOutput, speed);
    }

    public void stopLowerRoller(){
        lowerRollerMotor.set(VictorSPXControlMode.PercentOutput, 0);
    }

    public void stopUpperRoller(){
        upperRollerMotor.set(VictorSPXControlMode.PercentOutput, 0);
    }

    public void stopLauncher(){
        launcherMotor.set(VictorSPXControlMode.PercentOutput, 0);
    }

    public void stopAll(){
        stopLowerRoller();
        stopUpperRoller();
        stopLauncher();
    }
    
}

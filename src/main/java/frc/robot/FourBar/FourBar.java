package frc.robot.FourBar;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;
import frc.SpectrumLib.subsystems.angleMech.AngleMechSubsystem;

public class FourBar extends AngleMechSubsystem{
    public static AngleMechConfig config = new FourBarConfig();
    TalonFX motorLeader;

    public FourBar() {
        super(config);
        motorLeader = new TalonFX(40);
        setupFalconLeader();
    }

    
    
}

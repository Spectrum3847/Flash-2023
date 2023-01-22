package frc.robot.FourBar;

import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class FourBarConfig extends AngleMechConfig{
    public final int minAngle = 0;
    public final int maxAngle = 90;

    // Physical Constants
    public final double pulleyDiameterInches = 2;
    public final double pulleyDiameterMeters = pulleyDiameterInches * 0.0254;

    public final double gearRatio = 1;

    public final double pulleyCircumferenceMeters = pulleyDiameterMeters * Math.PI;
    public final double pulleyCircumferenceInches = pulleyDiameterInches * Math.PI;

    public FourBarConfig(){
        super("FourBar");
        updateTalonFXConfig();
    }

}

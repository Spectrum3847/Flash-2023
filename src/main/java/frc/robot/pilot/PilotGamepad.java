package frc.robot.pilot;

import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.mapping.ExpCurve;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    public static ExpCurve throttleCurve =
            new ExpCurve(
                    PilotConstants.throttleExp,
                    0,
                    PilotConstants.throttleScaler,
                    PilotConstants.throttleDeadband);
    public static ExpCurve steeringCurve =
            new ExpCurve(
                    PilotConstants.steeringExp,
                    0,
                    PilotConstants.steeringScaler,
                    PilotConstants.steeringDeadband);

    public PilotGamepad() {
        super("PILOT", PilotConstants.port);
    }

    public void setupTeleopButtons() {}

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

    /* Transforming raw input to exponentially curved */
    public double getDriveY() {
        return throttleCurve.calculateMappedVal(this.gamepad.leftStick.getY());
    }

    public double getDriveX() {
        return throttleCurve.calculateMappedVal(this.gamepad.leftStick.getX());
    }

    public double getDriveR() {
        return steeringCurve.calculateMappedVal(this.gamepad.triggers.getTwist());
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}

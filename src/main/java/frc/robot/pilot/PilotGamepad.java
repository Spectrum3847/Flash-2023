package frc.robot.pilot;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.mapping.ExpCurve;
import frc.robot.trajectories.TrajectoriesCommands;

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

    public void setupTeleopButtons() {
        gamepad.aButton.whileTrue(TrajectoriesCommands.snapPilotDrive(90));
        gamepad.bButton.whileTrue(new PrintCommand("Test"));
    }

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

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

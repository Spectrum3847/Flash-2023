package frc.robot.pilot;

import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.mapping.ExpCurve;
import frc.robot.swerve.commands.LockSwerve;

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
        gamepad.aButton.whileTrue(PilotCommands.aimPilotDrive(90).withName("Snap 90"));
        gamepad.bButton.and(gamepad.yButton).whileTrue(PilotCommands.fpvPilotSwerve());
        gamepad.xButton.whileTrue(new LockSwerve());
    }

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

    public double getDriveY() {
        return throttleCurve.calculateMappedVal(this.gamepad.leftStick.getY())
                * (PilotConstants.yInvert ? -1 : 1);
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

package frc.robot.pilot;

import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.mapping.ExpCurve;
import frc.robot.swerve.commands.LockSwerve;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    public static ExpCurve throttleCurve =
            new ExpCurve(
                    PilotConfig.throttleExp,
                    0,
                    PilotConfig.throttleScaler,
                    PilotConfig.throttleDeadband);
    public static ExpCurve steeringCurve =
            new ExpCurve(
                    PilotConfig.steeringExp,
                    0,
                    PilotConfig.steeringScaler,
                    PilotConfig.steeringDeadband);

    public PilotGamepad() {
        super("PILOT", PilotConfig.port);
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
                * (PilotConfig.yInvert ? -1 : 1);
    }

    public double getDriveX() {
        return throttleCurve.calculateMappedVal(this.gamepad.leftStick.getX())
                * (PilotConfig.xInvert ? -1 : 1);
    }

    public double getDriveR() {
        return steeringCurve.calculateMappedVal(this.gamepad.triggers.getTwist())
                * (PilotConfig.steeringInvert ? -1 : 1);
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}

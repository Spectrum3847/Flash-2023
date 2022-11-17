package frc.robot.pilot;

import edu.wpi.first.math.util.Units;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.mapping.ExpCurve;
import frc.robot.pilot.commands.DodgeDrive;
import frc.robot.pilot.commands.PilotCommands;
import frc.robot.swerve.commands.LockSwerve;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    public static ExpCurve steeringCurve =
            new ExpCurve(
                    PilotConfig.steeringExp,
                    0,
                    PilotConfig.steeringScaler,
                    PilotConfig.steeringDeadband);

    public PilotGamepad() {
        super("PILOT", PilotConfig.port);
        gamepad.leftStick.setDeadband(PilotConfig.throttleDeadband);
        gamepad.leftStick.configXCurve(PilotConfig.throttleExp, PilotConfig.xScaler);
        gamepad.leftStick.configYCurve(PilotConfig.throttleExp, PilotConfig.yScaler);
    }

    public void setupTeleopButtons() {
        gamepad.aButton.whileTrue(
                PilotCommands.aimPilotDrive(Units.degreesToRadians(90)).withName("Snap 90"));
        gamepad.bButton.whileTrue(PilotCommands.fpvPilotSwerve());
        gamepad.xButton.whileTrue(new LockSwerve());
        gamepad.yButton.whileTrue(new DodgeDrive());
    }

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

    public double getDriveX() {
        double x = gamepad.leftStick.getX();
        // Robot.log.logger.recordOutput("Pilot/X", x);
        return x;
    }

    public double getDriveY() {
        double y = gamepad.leftStick.getY();
        // Robot.log.logger.recordOutput("Pilot/Y", y);
        return y;
    }

    // Positive is counter-clockwise, left Trigger is positive
    public double getDriveR() {
        double r =
                steeringCurve.calculateMappedVal(gamepad.triggers.getTwist())
                        * PilotConfig.steeringScaler;

        return r;
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}

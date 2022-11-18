package frc.robot.pilot;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.SpectrumLib.gamepads.AxisButton;
import frc.SpectrumLib.gamepads.AxisButton.ThresholdType;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.XboxGamepad.XboxAxis;
import frc.robot.pilot.commands.PilotCommands;
import frc.robot.swerve.commands.LockSwerve;
import frc.robot.swerve.commands.SwerveCommands;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {

    public PilotGamepad() {
        super("PILOT", PilotConfig.port);
        gamepad.leftStick.setDeadband(PilotConfig.throttleDeadband);
        gamepad.leftStick.configCurves(PilotConfig.throttleExp, PilotConfig.throttleScaler);
        gamepad.leftStick.setXinvert(PilotConfig.xInvert);
        gamepad.leftStick.setYinvert(PilotConfig.yInvert);

        gamepad.triggers.setTwistDeadband(PilotConfig.steeringDeadband);
        gamepad.triggers.configTwistCurve(PilotConfig.steeringExp, PilotConfig.steeringScaler);
        gamepad.triggers.setTwistInvert(PilotConfig.steeringInvert);
    }

    public void setupTeleopButtons() {
        gamepad.aButton.whileTrue(
                PilotCommands.aimPilotDrive(Math.PI * -1 / 2).withName("Snap 90"));
        gamepad.bButton.whileTrue(PilotCommands.fpvPilotSwerve());
        gamepad.xButton.whileTrue(new LockSwerve());
        gamepad.yButton.whileTrue(PilotCommands.snakeDrive());

        // Right Stick points the robot in that direction
        Trigger rightX =
                new AxisButton(gamepad, XboxAxis.RIGHT_X, 0.5, ThresholdType.DEADBAND).trigger();
        Trigger rightY =
                new AxisButton(gamepad, XboxAxis.RIGHT_Y, 0.5, ThresholdType.DEADBAND).trigger();
        rightX.or(rightY).whileTrue(PilotCommands.stickSteer());

        gamepad.rightBumper.whileTrue(PilotCommands.stickSteer());

        gamepad.Dpad.Up.whileTrue(SwerveCommands.setHeadingDeg(0));
        gamepad.Dpad.Left.whileTrue(SwerveCommands.setHeadingDeg(90));
        gamepad.Dpad.Down.whileTrue(SwerveCommands.setHeadingDeg(180));
        gamepad.Dpad.Right.whileTrue(SwerveCommands.setHeadingDeg(270));
    }

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

    public double getDriveX() {
        double x = gamepad.leftStick.getX();
        return x;
    }

    public double getDriveY() {
        double y = gamepad.leftStick.getY();
        return y;
    }

    // Positive is counter-clockwise, left Trigger is positive
    public double getDriveR() {
        double r = gamepad.triggers.getTwist();
        return r;
    }

    // Return the angle created by the left stick in radians, 0 is up, 90 is left
    public Double getDriveAngle() {
        return gamepad.leftStick.getDirectionRadians();
    }

    // Return the angle created by the right stick in radians, 0 is up, 90 is left
    public double getRightStickAngle() {
        return gamepad.rightStick.getDirectionRadians();
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}

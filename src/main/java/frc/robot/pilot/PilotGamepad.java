package frc.robot.pilot;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.SpectrumLib.gamepads.AxisButton;
import frc.SpectrumLib.gamepads.AxisButton.ThresholdType;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.XboxGamepad.XboxAxis;
import frc.robot.leds.commands.BlinkLEDCommand;
import frc.robot.leds.commands.OneColorLEDCommand;
import frc.robot.leds.commands.RainbowLEDCommand;
import frc.robot.leds.commands.SnowfallLEDCommand;
import frc.robot.pilot.commands.PilotCommands;
import frc.robot.pose.commands.PoseCommands;
import frc.robot.swerve.commands.SwerveCommands;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    public PilotConfig config;

    public PilotGamepad() {
        super("PILOT", PilotConfig.port);
        config = new PilotConfig();
        gamepad.leftStick.setDeadband(config.throttleDeadband);
        gamepad.leftStick.configCurves(config.throttleExp, config.throttleScaler);
        gamepad.leftStick.setXinvert(config.xInvert);
        gamepad.leftStick.setYinvert(config.yInvert);

        gamepad.rightStick.setDeadband(config.throttleDeadband);
        gamepad.rightStick.configCurves(config.steeringExp, config.steeringScaler);
        gamepad.rightStick.setXinvert(config.xInvert);
        gamepad.rightStick.setYinvert(config.yInvert);

        gamepad.triggers.setTwistDeadband(config.steeringDeadband);
        gamepad.triggers.configTwistCurve(config.steeringExp, config.steeringScaler);
        gamepad.triggers.setTwistInvert(config.steeringInvert);
    }

    public void setupTeleopButtons() {
        gamepad.aButton.whileTrue(
                PilotCommands.aimPilotDrive(Math.PI * -1 / 2).withName("Snap 90"));
        gamepad.bButton.whileTrue(PilotCommands.fpvPilotSwerve());
        gamepad.xButton.whileTrue(SwerveCommands.lockSwerve());
        gamepad.yButton.whileTrue(PilotCommands.snakeDrive());

        // Right Stick points the robot in that direction
        Trigger rightX = AxisButton.create(gamepad, XboxAxis.RIGHT_X, 0.5, ThresholdType.DEADBAND);
        Trigger rightY = AxisButton.create(gamepad, XboxAxis.RIGHT_Y, 0.5, ThresholdType.DEADBAND);
        rightX.or(rightY).whileTrue(PilotCommands.stickSteer());

        // Reorient the robot to the current heading
        gamepad.Dpad.Up.whileTrue(
                PoseCommands.resetHeading(0).alongWith(PilotCommands.rumble(0.5, 1)));
        gamepad.Dpad.Left.whileTrue(
                PoseCommands.resetHeading(90).alongWith(PilotCommands.rumble(0.5, 1)));
        gamepad.Dpad.Down.whileTrue(
                PoseCommands.resetHeading(180).alongWith(PilotCommands.rumble(0.5, 1)));
        gamepad.Dpad.Right.whileTrue(
                PoseCommands.resetHeading(270).alongWith(PilotCommands.rumble(0.5, 1)));
    }

    public void setupDisabledButtons() {
        gamepad.aButton.whileTrue(new OneColorLEDCommand(Color.kGreen, "Green", 5, 3));
        gamepad.bButton.whileTrue(new BlinkLEDCommand(Color.kPink, "Blink Pink", 10, 3));
        gamepad.xButton.whileTrue(new RainbowLEDCommand("rainbow", 15, 3));
        gamepad.yButton.whileTrue(new SnowfallLEDCommand("Snowfall", 20, 3));
    }

    public void setupTestButtons() {}

    public double getDriveFwdPositive() {
        double fwdPositive = gamepad.leftStick.getY();
        return fwdPositive;
    }

    public double getDriveLeftPositive() {
        double leftPositive = gamepad.leftStick.getX();
        return leftPositive;
    }

    // Positive is counter-clockwise, left Trigger is positive
    public double getDriveCCWPositive() {
        double ccwPositive = gamepad.triggers.getTwist();
        return ccwPositive;
    }

    // Return the angle created by the left stick in radians, 0 is up, pi/2 is left
    public Double getDriveAngle() {
        return gamepad.leftStick.getDirectionRadians(getDriveFwdPositive(), getDriveLeftPositive());
    }

    // Return the angle created by the right stick in radians, 0 is up, pi/2 is left
    public double getRightStickAngle() {
        return gamepad.rightStick.getDirectionRadians(
                gamepad.rightStick.getY(), gamepad.rightStick.getX());
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}

package frc.robot.pilot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.mapping.ExpCurve;
import frc.robot.pilot.commands.PilotCommands;
import frc.robot.swerve.commands.LockSwerve;

/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    private double lastDriveAngle = 0;
    private double lastRightStickAngle = 0;
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
                PilotCommands.aimPilotDrive(Math.PI * -1 / 2).withName("Snap 90"));
        gamepad.bButton.whileTrue(PilotCommands.fpvPilotSwerve());
        gamepad.xButton.whileTrue(new LockSwerve());
        gamepad.yButton.whileTrue(PilotCommands.snakeDrive());
        gamepad.rightBumper.whileTrue(PilotCommands.stickSteer());
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
        double r = steeringCurve.calculateMappedVal(gamepad.triggers.getTwist());

        return r;
    }

    public Double getDriveAngle() {
        double x = getDriveX() * -1; // Xis flipped to get direction correctly
        double y = getDriveY();
        if (x != 0 || y != 0) {
            Translation2d translation = new Translation2d(x, y);
            // Minus 90 to switch Y axis to be 0 degrees
            double angle = translation.getAngle().getDegrees() - 90;
            lastDriveAngle = angle;
        }
        return Units.degreesToRadians(lastDriveAngle);
    }

    public double getRightStickAngle() {
        double x = gamepad.rightStick.getX();
        double y = gamepad.rightStick.getY() * -1;

        if (Math.abs(x) <= 0.5 || Math.abs(y) <= 0.5) {
            Translation2d translation = new Translation2d(x, y);
            double angle = translation.getAngle().getDegrees() - 90;
            lastRightStickAngle = angle;
        }
        return Units.degreesToRadians(lastRightStickAngle);
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}

// Created by Spectrum3847
// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;

public class PilotSwerve extends CommandBase {

    private double rotation;
    private Translation2d translation;
    private boolean fieldRelative;
    private boolean openLoop;

    private Swerve s_Swerve;

    /**
     * Creates a PilotSwerve Command to allow the pilot to control the swerve drive
     *
     * @param s_Swerve
     * @param fieldRelative
     * @param openLoop
     */
    public PilotSwerve(boolean fieldRelative, boolean openLoop) {
        this.s_Swerve = Robot.swerve;
        addRequirements(s_Swerve);
        this.fieldRelative = fieldRelative;
        this.openLoop = openLoop;
    }

    @Override
    public void execute() {
        if (Robot.pilotGamepad.configured) {
            // Get the inputs from the pilot gamepad
            double yAxis = Robot.pilotGamepad.getDriveY();
            double xAxis = Robot.pilotGamepad.getDriveX();
            double rAxis = Robot.pilotGamepad.getDriveR();

            // Multiply by max speed to scale the pilot inputs to velocity in m/s
            translation = new Translation2d(yAxis, xAxis).times(SwerveConfig.maxSpeed);

            // Multiply by max angular velocity to scale the pilot inputs to angular velocity in
            // rad/s
            rotation = rAxis * SwerveConfig.maxAngularVelocity;

            s_Swerve.drive(translation, rotation, fieldRelative, openLoop);
        } else {
            s_Swerve.stop();
        }
    }
}

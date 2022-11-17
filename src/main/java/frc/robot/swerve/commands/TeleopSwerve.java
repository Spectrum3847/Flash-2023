// Created by Spectrum3847
// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;

public class TeleopSwerve extends CommandBase {

    private double rotation;
    private Translation2d translation;
    private boolean fieldRelative;
    private boolean openLoop;

    private Swerve s_Swerve;

    /** Driver control */
    public TeleopSwerve(Swerve s_Swerve, boolean fieldRelative, boolean openLoop) {
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);
        this.fieldRelative = fieldRelative;
        this.openLoop = openLoop;
    }

    @Override
    public void execute() {
        if (Robot.pilotGamepad.configured) {
            double yAxis = Robot.pilotGamepad.getDriveY();
            double xAxis = Robot.pilotGamepad.getDriveX();
            double rAxis = Robot.pilotGamepad.getDriveR();

            translation = new Translation2d(yAxis, xAxis); // .times(SwerveConfig.maxSpeed);
            rotation = rAxis; // * SwerveConfig.maxAngularVelocity;
            s_Swerve.drive(translation, rotation, fieldRelative, openLoop);
        } else {
            s_Swerve.stop();
        }
    }
}

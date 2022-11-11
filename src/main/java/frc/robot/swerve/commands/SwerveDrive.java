// Created by Spectrum3847
// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;

public class SwerveDrive extends CommandBase {

    private double rotation;
    private Translation2d translation;
    private boolean fieldRelative;
    private boolean openLoop;

    private Swerve s_Swerve;
    private double m_x;
    private double m_y;

    /**
     * Creates a SwerveDrive command that allows for simple x and y translation of the robot. This
     * can be used for simiple autonomous driving like drive forward 2 secs when used with a
     * timeout.
     *
     * @param fieldRelative
     * @param y
     * @param x
     */
    public SwerveDrive(boolean fieldRelative, double y, double x) {
        this.s_Swerve = Robot.swerve;
        addRequirements(s_Swerve);
        this.fieldRelative = fieldRelative;
        this.openLoop = false;
        m_y = y;
        m_x = x;
    }

    public void intialize() {
        s_Swerve.brakeMode(true);
    }

    @Override
    public void execute() {
        double yAxis = m_y;
        double xAxis = m_x;
        double rAxis = 0;

        translation = new Translation2d(yAxis, xAxis).times(SwerveConfig.maxSpeed);
        rotation = rAxis * SwerveConfig.maxAngularVelocity;
        s_Swerve.drive(translation, rotation, fieldRelative, openLoop);
    }

    public void end(boolean interrupted) {
        super.end(interrupted);
        s_Swerve.brakeMode(false);
        s_Swerve.stop();
    }
}

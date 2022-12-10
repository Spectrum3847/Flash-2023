// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class SetModulesToAngle extends CommandBase {
    SwerveModuleState[] swerveModuleStates;
    SwerveModuleState[] stopModuleStates;

    /**
     * Set the swerve modules to the spcified angles in SwerveModuleState array
     *
     * @param states
     */
    public SetModulesToAngle(SwerveModuleState[] states) {
        this(
                states[0].angle.getDegrees(),
                states[1].angle.getDegrees(),
                states[2].angle.getDegrees(),
                states[3].angle.getDegrees());
    }

    /**
     * Set the swerve modules to the spcified angle
     *
     * @param angle
     */
    public SetModulesToAngle(double angle) {
        this(angle, angle, angle, angle);
    }

    /**
     * Set the swerve modules to the spcified angles
     *
     * @param angle0
     * @param angle1
     * @param angle2
     * @param angle3
     */
    public SetModulesToAngle(double angle0, double angle1, double angle2, double angle3) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);

        double minSpeed = 0.01 * Robot.swerve.config.tuning.maxVelocity;

        // Set the angles and minimum speeds to use when locking the swerve base
        swerveModuleStates =
                new SwerveModuleState[] {
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle0)),
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle1)),
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle2)),
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle3))
                };

        stopModuleStates =
                new SwerveModuleState[] {
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle0)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle1)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle2)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle3))
                };
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.swerve.setModuleStates(swerveModuleStates);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.swerve.setModuleStates(stopModuleStates);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}

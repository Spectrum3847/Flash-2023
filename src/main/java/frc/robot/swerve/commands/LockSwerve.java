// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class LockSwerve extends CommandBase {
    SwerveModuleState[] swerveModuleStates;

    /** Creates a new LockSwerve. */
    public LockSwerve() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);

        // Set the angles and minimum speeds to use when locking the swerve base
        swerveModuleStates =
                new SwerveModuleState[] {
                    new SwerveModuleState(0.1, Rotation2d.fromDegrees(45)),
                    new SwerveModuleState(0.1, Rotation2d.fromDegrees(315)),
                    new SwerveModuleState(0.1, Rotation2d.fromDegrees(135)),
                    new SwerveModuleState(0.1, Rotation2d.fromDegrees(225))
                };
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.swerve.setModuleStates(swerveModuleStates);
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

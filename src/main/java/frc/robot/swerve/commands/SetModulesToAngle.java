package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.SwerveConfig;

public class SetModulesToAngle extends CommandBase {
    SwerveModuleState[] swerveModuleStates;
    SwerveModuleState[] stopModuleStates;

    public SetModulesToAngle(double angle) {
        this(angle, angle, angle, angle);
    }

    /** Creates a new LockSwerve. */
    public SetModulesToAngle(double angle1, double angle2, double angle3, double angle4) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);

        double minSpeed = 0.01 * SwerveConfig.maxVelocity;

        // Set the angles and minimum speeds to use when locking the swerve base
        swerveModuleStates =
                new SwerveModuleState[] {
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle1)),
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle2)),
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle3)),
                    new SwerveModuleState(minSpeed, Rotation2d.fromDegrees(angle4))
                };

        stopModuleStates =
                new SwerveModuleState[] {
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle1)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle2)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle3)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(angle4))
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

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.trajectories.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.auton.commands.*;
import frc.robot.swerve.*;
import frc.robot.swerve.SwerveConfig;
import java.util.HashMap;

public class FollowTrajectory extends PPSwerveControllerCommand {

    /** Creates a new FollowTrajectory. */
    public FollowTrajectory(PathPlannerTrajectory trajectory) {
        this(trajectory, new HashMap<>());

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    /** Creates a new FollowTrajectory. */
    public FollowTrajectory(PathPlannerTrajectory trajectory, HashMap<String, Command> eventMap) {
        super(
                trajectory,
                Robot.pose::getPosition,
                SwerveConfig.swerveKinematics,
                Robot.trajectories.xController,
                Robot.trajectories.yController,
                Robot.trajectories.thetaController,
                Robot.swerve::setModuleStates,
                eventMap,
                Robot.swerve);

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        super.initialize();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        super.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}

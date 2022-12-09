package frc.robot.auton.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auton.Auton;
import frc.robot.auton.AutonConfig;
import frc.robot.trajectories.commands.FollowTrajectory;

public class FollowPath extends SequentialCommandGroup {

    /** Creates a new Drive1Meter. */
    public FollowPath(String path, Boolean isFirstPath) {
        // An example trajectory to follow. All units in meters.
        PathPlannerTrajectory traj =
                PathPlanner.loadPath(path, AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel);

        addCommands(
                AutonCommands.checkFirstPath(traj, isFirstPath),
                new FollowPathWithEvents(
                        new FollowTrajectory(traj), traj.getMarkers(), Auton.eventMap));
    }
}

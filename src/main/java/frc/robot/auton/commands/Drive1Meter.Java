package frc.robot.auton.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auton.AutonConstants;
import frc.robot.trajectories.commands.FollowTrajectory;

public class Drive1Meter extends SequentialCommandGroup {
    /** Creates a new Drive1Meter. */
    public Drive1Meter() {
        // An example trajectory to follow. All units in meters.
        PathPlannerTrajectory DriveOneMeterFwd =
                PathPlanner.loadPath("1 Meter", AutonConstants.kMaxSpeed, AutonConstants.kMaxAccel);

        addCommands(
                AutonCommands.intializePathFollowing(DriveOneMeterFwd),
                new FollowTrajectory(DriveOneMeterFwd));
    }
}

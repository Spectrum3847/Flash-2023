package frc.robot.auton.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auton.AutonConstants;
import frc.robot.trajectories.commands.FollowTrajectory;

public class TestPathPlannerCrazy extends SequentialCommandGroup {
    /** Creates a new Drive1Meter. */
    public TestPathPlannerCrazy() {
        // An example trajectory to follow. All units in meters.
        PathPlannerTrajectory testPath =
                PathPlanner.loadPath("Crazy", AutonConstants.kMaxSpeed, AutonConstants.kMaxAccel);

        addCommands(AutonCommands.intializePathFollowing(testPath), new FollowTrajectory(testPath));
    }
}

package frc.robot.auton.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auton.AutonConstants;
import frc.robot.trajectories.commands.FollowTrajectory;
import java.util.HashMap;

public class Drive1MeterWEventMapping extends SequentialCommandGroup {
    /** Creates a new Drive1Meter. */
    HashMap<String, Command> eventMap = new HashMap<>();

    public Drive1MeterWEventMapping() {

        eventMap.put("marker1", new PrintCommand("Passed marker 1"));
        eventMap.put("marker2", new PrintCommand("Passed marker 2"));

        // An example trajectory to follow. All units in meters.
        PathPlannerTrajectory drive1MeterWEventMapping =
                PathPlanner.loadPath("1 Meter", AutonConstants.kMaxSpeed, AutonConstants.kMaxAccel);

        addCommands(
                AutonCommands.intializePathFollowing(drive1MeterWEventMapping),
                new FollowTrajectory(drive1MeterWEventMapping));
    }
}

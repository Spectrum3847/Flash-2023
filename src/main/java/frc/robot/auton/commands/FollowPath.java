package frc.robot.auton.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.swerve.SwerveConfig;
import java.util.ArrayList;

public class FollowPath {

    // This will load the file "FullAuto.path" and generate it with a max velocity of 4 m/s and a
    // max acceleration of 3 m/s^2
    // for every path in the group
    ArrayList<PathPlannerTrajectory> pathGroup =
            (ArrayList<PathPlannerTrajectory>)
                    PathPlanner.loadPathGroup("1 Meter", new PathConstraints(4, 3));

    // Create the AutoBuilder. This only needs to be created once when robot code starts, not every
    // time you want to create an auto command. A good place to put this is in RobotContainer along
    // with your subsystems.
    public static final SwerveAutoBuilder autoBuilder =
            new SwerveAutoBuilder(
                    Robot.swerve.odometry::getPoseMeters, // Pose2d supplier
                    Robot.swerve.odometry
                            ::resetOdometry, // Pose2d consumer, used to reset odometry at the
                    // beginning of auto
                    SwerveConfig.swerveKinematics, // SwerveDriveKinematics
                    new PIDConstants(
                            5.0, 0.0,
                            0.0), // PID constants to correct for translation error (used to create
                    // the X and Y PID controllers)
                    new PIDConstants(
                            0.5, 0.0,
                            0.0), // PID constants to correct for rotation error (used to create the
                    // rotation controller)
                    Robot.swerve
                            ::setModuleStates, // Module states consumer used to output to the drive
                    // subsystem
                    Auton.eventMap,
                    true, // Should the path be automatically mirrored depending on alliance color.
                    // Optional, defaults to true
                    Robot.swerve // The drive subsystem. Used to properly set the requirements of
                    // path following commands
                    );

    public Command fullAuto = autoBuilder.fullAuto(pathGroup);

    /** Creates a new Drive1Meter. * */
    /*
    public FollowPath(String path, Boolean isFirstPath) {
        // An example trajectory to follow. All units in meters.
        PathPlannerTrajectory traj =
                PathPlanner.loadPath(path, AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel);

        addCommands(
                AutonCommands.checkFirstPath(traj, isFirstPath),
                new FollowPath(new FollowTrajectory(traj), traj.getMarkers(), Auton.eventMap));
    }

    public FollowPath(
            FollowTrajectory followTrajectory,
            List<EventMarker> markers,
            HashMap<String, Command> eventMap) {}*/
}

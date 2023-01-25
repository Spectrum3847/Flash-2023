package frc.robot.auton;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.swerve.SwerveConfig;
import java.util.HashMap;

public class Auton {
    public static final SendableChooser<Command> allianceColorChooser = new SendableChooser<>();
    public static final SendableChooser<Command> autonChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autonStart = 0;
    public static HashMap<String, Command> eventMap = new HashMap<>();
    public static SwerveAutoBuilder autoBuilder;

    public Auton() {
        setupSelectors();
        setupEventMap();
    }

    // A chooser for autonomous commands
    public static void setupSelectors() {
        allianceColorChooser.setDefaultOption("Blue Alliance", setAutoBuilder("Blue"));
        allianceColorChooser.addOption("Red Alliance", setAutoBuilder("Blue"));

        autonChooser.setDefaultOption(
                "Nothing", new PrintCommand("Doing Nothing in Auton").andThen(new WaitCommand(5)));
        // autonChooser.addOption("5 Ball w Balance", new FollowPath("5 Ball w Balance", true));
        // autonChooser.addOption("5 Ball", new FollowPath("5 Ball", false));
        autonChooser.addOption(
                "1 Meter",
                autoBuilder.fullAuto(
                        PathPlanner.loadPathGroup(
                                "1 Meter",
                                new PathConstraints(
                                        AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel))));
        autonChooser.addOption(
                "5 Ball",
                autoBuilder.fullAuto(
                        PathPlanner.loadPathGroup(
                                "5 Ball",
                                new PathConstraints(
                                        AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel))));
    }

    public static Command setAutoBuilder(String allianceColor) {
        if (allianceColor == "Blue") autoBuilder = autoBuilderBlue;
        else if (allianceColor == "Red") autoBuilder = autoBuilderRed;
        return null;
    }

    // Create the AutoBuilder. This only needs to be created once when robot code starts, not every
    // time you want to create an auto command. A good place to put this is in RobotContainer along
    // with your subsystems.
    public static final SwerveAutoBuilder autoBuilderRed =
            new SwerveAutoBuilder(
                    Robot.swerve.odometry::getPoseMeters, // Pose2d supplier
                    Robot.swerve.odometry
                            ::resetOdometry, // Pose2d consumer, used to reset odometry at the
                    // beginning of auto
                    SwerveConfig.swerveKinematics, // SwerveDriveKinematics
                    new PIDConstants(
                            AutonConfig.kPTranslationController,
                            AutonConfig.kITranslationController,
                            AutonConfig.kDTranslationController), // PID constants to correct for
                    // translation error (used to create
                    // the X and Y PID controllers)
                    new PIDConstants(
                            AutonConfig.kPRotationController,
                            AutonConfig.kIRotationController,
                            AutonConfig
                                    .kDRotationController), // PID constants to correct for rotation
                    // error (used to create the
                    // rotation controller)
                    Robot.swerve
                            ::setModuleStates, // Module states consumer used to output to the drive
                    // subsystem
                    Auton.eventMap,
                    true, // Should the path be automatically mirrored depending on
                    // alliance color
                    // Alliance.
                    Robot.swerve // The drive subsystem. Used to properly set the requirements of
                    // path following commands
                    );

    // Create the AutoBuilder. This only needs to be created once when robot code starts, not every
    // time you want to create an auto command. A good place to put this is in RobotContainer along
    // with your subsystems.
    public static final SwerveAutoBuilder autoBuilderBlue =
            new SwerveAutoBuilder(
                    Robot.swerve.odometry::getPoseMeters, // Pose2d supplier
                    Robot.swerve.odometry
                            ::resetOdometry, // Pose2d consumer, used to reset odometry at the
                    // beginning of auto
                    SwerveConfig.swerveKinematics, // SwerveDriveKinematics
                    new PIDConstants(
                            AutonConfig.kPTranslationController,
                            AutonConfig.kITranslationController,
                            AutonConfig.kDTranslationController), // PID constants to correct for
                    // translation error (used to create
                    // the X and Y PID controllers)
                    new PIDConstants(
                            AutonConfig.kPRotationController,
                            AutonConfig.kIRotationController,
                            AutonConfig
                                    .kDRotationController), // PID constants to correct for rotation
                    // error (used to create the
                    // rotation controller)
                    Robot.swerve
                            ::setModuleStates, // Module states consumer used to output to the drive
                    // subsystem
                    Auton.eventMap,
                    false, // Should the path be automatically mirrored depending on
                    // alliance color
                    // Alliance.
                    Robot.swerve // The drive subsystem. Used to properly set the requirements of
                    // path following commands
                    );

    // Adds event mapping to autonomous commands
    public static void setupEventMap() {
        eventMap.put("marker1", new PrintCommand("Passed marker 1"));
        eventMap.put("marker2", new PrintCommand("Passed marker 2"));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static Command getAutonomousCommand() {
        // return new CharacterizeLauncher(Robot.launcher);
        Command auton = autonChooser.getSelected();
        if (auton != null) {
            return auton;
        } else {
            return new PrintCommand("*** AUTON COMMAND IS NULL ***");
        }
    }

    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autonStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    /** Called in RobotPeriodic and displays the duration of the auton command Based on 6328 code */
    public static void printAutoDuration() {
        Command autoCommand = Auton.getAutonomousCommand();
        if (autoCommand != null) {
            if (!autoCommand.isScheduled() && !autoMessagePrinted) {
                if (DriverStation.isAutonomousEnabled()) {
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton finished in %.2f secs ***",
                                    Timer.getFPGATimestamp() - autonStart));
                } else {
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton CANCELLED in %.2f secs ***",
                                    Timer.getFPGATimestamp() - autonStart));
                }
                autoMessagePrinted = true;
            }
        }
    }
}

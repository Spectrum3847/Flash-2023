package frc.robot;

import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.SpectrumLib.sim.PhysicsSim;
import frc.SpectrumLib.util.Network;
import frc.robot.auton.Auton;
import frc.robot.leds.LEDs;
import frc.robot.pilot.PilotCommands;
import frc.robot.pilot.PilotGamepad;
import frc.robot.pose.Pose;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.trajectories.Trajectories;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedPowerDistribution;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    public static RobotConfig config;
    public static Swerve swerve;
    public static Pose pose;
    public static Trajectories trajectories;
    public static LEDs leds;
    public static PilotGamepad pilotGamepad;
    public static RobotTelemetry telemetry;

    public static String MAC = "";

    // Intialize subsystems and run their setupDefaultCommand methods here
    private void intializeSystems() {
        swerve = new Swerve();
        pose = new Pose();
        trajectories = new Trajectories();

        leds = new LEDs();
        pilotGamepad = new PilotGamepad();
        telemetry = new RobotTelemetry();

        // Set Default Commands, this method should exist for each subsystem that has
        // commands
        PilotCommands.setupDefaultCommand();
        SwerveCommands.setupDefaultCommand();
    }

    /**
     * Used in all robot mode intialize methods to cancel previous commands and reset button
     * bindings
     */
    public static void resetCommandsAndButtons() {
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        LiveWindow.disableAllTelemetry();

        // Reset Config for all gamepads and other button bindings
        pilotGamepad.resetConfig();
    }

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Set the MAC Address for this robot, useful for adjusting comp/practice bot
        // settings
        MAC = Network.getMACaddress();

        // Set up the config
        config = new RobotConfig();

        // Setup the logger
        setupAdvantageKit();

        // Initialize all systems, do this after getting the MAC address
        intializeSystems();
    }

    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Ensures that the main thread is the highest priority thread
        Threads.setCurrentThreadPriority(true, 99);
        /**
         * Runs the Scheduler. This is responsible for polling buttons, adding newly-scheduled
         * commands, running already-scheduled commands, removing finished or interrupted commands,
         * and running subsystem periodic() methods. This must be called from the robot's periodic
         * block in order for anything in the Command-based framework to work.
         */
        CommandScheduler.getInstance().run();

        Auton.printAutoDuration(); // Prints auton command duration if it finishes or cancelled

        Threads.setCurrentThreadPriority(true, 10); // Set the main thread back to normal priority
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        resetCommandsAndButtons();
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousInit() {
        resetCommandsAndButtons();

        Command autonCommand = Auton.getAutonomousCommand();
        if (autonCommand != null) {
            autonCommand.schedule();
            Auton.startAutonTimer();
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {
        resetCommandsAndButtons();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        resetCommandsAndButtons();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}

    /** This function is called once when a simulation starts */
    public void simulationInit() {}

    /** This function is called periodically during a simulation */
    public void simulationPeriodic() {
        PhysicsSim.getInstance().run();
    }

    /** Setups logging with advantageKit */
    private void setupAdvantageKit() {
        Logger logger = Logger.getInstance();

        // Record metadata
        logger.recordMetadata("MAC Address", MAC);
        logger.recordMetadata("RuntimeType", getRuntimeType().toString());
        logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
        logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
        switch (BuildConstants.DIRTY) {
            case 0:
                logger.recordMetadata("GitDirty", "All changes committed");
                break;
            case 1:
                logger.recordMetadata("GitDirty", "Uncomitted changes");
                break;
            default:
                logger.recordMetadata("GitDirty", "Unknown");
                break;
        }
        // Set up data receivers & replay source
        switch (config.getRobotType()) {
                // Running on a comp robot, log to a USB stick
            case COMP:
            case PRACTICE:
                logger.addDataReceiver(new WPILOGWriter("/media/sda1/"));
                logger.addDataReceiver(new NT4Publisher());
                LoggedPowerDistribution.getInstance(0, config.PowerDistributionType);
                break;

                // Running a physics simulator, log to local folder
            case SIM:
                logger.addDataReceiver(new WPILOGWriter(""));
                logger.addDataReceiver(new NT4Publisher());
                break;

                // Replaying a log, set up replay source
            case REPLAY:
                setUseTiming(false); // Run as fast as possible
                String logPath = LogFileUtil.findReplayLog();
                logger.setReplaySource(new WPILOGReader(logPath));
                logger.addDataReceiver(
                        new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
                break;
        }

        // Start AdvantageKit logger
        logger.start();
    }
}

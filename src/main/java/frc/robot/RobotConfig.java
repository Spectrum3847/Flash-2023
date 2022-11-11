package frc.robot;

import frc.robot.swerve.SwerveConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class RobotConfig {

    private RobotType robotType;
    public final String Canivore = "3847";
    public final Motors motors = new Motors();
    public final Pneumatic pneumatic = new Pneumatic();
    public final String praticeBotMAC = "00-80-2F-1C-1C-1C";

    public static final int pigeonID = 0;

    public final class Motors {
        public static final int driveMotor0 = 1;
        public static final int angleMotor0 = 2;
        public static final int driveMotor1 = 11;
        public static final int angleMotor1 = 12;
        public static final int driveMotor2 = 21;
        public static final int angleMotor2 = 22;
        public static final int driveMotor3 = 31;
        public static final int angleMotor3 = 32;
    }

    public final class Pneumatic {
        public final int ExamplePneumatic = 0;
    }

    public RobotConfig() {
        checkRobotType();
        switch (getRobotType()) {
            case COMP:
                // Set all the constants specifically for the competition robot
                SwerveConfig.Mod0.angleOffset = SwerveConfig.Mod0.angleOffsetC;
                SwerveConfig.Mod1.angleOffset = SwerveConfig.Mod1.angleOffsetC;
                SwerveConfig.Mod2.angleOffset = SwerveConfig.Mod2.angleOffsetC;
                SwerveConfig.Mod3.angleOffset = SwerveConfig.Mod3.angleOffsetC;
                break;
            case PRACTICE:
                // Set all the constants specifically for the practice robot
                SwerveConfig.Mod0.angleOffset = SwerveConfig.Mod0.angleOffsetP;
                SwerveConfig.Mod1.angleOffset = SwerveConfig.Mod1.angleOffsetP;
                SwerveConfig.Mod2.angleOffset = SwerveConfig.Mod2.angleOffsetP;
                SwerveConfig.Mod3.angleOffset = SwerveConfig.Mod3.angleOffsetP;
                break;
            case SIM:
                // Set all the constants specifically for the simulation
                break;
        }
    }

    /** Set the RobotType based on if simulation or the MAC address of the RIO */
    public void checkRobotType() {
        if (Robot.isSimulation()) {
            robotType = RobotType.SIM;
            System.out.println("Robot Type: Simulation");
        } else if (Robot.MAC.equals(praticeBotMAC)) {
            robotType = RobotType.PRACTICE;
            System.out.println("Robot Type: Practice");
        } else {
            robotType = RobotType.COMP;
            System.out.println("Robot Type: Competition");
        }
    }

    public RobotType getRobotType() {
        return robotType;
    }

    public enum RobotType {
        COMP,
        PRACTICE,
        SIM
    }
}

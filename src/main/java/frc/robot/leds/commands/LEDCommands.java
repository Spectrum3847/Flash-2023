package frc.robot.leds.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;

/** All of the commands to schedule LEDs */
public class LEDCommands {

    public static void setupDefaultCommand() {
        // Robot.leds.scheduler.setDefaultAnimation(
        //        "Default LEDs", new BlinkLEDCommand(Color.kPurple));
    }

    public static Command blink(Color color, String name, int priority, int timeout) {
        return new ScheduleAnimation(name, new BlinkLEDCommand(color), priority, timeout);
    }

    public static Command chase(Color color, String name, int priority, int timeout) {
        return new ScheduleAnimation(name, new ChaseLEDCommand(), priority, timeout);
    }

    public static Command rainbow(String name, int priority, int timeout) {
        return new ScheduleAnimation(name, new RainbowLEDCommand(20), priority, timeout);
    }

    public static Command solidColor(Color color, String name, int priority, int timeout) {
        return new ScheduleAnimation(name, new SetLEDToRGBCommand(color), priority, timeout);
    }

    public static Command snowfall(String name, int priority, int timeout) {
        return new ScheduleAnimation(name, new SnowfallLEDCommand(100), priority, timeout);
    }
}

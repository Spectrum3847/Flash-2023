package frc.robot.leds.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ScheduleAnimation extends CommandBase {
    String name;
    LEDCommandBase command;
    int priority;
    int timeout;

    /** Creates a new ScheduleAnimation. */
    public ScheduleAnimation(String name, LEDCommandBase command, int priority, int timeout) {
        this.name = name;
        this.command = command;
        this.priority = priority;
        this.timeout = timeout;
    }

    public boolean runsWhenDisabled() {
        return true;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.leds.scheduler.addAnimation(name, command, priority, timeout);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }
}

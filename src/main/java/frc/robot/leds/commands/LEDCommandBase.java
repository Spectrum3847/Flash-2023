package frc.robot.leds.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public abstract class LEDCommandBase extends CommandBase {

    public LEDCommandBase() {
        super();
        addRequirements(Robot.leds);
    }

    public LEDCommandBase(String name) {
        super();
        setName(name);
        addRequirements(Robot.leds);
    }

    public abstract void ledInitialize();

    public abstract void ledExecute();

    public boolean runsWhenDisabled() {
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}

package frc.robot.leds.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.robot.Robot;
import frc.robot.leds.LEDs;

public class SetLEDToRGBCommand extends LEDCommandBase {
    private final LEDs ledSubsystem;
    private final int r, g, b;

    public SetLEDToRGBCommand(int r, int g, int b) {
        ledSubsystem = Robot.leds;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public SetLEDToRGBCommand(Color color) {
        this(new Color8Bit(color).red, new Color8Bit(color).green, new Color8Bit(color).blue);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    @Override
    public void ledInitialize() {
        for (int i = 0; i < ledSubsystem.getBufferLength(); i++) {
            ledSubsystem.setRGB(i, r, g, b);
        }
        ledSubsystem.sendData();
    }

    @Override
    public void ledExecute() {}

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {}
}

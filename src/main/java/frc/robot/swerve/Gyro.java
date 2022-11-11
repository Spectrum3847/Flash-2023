package frc.robot.swerve;

import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.RobotConfig;

public class Gyro {
    public Pigeon2 pigeon;
    public Rotation2d yawOffset = new Rotation2d(0);

    public Gyro() {
        pigeon = new Pigeon2(RobotConfig.pigeonID);
        pigeon.configFactoryDefault();
        zeroGyro();
    }

    public void zeroGyro() {
        setGyroDegrees(0);
    }

    // Set gyro to a specific value
    public void setGyroDegrees(double value) {
        yawOffset = getRawYaw().minus(new Rotation2d(value));
    }

    public Rotation2d getYaw() {
        return getRawYaw().minus(yawOffset);
    }

    public Rotation2d getRawYaw() {
        double yaw = pigeon.getYaw();
        return Rotation2d.fromDegrees(yaw);
    }

    public double getDegrees() {
        return getYaw().getDegrees();
    }

    public double getRadians() {
        return getYaw().getRadians();
    }
}

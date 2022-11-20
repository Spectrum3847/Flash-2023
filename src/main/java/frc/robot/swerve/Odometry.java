package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;

public class Odometry {

    public SwerveDriveOdometry swerveOdometry;
    private Swerve swerve;

    public Odometry(Swerve s) {
        swerve = s;
        swerveOdometry =
                new SwerveDriveOdometry(
                        SwerveConfig.swerveKinematics, swerve.gyro.getYaw(), swerve.getPositions());
    }

    public SwerveDriveOdometry getSwerveDriveOdometry() {
        return swerveOdometry;
    }

    public void update() {
        swerveOdometry.update(swerve.gyro.getYaw(), swerve.getPositions());
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(pose, swerve.gyro.getYaw());
    }

    public Pose2d getPoseMeters() {
        return swerveOdometry.getPoseMeters();
    }

    public double getXDistance() {
        return getPoseMeters().getX();
    }

    public double getYDistance() {
        return getPoseMeters().getY();
    }

    public double getDistance() {
        return getPoseMeters().getTranslation().getNorm();
    }
}

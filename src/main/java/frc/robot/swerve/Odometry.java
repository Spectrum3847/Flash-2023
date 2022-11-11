package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Odometry {

    public SwerveDriveOdometry swerveOdometry;
    private Swerve swerve;
    private final Field2d m_field = new Field2d();

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
        m_field.setRobotPose(
                swerveOdometry.getPoseMeters()); // Field is used for simulation and testing
        SmartDashboard.putData("Field", m_field);
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(pose, swerve.gyro.getYaw());
    }

    public Pose2d getPose() {
        return swerveOdometry.getPoseMeters();
    }

    public double getXDistance() {
        return getPose().getX();
    }

    public double getYDistance() {
        return getPose().getY();
    }

    public double getDistance() {
        return getPose().getTranslation().getNorm();
    }
}

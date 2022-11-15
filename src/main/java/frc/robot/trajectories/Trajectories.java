// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.trajectories;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class Trajectories extends SubsystemBase {
    public ProfiledPIDController thetaController;
    public PIDController xController;
    public PIDController yController;

    /** Creates a new Trajectory. */
    public Trajectories() {
        thetaController =
                new ProfiledPIDController(
                        TrajectoriesConfig.kPThetaController,
                        0,
                        TrajectoriesConfig.kDThetaController,
                        TrajectoriesConfig.kThetaControllerConstraints);
        // Setup thetaController used for auton and automatic turns
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        xController =
                new PIDController(
                        TrajectoriesConfig.kPXController, 0, TrajectoriesConfig.kDXController);

        yController =
                new PIDController(
                        TrajectoriesConfig.kPYController, 0, TrajectoriesConfig.kDYController);
    }

    public void resetTheta() {
        thetaController.reset(Robot.swerve.gyro.getRadians());
    }

    public double calculteTheta(double goalAngle) {
        return thetaController.calculate(Robot.swerve.gyro.getRadians(), goalAngle);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}

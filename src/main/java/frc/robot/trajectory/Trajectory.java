// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.trajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Trajectory extends SubsystemBase {
    public ProfiledPIDController thetaController =
            new ProfiledPIDController(
                    TrajectoryConfig.kPThetaController,
                    0,
                    TrajectoryConfig.kDThetaController,
                    TrajectoryConfig.kThetaControllerConstraints);

    public PIDController xController =
            new PIDController(TrajectoryConfig.kPXController, 0, TrajectoryConfig.kDXController);

    public PIDController yController =
            new PIDController(TrajectoryConfig.kPYController, 0, TrajectoryConfig.kDYController);

    /** Creates a new Trajectory. */
    public Trajectory() {
        // Setup thetaController used for auton and automatic turns
        thetaController.enableContinuousInput(-Math.PI, Math.PI);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.trajectories;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Trajectories extends SubsystemBase {
    public ProfiledPIDController thetaController =
            new ProfiledPIDController(
                    TrajectoriesConfig.kPThetaController,
                    0,
                    TrajectoriesConfig.kDThetaController,
                    TrajectoriesConfig.kThetaControllerConstraints);

    public PIDController xController =
            new PIDController(
                    TrajectoriesConfig.kPXController, 0, TrajectoriesConfig.kDXController);

    public PIDController yController =
            new PIDController(
                    TrajectoriesConfig.kPYController, 0, TrajectoriesConfig.kDYController);

    /** Creates a new Trajectory. */
    public Trajectories() {
        // Setup thetaController used for auton and automatic turns
        thetaController.enableContinuousInput(-Math.PI, Math.PI);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}

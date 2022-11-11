# Swerve
Handles the configuration and main commands of the swerve drive. Any functions that directly relate to the basic operation of the swerve are included here.

PID configuration and commands for x, y, theta controllers and trajectory following are in the trajectory system.

1. Create a command to set the wheels into an X pattern and velcotity to zero, to make it hard to push our robot. (should be able to use setModuleStates() method for it.)
2. Write methods for acceleration limiting, may want to test slewratelimits on the pilot inputs first.
3. Create a command that changes the center of rotation when turning. Allow pilot to turn around a certain module, or corner of the robot, used for getting around defense.

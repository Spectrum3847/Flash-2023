# Trajectory

PID configuration and commands for x, y, & theta controllers and trajectory following commands.

1. PID controllers for x, y, & theta
2. Command to snap to a heading
3. Command to follow a trajectory
4. Command to generate a trajectory

Command to write to orient wheels before following first command.

"The other option is to create a command that will take in the trajectory you want to run, and orient the modules based on the first state. Here’s some example steps for that:

Get the heading(direction of travel) of the first state
Use this heading to get the X and Y velocity components of some velocity. Doesn’t really matter what you choose for the velocity since we will just ignore it. If you just use a velocity of 1 you can just ignore the velocity in the calculations altogether:
xVel = cos(heading)
yVel = sin(heading)
Use your kinematics to convert these field relative speeds into swerve module states
Set your module rotations to match the rotations of these states. Do no apply the speed component
Wait for some amount of time so the modules can finish rotating
Then you can run your path following command."
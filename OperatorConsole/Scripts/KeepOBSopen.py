import os
import subprocess
import time

# Set the path to the OBS Studio executable
obs_path = "C:\\Program Files\\obs-studio\\bin\\64bit\\obs64.exe"

# Set the path to the OBS Studio installation directory
obs_dir = "C:\\Program Files\\obs-studio\\bin\\64bit\\"

# Set the interval for checking if OBS is running (in seconds)
check_interval = 5

# Start OBS Studio in the default working directory
obs_process = subprocess.Popen([obs_path], cwd=obs_dir, shell=True)

# Check if OBS is still running at the specified interval
while True:
  if obs_process.poll() is not None:
    # If OBS is not running, restart it in the default working directory
    obs_process = subprocess.Popen([obs_path], cwd=obs_dir)
    print("OBS Studio restarted")
  time.sleep(check_interval)
import os
import subprocess
import time

# Get the current directory
current_dir = os.path.realpath(os.path.dirname(__file__))

# Get a list of all the Python scripts in the current directory
scripts = [f for f in os.listdir(current_dir) if f.endswith(".py") and f != "StartAllScripts.py"]

# Create a dictionary to hold the processes for each script
processes = {}

# Start all of the scripts
for script in scripts:
  processes[script] = subprocess.Popen(["python", os.path.realpath(os.path.dirname(__file__)) + "\\" + script])

# Monitor the scripts to ensure they are always running
while True:
  for script, process in processes.items():
    # Check if the process is still running
    if process.poll() is not None:
      # If the process is not running, restart it
      processes[script] = subprocess.Popen(["python", os.path.realpath(os.path.dirname(__file__)) + "\\" + script])
      
  # Sleep for 1 second before checking the processes again
  time.sleep(1)
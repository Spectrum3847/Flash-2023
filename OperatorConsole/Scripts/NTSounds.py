import os
import time
import threading
from networktables import NetworkTables
import pygame

# Get the current directory
current_dir = os.path.realpath(os.path.dirname(__file__))

# Set the IP address of the networktables server and the keys to monitor
server_ips = ["10.38.47.2", "127.0.0.1"]
keys_to_monitor = {
    "PlaySound1": current_dir + "sounds\\sound1.mp3",
    "PlaySound2": current_dir + "sounds\\sound2.mp3",
    "PlaySound3": current_dir + "sounds\\sound3.mp3"
}

# Connect to the networktables server
nt = NetworkTables.getTable("SmartDashboard")
NetworkTables.initialize(server_ips)

# Initialize the pygame mixer
pygame.mixer.init()

# Load the .mp3 files
sounds = {}
for key, value in keys_to_monitor.items():
    sounds[key] = pygame.mixer.Sound(value)

# Define a function to monitor the networktables values
def monitor_values():
    # Create a dictionary to store the current values of the networktables keys
    current_values = {}
    for key, value in keys_to_monitor.items():
        current_values[key] = nt.getBoolean(key, False)

    # Keep monitoring the values in a loop
    while True:
        # Create a dictionary to store the updated values of the networktables keys
        new_values = {}
        for key, value in keys_to_monitor.items():
            new_values[key] = nt.getBoolean(key, False)

        # Check each key to see if its value has changed from false to true
        for key, value in keys_to_monitor.items():
            # If the value has changed from false to true, play the corresponding sound file
            if current_values[key] != new_values[key]:
                # Update the current values
                current_values = new_values
                
                if new_values[key]:
                    sounds[key].play()
                    
                # Wait for a short time before checking the values again
                time.sleep(0.1)

# Start the monitoring thread
monitoring_thread = threading.Thread(target=monitor_values)
monitoring_thread.start()
import time
import threading
from networktables import NetworkTables
import keyboard

# Set the IP address of the networktables server and the keys to monitor
server_ips = ["10.38.47.2", "127.0.0.1"]
keys_to_monitor = {
    "RecordMatch": "F9",
    "StopRecording": "F10"
}

# Define a function to try to connect to the networktables server at each of the IP addresses
def try_connect():
    global nt
    print("Trying to connect to networktables server at " + server_ips[0] + ", " + server_ips[1])
    # Connect to the networktables server
    NetworkTables.startClient(server_ips)
    nt = NetworkTables.getTable("SmartDashboard")
    print("Connected to networktables server")

        
# Try to connect to the networktables server at each of the IP addresses
try_connect()

# Define a function to monitor the networktables values
def monitor_values():
    # Create a dictionary to store the current values of the networktables keys
    current_values = {}
    for key, value in keys_to_monitor.items():
        current_values[key] = nt.getBoolean(key, False)

    # Keep monitoring the values in a loop
    try:
        while True:
            # Create a dictionary to store the updated values of the networktables keys
            new_values = {}
            for key, value in keys_to_monitor.items():
                new_values[key] = nt.getBoolean(key, False)
            # Check each key to see if its value has changed from false to true
            for key, value in keys_to_monitor.items():
                # If the value has changed from false to true, send a keyboard press
                if current_values[key] != new_values[key]:
                    current_values[key] = new_values[key]
                    if new_values[key]:
                        keyboard.press(value)
                        time.sleep(0.1)
                        keyboard.release(value)

            # Wait for a short time before checking the values again
            time.sleep(0.1)
    except:
        try_connect()

# Start the monitoring thread
monitoring_thread = threading.Thread(target=monitor_values)
monitoring_thread.start()
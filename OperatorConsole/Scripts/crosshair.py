from networktables import NetworkTables
import pygame
import win32api
import win32con
import win32gui

# Initialize pygame and networktables
pygame.init()
NetworkTables.initialize(server="127.0.0.1")

# Set up screen
screen = pygame.display.set_mode((3840, 2160), pygame.HWSURFACE | pygame.DOUBLEBUF | pygame.FULLSCREEN)

# Get window handle
hwnd = pygame.display.get_wm_info()['window']

# Set window style to enable transparency
win32api.SetWindowLong(hwnd, win32con.GWL_EXSTYLE, win32api.GetWindowLong(hwnd, win32con.GWL_EXSTYLE) | win32con.WS_EX_LAYERED)

# Set window alpha value (transparency)
win32gui.SetLayeredWindowAttributes(hwnd, 0, 0, win32con.LWA_ALPHA)

# Connect to the network table with the given IP address
table = NetworkTables.getTable("SmartDashboard")

# Set up crosshair image
crosshair_img = pygame.image.load("crosshair.png")

# Main game loop
while True:
    # Get x and y values from network table
    x = table.getNumber("x", 0)
    y = table.getNumber("y", 0)
    
    # Clear screen
    screen.fill((0, 0, 0))
    
    # Draw crosshair at x, y position
    screen.blit(crosshair_img, (x, y))

    # Update screen
    pygame.display.flip()
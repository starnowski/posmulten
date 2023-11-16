#!/bin/bash

# Specify the path to the VNC password file
VNC_PASSWORD_FILE="$HOME/.vnc/passwd"

# Set your desired VNC password
VNC_PASSWORD="your_password_here"

# Create the directory for the VNC password file if it doesn't exist
mkdir -p "$(dirname "$VNC_PASSWORD_FILE")"

# Use vncpasswd to set the password
echo "$VNC_PASSWORD" | vncpasswd -f > "$VNC_PASSWORD_FILE"

# Make the VNC password file readable only by the user
chmod 600 "$VNC_PASSWORD_FILE"
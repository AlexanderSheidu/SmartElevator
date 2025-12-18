import os
import subprocess
import sys
import shutil
import webbrowser
import platform
import socket
import time

MIN_JAVA_VERSION = 17

# Check if command is in system path
def check_command_exists(command):
    return shutil.which(command) is not None

# Extracts version from a given java executable path
def get_java_version(path=None):
    try:
        if path:
            result = subprocess.run([path, "--version"], capture_output=True, text=True)
        else:
            result = subprocess.run(["java", "--version"], capture_output=True, text=True)

        output = result.stdout or result.stderr
        for line in output.splitlines():
            if "version" in line or "openjdk" in line.lower():
                version_part = line.split()[1].strip('"')
                major_version = int(version_part.split(".")[0])
                return major_version
    except Exception as e:
        print("Error checking Java version:", e)
    return None

# OS-specific install instructions
def install_instructions(tool):
    os_type = platform.system()
    if tool == "java":
        return {
            "Windows": "Install Java 17+ from https://adoptium.net/",
            "Darwin": "brew install openjdk@17",
            "Linux": "sudo apt install openjdk-17-jdk"
        }.get(os_type, "Refer to your OS documentation to install Java 17+")
    if tool == "maven":
        return {
            "Windows": "choco install maven",
            "Darwin": "brew install maven",
            "Linux": "sudo apt install maven"
        }.get(os_type, "Refer to your OS documentation to install Maven")
    if tool == "git":
        return {
            "Windows": "choco install git",
            "Darwin": "brew install git",
            "Linux": "sudo apt install git"
        }.get(os_type, "Refer to your OS documentation to install Git")

# Main dependency check
def ensure_dependencies():
    print("Checking dependencies...\n")

    if not check_command_exists("git"):
        print("Git is not installed.")
        print("Install Git:", install_instructions("git"))
        return False

    if not check_command_exists("mvn"):
        print("Maven is not installed.")
        print("Install Maven:", install_instructions("maven"))
        return False

    java_locations = []
    for root in ["C:\\Program Files", "C:\\Program Files (x86)"]:
        for dirpath, _, filenames in os.walk(root):
            if "java.exe" in filenames:
                java_locations.append(os.path.join(dirpath, "java.exe"))

    if not java_locations:
        print("Java not found.")
        print("Install Java:", install_instructions("java"))
        return False

    print("Found the following Java installations:")
    valid = False
    for java_path in java_locations:
        version = get_java_version(java_path)
        print(f"  {java_path} -> Version {version}")
        if version and version >= MIN_JAVA_VERSION:
            valid = True

    if not valid:
        print(f"\nJava version {MIN_JAVA_VERSION}+ required.")
        print("Install or switch to Java 17+:", install_instructions("java"))
        return False

    print("\nAll dependencies are installed and valid.\n")
    return True

# Wait until a port becomes active
def is_port_open(port, host='localhost'):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
        sock.settimeout(1)
        return sock.connect_ex((host, port)) == 0

def wait_for_port(port, timeout=60):
    print(f"Waiting for port {port} to be available...")
    start_time = time.time()
    while time.time() - start_time < timeout:
        if is_port_open(port):
            print(f"Port {port} is now accepting connections.")
            return True
        time.sleep(2)
    print(f"Timeout reached. Port {port} not available.")
    return False

# Launches backend from repo root
def start_backend():
    print("Starting backend...")

    pom_path = os.path.join("backend", "elevator-backend", "pom.xml")

    if not os.path.exists(pom_path):
        print("Could not find pom.xml at:", pom_path)
        print("Make sure you're running this script from the root of the repo.")
        return

    try:
        subprocess.Popen("mvn spring-boot:run", cwd=os.path.dirname(pom_path), shell=True)
        print("Backend is starting...")
    except Exception as e:
        print("Failed to start backend:", e)

# Open web pages after backend is ready
def open_frontend():
    if wait_for_port(8080):
        print("Opening frontend pages...")
        webbrowser.open("http://localhost:8080/h2-console")
        webbrowser.open("http://localhost:8080/admin.html")
        webbrowser.open("http://localhost:8080/index.html")
    else:
        print("Backend did not start in time. Skipping frontend launch.")

# Entry
def main():
    if not ensure_dependencies():
        print("Please install the missing dependencies and try again.")
        sys.exit(1)

    start_backend()
    open_frontend()

if __name__ == "__main__":
    main()

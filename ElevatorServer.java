// ElevatorServer.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ElevatorServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Elevator Server started on port 3000");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    if (inputLine.startsWith("floorRequest:")) {
                        String floor = inputLine.substring("floorRequest:".length());
                        handleFloorRequest(floor);
                    } else if (inputLine.equals("bye")) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) out.close();
                    if (in != null) in.close();
                    clientSocket.close();
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleFloorRequest(String floor) {
            String assignedElevator = assignElevator();
            int eta = calculateETA();
            out.println("elevatorAssigned:" + assignedElevator + "," + eta);
            simulateElevatorMovement(assignedElevator, eta);

        }

        private String assignElevator() {
            String[] elevators = {"A", "B", "C"};
            Random random = new Random();
            return elevators[random.nextInt(elevators.length)];
        }

        private int calculateETA() {
            Random random = new Random();
            return random.nextInt(10) + 5;
        }

        private void simulateElevatorMovement(String elevator, int eta) {
            try {
                for (int i = eta; i >= 0; i--) {
                    out.println("elevatorStatus:moving," + i);
                    Thread.sleep(1000); // Simulate 1 second delay
                }
                out.println("elevatorArrived");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
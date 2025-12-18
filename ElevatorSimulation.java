// ElevatorSimulation.java
public class ElevatorSimulation {
    public static void main(String[] args) {
        Elevator elevator = new Elevator(10); // 10-floor building
        elevator.addExternalRequest(5, 1); // Request from floor 5, going up
        elevator.addInternalRequest(8); // Inside elevator, request floor 8
        elevator.addExternalRequest(2,-1); // Request from floor 2, going down.
        elevator.run();
    }
}

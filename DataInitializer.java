package com.smartelevator.backend;

import com.smartelevator.backend.model.Elevator;
import com.smartelevator.backend.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;  // <-- Add this import

@Component
public class DataInitializer {

    private final ElevatorService elevatorService;

    @Autowired
    public DataInitializer(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
        initializeElevators(); // Ensure this is called in the constructor
    }

    public void initializeElevators() {
        // Create and save hardcoded elevators
        Elevator elevatorA = new Elevator("A", 10);
        Elevator elevatorB = new Elevator("B", 10);
        Elevator elevatorC = new Elevator("C", 10);

        elevatorService.saveElevators(List.of(elevatorA, elevatorB, elevatorC));
    }
}

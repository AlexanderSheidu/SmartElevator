package com.smartelevator.backend;

import com.smartelevator.backend.model.Elevator;
import com.smartelevator.backend.repository.ElevatorRepository;
import com.smartelevator.backend.service.ElevatorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ElevatorService
 */
@SpringBootTest
public class ElevatorServiceTests {

    @Autowired
    private ElevatorService elevatorService;

    @Autowired
    private ElevatorRepository elevatorRepository;

    /**
     * Setup method to initialize the database with hardcoded elevator data before each test.
     */
    @BeforeEach
    public void setup() {
        // Clear existing data before each test to ensure no duplicates
        elevatorRepository.deleteAll();

        // Add test data with unique labels
        Elevator elevatorA = new Elevator("A", 10);
        Elevator elevatorB = new Elevator("B", 10);
        Elevator elevatorC = new Elevator("C", 10);
        elevatorService.saveElevators(List.of(elevatorA, elevatorB, elevatorC));
    }

    /**
     * Test saving an elevator and ensuring it is correctly saved to the database.
     */
    @Test
    public void createElevatorTest() {
        // Ensure the label is unique before saving
        String elevatorLabel = "A";
        if (elevatorRepository.findByLabel(elevatorLabel) == null) {
            Elevator elevator = new Elevator(elevatorLabel, 10);
            elevatorService.saveElevators(List.of(elevator));  // Ensure saveElevators is called
        }

        // Ensure the label is unique and check for existence
        Elevator found = elevatorRepository.findByLabel("A");  // Searching for the saved elevator by label

        assertNotNull(found);  // Ensure the elevator is found
        assertEquals("A", found.getLabel());  // Ensure the label matches
        assertEquals(10, found.getCapacity());  // Ensure capacity is correct
    }

    /**
     * Test moving an elevator to a target floor and verifying the current floor.
     */
    @Test
    public void moveElevatorTest() {
        Elevator elevator = new Elevator("B", 10);
        elevatorService.saveElevators(List.of(elevator));  // Saving elevator
        elevatorService.moveElevator(elevator.getId(), 5);  // Moving elevator to floor 5
        assertEquals(5, elevatorService.getElevatorById(elevator.getId()).getCurrentFloor());  // Verifying current floor
    }

    /**
     * Test loading passengers into an elevator and ensuring the load is updated.
     */
    @Test
    public void loadPeopleTest() {
        Elevator elevator = new Elevator("C", 10);
        elevatorService.saveElevators(List.of(elevator));  // Saving elevator
        elevatorService.loadPeople(elevator.getId(), 3);  // Loading 3 passengers
        assertEquals(3, elevatorService.getElevatorById(elevator.getId()).getCurrentLoad());  // Verifying the current load

        // Try to load beyond capacity
        elevatorService.loadPeople(elevator.getId(), 8);  // Try loading 8 more passengers
        assertEquals(10, elevatorService.getElevatorById(elevator.getId()).getCurrentLoad());  // Ensure it doesn't exceed capacity
    }

    /**
     * Test unloading passengers from an elevator and ensuring the load is updated.
     */
    @Test
    public void unloadPeopleTest() {
        Elevator elevator = new Elevator("D", 10);
        elevatorService.saveElevators(List.of(elevator));  // Saving elevator
        elevatorService.loadPeople(elevator.getId(), 5);  // Loading 5 passengers
        elevatorService.unloadPeople(elevator.getId(), 3);  // Unloading 3 passengers
        assertEquals(2, elevatorService.getElevatorById(elevator.getId()).getCurrentLoad());  // Verifying the current load after unloading

        // Try unloading more than the current load
        elevatorService.unloadPeople(elevator.getId(), 5);  // Try unloading more passengers than in the elevator
        assertEquals(0, elevatorService.getElevatorById(elevator.getId()).getCurrentLoad());  // Ensure it doesn't go below 0
    }

    /**
     * Test deleting an elevator and ensuring it is removed from the database.
     */
    @Test
    public void deleteElevatorTest() {
        Elevator elevator = new Elevator("E", 10);
        elevatorService.saveElevators(List.of(elevator));  // Saving elevator
        elevatorService.deleteElevator(elevator.getId());  // Deleting elevator
        Elevator found = elevatorService.getElevatorById(elevator.getId());  // Searching for the deleted elevator
        assertNull(found);  // Ensure the elevator is not found
    }
}

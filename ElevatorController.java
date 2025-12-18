package com.smartelevator.backend.controller;

import com.smartelevator.backend.model.Elevator;
import com.smartelevator.backend.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * This controller handles all the API requests related to the elevators.
 * It uses the ElevatorService to manage the elevator-related operations.
 */
@RestController
@RequestMapping("/api/elevators")
public class ElevatorController {

    private final ElevatorService elevatorService;

    @Autowired
    public ElevatorController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    /**
     * Creates multiple elevators and saves them in the database.
     *
     * @param elevators List of Elevator objects to be created.
     * @return The list of created elevators.
     */
    @PostMapping
    public List<Elevator> createElevators(@RequestBody List<Elevator> elevators) {
        return elevatorService.saveElevators(elevators);
    }

    /**
     * Retrieves all elevators from the database.
     *
     * @return List of all elevators.
     */
    @GetMapping
    public List<Elevator> getAllElevators() {
        return elevatorService.getAllElevators();
    }

    /**
     * Retrieves a specific elevator by its ID.
     *
     * @param id The ID of the elevator to retrieve.
     * @return The Elevator object with the given ID.
     */
    @GetMapping("/{id}")
    public Elevator getElevator(@PathVariable Long id) {
        return elevatorService.getElevatorById(id);
    }

    /**
     * Moves the specified elevator to the target floor.
     *
     * @param id          The ID of the elevator to move.
     * @param targetFloor The floor to move the elevator to.
     * @return The updated Elevator object after moving.
     */
    @PostMapping("/{id}/move/{targetFloor}")
    public Elevator moveElevator(@PathVariable Long id, @PathVariable int targetFloor) {
        return elevatorService.moveElevator(id, targetFloor);
    }

    /**
     * Loads a specified number of people into the elevator.
     *
     * @param id        The ID of the elevator.
     * @param numPeople The number of people to load.
     * @return The updated Elevator object after loading people.
     */
    @PostMapping("/{id}/load/{numPeople}")
    public Elevator loadPeople(@PathVariable Long id, @PathVariable int numPeople) {
        return elevatorService.loadPeople(id, numPeople);
    }

    /**
     * Unloads a specified number of people from the elevator.
     *
     * @param id        The ID of the elevator.
     * @param numPeople The number of people to unload.
     * @return The updated Elevator object after unloading people.
     */
    @PostMapping("/{id}/unload/{numPeople}")
    public Elevator unloadPeople(@PathVariable Long id, @PathVariable int numPeople) {
        return elevatorService.unloadPeople(id, numPeople);
    }

    /**
     * Deletes the elevator with the specified ID.
     *
     * @param id The ID of the elevator to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteElevator(@PathVariable Long id) {
        elevatorService.deleteElevator(id);
    }

    /**
     * Assigns the best elevator for a given request.
     * Expects a JSON body with requestFloor, destinationFloor, and passengerCount.
     *
     * Example body:
     * {
     *   "requestFloor": 3,
     *   "destinationFloor": 7,
     *   "passengerCount": 4
     * }
     *
     * @param requestData A map containing the requestFloor, destinationFloor, and passengerCount.
     * @return The assigned Elevator object.
     */
    @PostMapping("/assign")
    public Elevator assignElevator(@RequestBody Map<String, Integer> requestData) {
        int requestFloor = requestData.getOrDefault("requestFloor", 0);
        int destinationFloor = requestData.getOrDefault("destinationFloor", 0);
        int passengerCount = requestData.getOrDefault("passengerCount", 1);

        return elevatorService.assignBestElevator(requestFloor, destinationFloor, passengerCount);
    }
}

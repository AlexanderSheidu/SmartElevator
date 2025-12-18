package com.smartelevator.backend.service;

import com.smartelevator.backend.model.Elevator;
import com.smartelevator.backend.repository.ElevatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * ElevatorService provides the business logic for managing elevators.
 * It handles tasks such as moving elevators, loading/unloading people,
 * assigning the best elevator for a request, simulating realistic delays,
 * and recording trips using ElevatorTripService.
 */
@Service
public class ElevatorService {

    private final ElevatorRepository elevatorRepository;
    private final ElevatorTripService tripService;
    private static final Logger logger = LoggerFactory.getLogger(ElevatorService.class);

    @Autowired
    public ElevatorService(ElevatorRepository elevatorRepository, ElevatorTripService tripService) {
        this.elevatorRepository = elevatorRepository;
        this.tripService = tripService;
    }

    /**
     * Saves a list of elevator entities to the database.
     *
     * @param elevators The list of elevators to save.
     * @return The saved list of elevators.
     */
    public List<Elevator> saveElevators(List<Elevator> elevators) {
        logger.info("Saving elevators: {}", elevators); // Log statement for saving elevators
        List<Elevator> savedElevators = elevatorRepository.saveAll(elevators);
        logger.info("Saved elevators: {}", savedElevators); // Log statement for saved elevators
        return savedElevators;
    }

    /**
     * Retrieves all elevators from the database.
     *
     * @return A list of all elevators.
     */
    public List<Elevator> getAllElevators() {
        return elevatorRepository.findAll();
    }

    /**
     * Retrieves a specific elevator by its ID.
     *
     * @param id The ID of the elevator.
     * @return The corresponding Elevator object, or null if not found.
     */
    public Elevator getElevatorById(Long id) {
        return elevatorRepository.findById(id).orElse(null);
    }

    /**
     * Moves an elevator to the specified floor and updates its status.
     *
     * @param id          The elevator ID.
     * @param targetFloor The destination floor.
     * @return The updated Elevator object.
     */
    public Elevator moveElevator(Long id, int targetFloor) {
        Elevator elevator = getElevatorById(id);
        if (elevator != null) {
            elevator.move(targetFloor);
            elevatorRepository.save(elevator);
        }
        return elevator;
    }

    /**
     * Loads passengers into an elevator and updates its state.
     *
     * @param id        Elevator ID.
     * @param numPeople Number of passengers boarding.
     * @return Updated Elevator object.
     */
    public Elevator loadPeople(Long id, int numPeople) {
        Elevator elevator = getElevatorById(id);
        if (elevator != null) {
            elevator.loadPeople(numPeople);
            elevatorRepository.save(elevator);
        }
        return elevator;
    }

    /**
     * Unloads passengers from an elevator and updates its state.
     *
     * @param id        Elevator ID.
     * @param numPeople Number of passengers exiting.
     * @return Updated Elevator object.
     */
    public Elevator unloadPeople(Long id, int numPeople) {
        Elevator elevator = getElevatorById(id);
        if (elevator != null) {
            elevator.unloadPeople(numPeople);
            elevatorRepository.save(elevator);
        }
        return elevator;
    }

    /**
     * Deletes an elevator from the database.
     *
     * @param id The ID of the elevator to delete.
     */
    public void deleteElevator(Long id) {
        elevatorRepository.deleteById(id);
    }

    /**
     * Assigns the most efficient elevator based on estimated travel time,
     * simulates a realistic trip with delays, and records the trip information.
     *
     * @param requestFloor      The floor where the user is calling the elevator.
     * @param destinationFloor  The floor the user wants to go to.
     * @param passengerCount    The number of passengers requesting the elevator.
     * @return The elevator assigned to handle the trip, or null if none available.
     */
    public Elevator assignBestElevator(int requestFloor, int destinationFloor, int passengerCount) {
        List<Elevator> elevators = elevatorRepository.findAll();

        // Find the best elevator that is not full and has the shortest estimated time,
        // taking into account the direction of movement and current load.
        Elevator bestElevator = elevators.stream()
                .filter(elevator -> !elevator.isFull()) // Only elevators with space
                .min(Comparator.comparingInt(e -> {
                    // Factor in the current direction (moving towards the request floor is better)
                    int timeToRequest = e.estimateTravelTime(requestFloor);
                    int directionPenalty = (e.getDirection().equals("UP") && requestFloor < e.getCurrentFloor()) ||
                                           (e.getDirection().equals("DOWN") && requestFloor > e.getCurrentFloor()) ? 5 : 0;
                    return timeToRequest + directionPenalty; // Add a penalty if direction is not optimal
                }))
                .orElse(null);

        if (bestElevator != null) {
            // Record the start time of the trip
            LocalDateTime startTime = LocalDateTime.now();

            // Simulate the estimated trip delays
            int floorsToTravel = Math.abs(bestElevator.getCurrentFloor() - requestFloor)
                                 + Math.abs(requestFloor - destinationFloor);

            long travelTimePerFloorMs = 1000;             // 1 second per floor
            long doorOperationDelayMs = 2000;             // 2 seconds total for doors opening/closing
            long boardingTimeMs = passengerCount * 1000L; // 1 second per person to board

            long simulatedTripTimeMs = (floorsToTravel * travelTimePerFloorMs)
                                     + doorOperationDelayMs
                                     + boardingTimeMs;

            try {
                // Simulate time passing (can be skipped or virtualized in production)
                Thread.sleep(simulatedTripTimeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Record the end time of the trip
            LocalDateTime endTime = LocalDateTime.now();

            // Update the elevator's position and status
            bestElevator.setDirection(destinationFloor > bestElevator.getCurrentFloor() ? "UP" : "DOWN");
            bestElevator.setMoving(true);
            bestElevator.setCurrentFloor(destinationFloor);
            bestElevator.setMoving(false);
            bestElevator.setDirection("IDLE");

            // Save the elevator’s new state
            elevatorRepository.save(bestElevator);

            // Save trip data to the database using elevator label as ID
            tripService.saveTrip(
                    bestElevator.getLabel(),
                    requestFloor,
                    destinationFloor,
                    passengerCount,
                    startTime,
                    endTime
            );
        }

        return bestElevator;
    }
}

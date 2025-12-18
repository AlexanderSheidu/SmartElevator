package com.smartelevator.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Elevator represents an elevator with various attributes such as its current
 * floor, direction, capacity, current load, and movement state. This class
 * provides functionality for loading/unloading people, moving between floors,
 * and tracking the elevator's status (moving, idle).
 */
@Entity
@Table(name = "elevator")
public class Elevator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for each elevator
    private String label; // 'A', 'B', or 'C'
    private String direction; // 'UP', 'DOWN', 'IDLE'
    private int currentFloor; // The current floor the elevator is on
    private int capacity; // Max number of people the elevator can hold
    private int currentLoad; // Current number of people in the elevator
    private boolean isMoving; // Indicates if the elevator is currently moving

    // Default constructor for JPA
    public Elevator() {}

    // Constructor
    public Elevator(String label, int capacity) {
        this.label = label;
        this.capacity = capacity;
        this.currentLoad = 0; // Initially, the elevator is empty
        this.currentFloor = 0; // Assuming ground floor (or floor 1)
        this.isMoving = false;
        this.direction = "IDLE"; // Elevator starts idle
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    // Method to simulate elevator moving
    public void move(int targetFloor) {
        if (currentLoad <= capacity) {
            this.direction = targetFloor > currentFloor ? "UP" : "DOWN";
            this.isMoving = true;
            this.currentFloor = targetFloor;
            this.isMoving = false;
            this.direction = "IDLE";
        }
    }

    // Method to load people into the elevator
    public void loadPeople(int numPeople) {
        if (currentLoad + numPeople <= capacity) {
            this.currentLoad += numPeople;
        } else {
            this.currentLoad = capacity; // Ensure it doesn't exceed the capacity
            System.out.println("Elevator is full, loading only up to capacity.");
        }
    }

    // Method to unload people from the elevator
    public void unloadPeople(int numPeople) {
        if (currentLoad >= numPeople) {
            this.currentLoad -= numPeople;
        } else {
            this.currentLoad = 0; // Ensure it doesn't go below zero
            System.out.println("Cannot unload more people than are in the elevator.");
        }
    }

    // Helper: Check if elevator is full
    public boolean isFull() {
        return currentLoad >= capacity;
    }

    // Helper: Check if elevator is idle
    public boolean isIdle() {
        return "IDLE".equalsIgnoreCase(direction);
    }

    // Helper: Estimate time to travel to a given floor (1 sec per floor for now)
    public int estimateTravelTime(int targetFloor) {
        return Math.abs(targetFloor - currentFloor);
    }
}

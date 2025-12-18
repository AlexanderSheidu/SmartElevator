package com.smartelevator.backend.controller;

import com.smartelevator.backend.model.ElevatorTrip;
import com.smartelevator.backend.service.ElevatorTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class ElevatorTripController {

    private final ElevatorTripService tripService;

    @Autowired
    public ElevatorTripController(ElevatorTripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public List<ElevatorTrip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @PostMapping
    public ElevatorTrip createTrip(@RequestBody ElevatorTrip trip) {
        return tripService.saveTrip(trip);
    }

    @GetMapping("/{id}")
    public ElevatorTrip getTripById(@PathVariable Long id) {
        return tripService.getTripById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
    }
}

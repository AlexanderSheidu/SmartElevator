package com.smartelevator.backend.service;

import com.smartelevator.backend.model.ElevatorTrip;
import com.smartelevator.backend.repository.ElevatorTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ElevatorTripService provides the business logic for managing elevator trips.
 * It handles tasks such as saving trip records, calculating time saved,
 * retrieving trips, and deleting them from the database.
 */
@Service
public class ElevatorTripService {

    private final ElevatorTripRepository tripRepository;
    private static final Logger logger = LoggerFactory.getLogger(ElevatorTripService.class);

    @Autowired
    public ElevatorTripService(ElevatorTripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * Saves a trip with calculated time savings using the smart elevator logic.
     *
     * @param elevatorId         ID of the elevator
     * @param originFloor        Starting floor
     * @param destinationFloor   Ending floor
     * @param passengerCount     Number of passengers
     * @param startTime          Time trip began
     * @param endTime            Time trip ended
     * @return ElevatorTrip object saved to the database
     */
    public ElevatorTrip saveTrip(String elevatorId, int originFloor, int destinationFloor,
                                 int passengerCount, LocalDateTime startTime, LocalDateTime endTime) {
        long smartDuration = Duration.between(startTime, endTime).toMillis();
        long traditionalDuration = (long) (smartDuration * 1.5);
        long timeSavedMs = Math.max(traditionalDuration - smartDuration, 0);

        logger.debug("Start Time: {}", startTime);
        logger.debug("End Time: {}", endTime);
        logger.debug("Smart Duration (ms): {}", smartDuration);
        logger.debug("Traditional Duration (ms): {}", traditionalDuration);
        logger.debug("Time Saved (ms): {}", timeSavedMs);

        ElevatorTrip trip = new ElevatorTrip(
                elevatorId,
                originFloor,
                destinationFloor,
                passengerCount,
                startTime,
                endTime,
                traditionalDuration,
                smartDuration,
                timeSavedMs
        );

        return tripRepository.save(trip);
    }

    /**
     * Saves an ElevatorTrip object directly to the database and calculates timeSavedMs if needed.
     *
     * @param trip The trip to be saved.
     * @return The saved ElevatorTrip.
     */
    public ElevatorTrip saveTrip(ElevatorTrip trip) {
        if (trip.getStartTime() != null && trip.getEndTime() != null) {
            long smartDuration = Duration.between(trip.getStartTime(), trip.getEndTime()).toMillis();
            trip.setSmartDurationMs(smartDuration);
            trip.setTraditionalDurationMs((long) (smartDuration * 1.5));
            trip.setTimeSavedMs(Math.max(trip.getTraditionalDurationMs() - smartDuration, 0));
        } else if (trip.getSmartDurationMs() > 0 && trip.getTraditionalDurationMs() > 0) {
            trip.setTimeSavedMs(Math.max(trip.getTraditionalDurationMs() - trip.getSmartDurationMs(), 0));
        } else {
            trip.setTimeSavedMs(0);
        }

        logger.debug("Saving trip with timeSavedMs = {}", trip.getTimeSavedMs());
        return tripRepository.save(trip);
    }

    public List<ElevatorTrip> getAllTrips() {
        return tripRepository.findAll();
    }

    public ElevatorTrip getTripById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}

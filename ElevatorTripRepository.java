package com.smartelevator.backend.repository;

import com.smartelevator.backend.model.ElevatorTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ElevatorTripRepository is a Spring Data JPA repository for the ElevatorTrip entity.
 * It provides CRUD operations for managing elevator trip records in the database.
 */
@Repository
public interface ElevatorTripRepository extends JpaRepository<ElevatorTrip, Long> {
}

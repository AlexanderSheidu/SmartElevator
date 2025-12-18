package com.smartelevator.backend.repository;

import com.smartelevator.backend.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ElevatorRepository is a Spring Data JPA repository for the Elevator entity.
 * It provides CRUD operations for managing elevator records in the database.
 */
@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Long> {
    Elevator findByLabel(String label);
}

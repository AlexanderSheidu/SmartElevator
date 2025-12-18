package com.smartelevator.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "elevator_trips")
public class ElevatorTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elevator_id")
    private String elevatorId;

    @Column(name = "origin_floor")
    private int originFloor;

    @Column(name = "destination_floor")
    private int destinationFloor;

    @Column(name = "passenger_count")
    private int passengerCount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "traditional_duration_ms")
    private long traditionalDurationMs;

    @Column(name = "smart_duration_ms")
    private long smartDurationMs;

    @Column(name = "time_saved_ms")
    private long timeSavedMs;

    public ElevatorTrip() {
    }

    public ElevatorTrip(String elevatorId, int originFloor, int destinationFloor, int passengerCount,
                        LocalDateTime startTime, LocalDateTime endTime,
                        long traditionalDurationMs, long smartDurationMs, long timeSavedMs) {
        this.elevatorId = elevatorId;
        this.originFloor = originFloor;
        this.destinationFloor = destinationFloor;
        this.passengerCount = passengerCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.traditionalDurationMs = traditionalDurationMs;
        this.smartDurationMs = smartDurationMs;
        this.timeSavedMs = timeSavedMs;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getOriginFloor() {
        return originFloor;
    }

    public void setOriginFloor(int originFloor) {
        this.originFloor = originFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getTraditionalDurationMs() {
        return traditionalDurationMs;
    }

    public void setTraditionalDurationMs(long traditionalDurationMs) {
        this.traditionalDurationMs = traditionalDurationMs;
    }

    public long getSmartDurationMs() {
        return smartDurationMs;
    }

    public void setSmartDurationMs(long smartDurationMs) {
        this.smartDurationMs = smartDurationMs;
    }

    public long getTimeSavedMs() {
        return timeSavedMs;
    }

    public void setTimeSavedMs(long timeSavedMs) {
        this.timeSavedMs = timeSavedMs;
    }

    @Override
    public String toString() {
        return "ElevatorTrip{" +
                "id=" + id +
                ", elevatorId='" + elevatorId + '\'' +
                ", originFloor=" + originFloor +
                ", destinationFloor=" + destinationFloor +
                ", passengerCount=" + passengerCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", traditionalDurationMs=" + traditionalDurationMs +
                ", smartDurationMs=" + smartDurationMs +
                ", timeSavedMs=" + timeSavedMs +
                '}';
    }
}

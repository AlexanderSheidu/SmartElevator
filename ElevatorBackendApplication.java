package com.smartelevator.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ElevatorBackendApplication is the main entry point of the Spring Boot application.
 * It initializes the application and runs the embedded web server. 
 * This is where the Spring Boot application is launched.
 */
@SpringBootApplication
public class ElevatorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElevatorBackendApplication.class, args);
    }
}

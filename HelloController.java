package com.smartelevator.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController serves as a simple test controller to check if the 
 * application is running. It returns a "Hello, World!" message when accessed.
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }
}

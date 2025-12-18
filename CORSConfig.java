package com.smartelevator.backend;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSConfig {
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        var corsFilter = new CorsFilter(request -> {
            var config = new org.springframework.web.cors.CorsConfiguration();
            config.addAllowedOrigin("*"); // Allow all origins (for development)
            config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
            config.addAllowedHeader("*"); // Allow all headers
            return config;
        });

        var registration = new FilterRegistrationBean<>(corsFilter);
        registration.setOrder(0); // Ensure this filter is applied first
        return registration;
    }
}

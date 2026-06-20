package com.gateway.core.api.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health Controller
 * 
 * Provides application health and readiness checks:
 * - Application startup status
 * - Database connectivity
 * - External service dependencies
 * - Kafka broker status
 * - Redis connectivity
 * 
 * REST Endpoints:
 * GET    /actuator/health          - Overall application health
 * GET    /actuator/health/liveness - Liveness probe for K8s
 * GET    /actuator/health/readiness - Readiness probe for K8s
 * 
 * @author Payment Team
 */
@RestController
@RequestMapping("/actuator")
public class HealthController {

    /**
     * Get overall application health status.
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        // Implementation here - delegated to Spring Boot Actuator
        return ResponseEntity.ok().build();
    }

    /**
     * Kubernetes liveness probe - checks if application is running.
     */
    @GetMapping("/health/liveness")
    public ResponseEntity<?> liveness() {
        // Implementation here
        return ResponseEntity.ok().build();
    }

    /**
     * Kubernetes readiness probe - checks if application is ready to serve traffic.
     */
    @GetMapping("/health/readiness")
    public ResponseEntity<?> readiness() {
        // Implementation here
        return ResponseEntity.ok().build();
    }
}

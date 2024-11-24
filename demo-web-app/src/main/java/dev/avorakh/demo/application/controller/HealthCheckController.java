package dev.avorakh.demo.application.controller;

import dev.avorakh.demo.application.resource.HealthCheck;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/status")
    public HealthCheck status() {
        return new HealthCheck(true);
    }
}

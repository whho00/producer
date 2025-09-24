package com.george.kafka.producer1;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public HealthController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public String healthCheck() {
        return "Order service is running!";
    }

}

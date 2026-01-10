package com.abdul.checkinservice.config.circuitbreaker;

import com.abdul.checkinservice.adapter.out.api.RecordingServiceApiClient;
import com.abdul.checkinservice.config.kafka.KafkaListenerContainerManager;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig recordingServiceConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(2)
                .failureRateThreshold(50)
                .slowCallRateThreshold(80)
                .slowCallDurationThreshold(Duration.ofSeconds(3))
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();

        return CircuitBreakerRegistry.of(
                java.util.Map.of(
                        RecordingServiceApiClient.CIRCUIT_BREAKER_NAME, recordingServiceConfig
                )
        );
    }

    @Bean
    public CircuitBreaker recordingServiceCircuitBreaker(
            CircuitBreakerRegistry circuitBreakerRegistry,
            KafkaListenerContainerManager kafkaListenerContainerManager) {

        CircuitBreaker circuitBreaker = circuitBreakerRegistry
                .circuitBreaker(RecordingServiceApiClient.CIRCUIT_BREAKER_NAME);

        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {
                    log.info("Circuit breaker '{}' state transition: {}",
                            event.getCircuitBreakerName(), event.getStateTransition());

                    switch (event.getStateTransition()) {
                        case CLOSED_TO_OPEN, CLOSED_TO_FORCED_OPEN, HALF_OPEN_TO_OPEN -> {
                            log.warn("Circuit breaker OPEN - Pausing recording service consumer");
                            kafkaListenerContainerManager.pauseRecordingServiceListener();
                        }
                        case OPEN_TO_HALF_OPEN -> {
                            log.info("Circuit breaker HALF_OPEN - Resuming recording service consumer for testing");
                            kafkaListenerContainerManager.resumeRecordingServiceListener();
                        }
                        case HALF_OPEN_TO_CLOSED, FORCED_OPEN_TO_CLOSED, FORCED_OPEN_TO_HALF_OPEN -> {
                            log.info("Circuit breaker CLOSED - Recording service consumer resumed");
                            kafkaListenerContainerManager.resumeRecordingServiceListener();
                        }
                    }
                });

        circuitBreaker.getEventPublisher()
                .onError(event -> log.error("Circuit breaker error: {}", event.getThrowable().getMessage()))
                .onSuccess(event -> log.debug("Circuit breaker call succeeded"))
                .onCallNotPermitted(event -> log.warn("Circuit breaker call not permitted - circuit is OPEN"));

        return circuitBreaker;
    }
}

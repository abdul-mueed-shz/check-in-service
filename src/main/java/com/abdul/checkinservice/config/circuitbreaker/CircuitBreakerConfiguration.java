package com.abdul.checkinservice.config.circuitbreaker;

import com.abdul.checkinservice.config.AppProperties;
import com.abdul.checkinservice.domain.common.manager.ListenerContainerManager;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CircuitBreakerConfiguration {

    private final AppProperties appProperties;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final ListenerContainerManager listenerContainerManager;

    @PostConstruct
    public void registerAllCircuitBreakerEventListeners() {
        appProperties.getAllCircuitBreakerServices().forEach(service -> {
            if (Boolean.FALSE.equals(service.getCircuitBreakerEnabled())) {
                log.info("Circuit breaker event handler '{}' is disabled for the service", service.getName());
                return;
            }
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(service.getName());

            circuitBreaker.getEventPublisher()
                    .onStateTransition(event ->
                            handleStateTransition(event, service.getListenerId()))
                    .onError(event ->
                            log.error("Circuit breaker '{}' error: {}", service.getName(),
                                    event.getThrowable().getMessage()))
                    .onSuccess(event ->
                            log.debug("Circuit breaker '{}' call succeeded", service.getName()))
                    .onCallNotPermitted(event ->
                            log.warn("Circuit breaker '{}' OPEN - call not permitted", service.getName()));

            log.info("Registered event listeners for circuit breaker: '{}'", service.getName());
        });
    }

    private void handleStateTransition(
            CircuitBreakerOnStateTransitionEvent event,
            String listenerId) {

        String circuitBreakerName = event.getCircuitBreakerName();
        log.info("Circuit breaker '{}' state transition: {}", circuitBreakerName, event.getStateTransition());

        switch (event.getStateTransition()) {
            case CLOSED_TO_OPEN, CLOSED_TO_FORCED_OPEN, HALF_OPEN_TO_OPEN -> {
                log.warn("Circuit breaker '{}' OPEN - Pausing associated consumer", circuitBreakerName);
                listenerContainerManager.pauseListener(listenerId);
            }
            case OPEN_TO_HALF_OPEN -> {
                log.info("Circuit breaker '{}' HALF_OPEN - Resuming consumer for testing", circuitBreakerName);
                listenerContainerManager.resumeListener(listenerId);
            }
            case HALF_OPEN_TO_CLOSED, FORCED_OPEN_TO_CLOSED, FORCED_OPEN_TO_HALF_OPEN -> {
                log.info("Circuit breaker '{}' CLOSED - Consumer resumed", circuitBreakerName);
                listenerContainerManager.resumeListener(listenerId);
            }
        }
    }
}

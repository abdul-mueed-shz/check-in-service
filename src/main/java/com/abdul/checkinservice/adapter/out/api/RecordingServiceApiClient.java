package com.abdul.checkinservice.adapter.out.api;

import com.abdul.checkinservice.adapter.out.exception.RecordingServiceException;
import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecordingServiceApiClient {

    @CircuitBreaker(name = "${app.services.recording-service.name}")
    public void notifyRecordingService(EmployeeTrackedHoursDto dto) {
        if (Math.random() < 0.7) {
            throw new RecordingServiceException("Service Unavailable ");
        }
        log.info("Recording service notified successfully about employee {} with {} tracked hours",
                dto.employeeId(),
                dto.trackedHours());
    }
}


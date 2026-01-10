package com.abdul.checkinservice.domain.common.model;

public record EmployeeTrackedHoursDto(
        Long recordId,
        Long employeeId,
        Integer trackedHours
) {
}

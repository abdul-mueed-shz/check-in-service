package com.abdul.checkinservice.domain.common.model;

public record EmployeeTrackedHoursDto(
        Long employeeId,
        Integer trackedHours
) {
}

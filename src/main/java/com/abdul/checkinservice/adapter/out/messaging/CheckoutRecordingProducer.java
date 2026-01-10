package com.abdul.checkinservice.adapter.out.messaging;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;

public interface CheckoutRecordingProducer {
    void produce(EmployeeTrackedHoursDto employeeTrackedHoursDto);
}

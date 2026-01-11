package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.adapter.out.mapper.TimeSheetMapper;
import com.abdul.checkinservice.adapter.out.messaging.CheckoutEmailProducer;
import com.abdul.checkinservice.adapter.out.messaging.CheckoutRecordingProducer;
import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import com.abdul.checkinservice.domain.common.utils.DateUtils;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckOutUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeCheckOutUseCaseImpl implements EmployeeCheckOutUseCase {
    private final TimeSheetRepository timeSheetRepository;
    private final TimeSheetMapper timeSheetMapper;

    private final CheckoutEmailProducer checkoutEmailProducer;
    private final CheckoutRecordingProducer checkoutRecordingProducer;

    @Transactional
    @Override
    public TimeSheetDto checkoutWithDuration(TimeSheetDto existingTimeSheetDto, Integer hoursWorked) {
        TimeSheetDto updatedEmployeeTimeSheet = updateEmployeeTimeSheet(existingTimeSheetDto);
        trackHours(updatedEmployeeTimeSheet, hoursWorked);
        return updatedEmployeeTimeSheet;
    }

    @Transactional
    @Override
    public TimeSheetDto checkout(TimeSheetDto existingTimeSheetDto) {
        TimeSheetDto updatedEmployeeTimeSheet = updateEmployeeTimeSheet(existingTimeSheetDto);
        Integer hoursWorked = DateUtils.getDifferenceBetweenInstants(
                updatedEmployeeTimeSheet.getCheckIn().toInstant(),
                updatedEmployeeTimeSheet.getCheckOut().toInstant());
        trackHours(updatedEmployeeTimeSheet, hoursWorked);
        return updatedEmployeeTimeSheet;
    }


    private void trackHours(TimeSheetDto updatedEmployeeTimeSheet, Integer hoursWorked) {
        log.info("Employee {} worked {} hours (checkIn={}, checkOut={})",
                updatedEmployeeTimeSheet.getEmployeeId(), hoursWorked,
                updatedEmployeeTimeSheet.getCheckIn(), updatedEmployeeTimeSheet.getCheckOut());

        EmployeeTrackedHoursDto employeeTrackedHoursDto = new EmployeeTrackedHoursDto(
                updatedEmployeeTimeSheet.getId(),
                updatedEmployeeTimeSheet.getEmployeeId(),
                hoursWorked
        );

        checkoutEmailProducer.produce(employeeTrackedHoursDto);
        checkoutRecordingProducer.produce(employeeTrackedHoursDto);
    }

    private TimeSheetDto updateEmployeeTimeSheet(TimeSheetDto timeSheetDto) {
        TimeSheetDto updatedEmployeeTimeSheet = timeSheetMapper.toUpdatedDto(
                timeSheetDto,
                TimeSheetDto.builder()
                        .checkOut(Timestamp.from(java.time.Instant.now()))
                        .build()
        );
        timeSheetRepository.upsertTimeSheet(updatedEmployeeTimeSheet);
        return updatedEmployeeTimeSheet;
    }

}

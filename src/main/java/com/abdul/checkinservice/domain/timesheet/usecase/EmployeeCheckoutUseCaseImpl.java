package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.adapter.out.mapper.TimeSheetMapper;
import com.abdul.checkinservice.adapter.out.messaging.CheckoutEmailProducer;
import com.abdul.checkinservice.adapter.out.messaging.CheckoutRecordingProducer;
import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckoutUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeCheckoutUseCaseImpl implements EmployeeCheckoutUseCase {
    private final TimeSheetRepository timeSheetRepository;
    private final TimeSheetMapper timeSheetMapper;

    private final CheckoutEmailProducer checkoutEmailProducer;
    private final CheckoutRecordingProducer checkoutRecordingProducer;

    @Transactional
    @Override
    public TimeSheetDto execute(TimeSheetDto existingTimeSheetDto) throws MessagingException {
        TimeSheetDto updatedEmployeeTimeSheet = timeSheetMapper.toUpdatedDto(
                existingTimeSheetDto,
                TimeSheetDto.builder()
                        .checkOut(Timestamp.from(java.time.Instant.now()))
                        .build()
        );
        timeSheetRepository.upsertTimeSheet(updatedEmployeeTimeSheet);
        Integer hoursWorked = getWorkedHours(updatedEmployeeTimeSheet);

        log.info("Employee {} worked {} hours (checkIn={}, checkOut={})",
                updatedEmployeeTimeSheet.getEmployeeId(), hoursWorked,
                updatedEmployeeTimeSheet.getCheckIn(), updatedEmployeeTimeSheet.getCheckOut());

        EmployeeTrackedHoursDto employeeTrackedHoursDto = new EmployeeTrackedHoursDto(
                updatedEmployeeTimeSheet.getEmployeeId(),
                hoursWorked
        );
        checkoutEmailProducer.produce(employeeTrackedHoursDto);
        checkoutRecordingProducer.produce(employeeTrackedHoursDto);
        return updatedEmployeeTimeSheet;
    }

    private Integer getWorkedHours(TimeSheetDto updatedEmployeeTimeSheet) {
        Instant checkInInstant = updatedEmployeeTimeSheet.getCheckIn().toInstant();
        Instant checkOutInstant = updatedEmployeeTimeSheet.getCheckOut().toInstant();
        Duration duration = Duration.between(checkInInstant, checkOutInstant);
        long hours = duration.toHours();
        if (hours < 0) {
            return 0;
        }
        return (int) hours;
    }
}

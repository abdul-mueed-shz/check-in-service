package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.adapter.out.mapper.TimeSheetMapper;
import com.abdul.checkinservice.domain.email.port.in.SendTrackedHoursEmailUseCase;
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

    private final SendTrackedHoursEmailUseCase sendTrackedHoursEmailUseCase;

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
        // Todo Send to message queue of emails to send email to user
        // Todo Send to message queue of recording service to send timesheet data to the other service
        // Todo Remove the test call below
        sendTrackedHoursEmailUseCase.execute(
                "abdulmueedshahbaz@gmail.com",
                "Abdul Mueed",
                hoursWorked
        );
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

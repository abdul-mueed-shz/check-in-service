package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.config.AppProperties;
import com.abdul.checkinservice.domain.common.model.MessageDto;
import com.abdul.checkinservice.domain.common.utils.DateUtils;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckInUseCase;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckOutUseCase;
import com.abdul.checkinservice.domain.timesheet.port.in.RecordEmployeeTimeDataUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordEmployeeTimeDataUseCaseImpl implements RecordEmployeeTimeDataUseCase {
    private final AppProperties appProperties;

    private final TimeSheetRepository timeSheetRepository;
    private final EmployeeCheckInUseCase employeeCheckInUseCase;
    private final EmployeeCheckOutUseCase employeeCheckoutUseCase;

    @Transactional
    @Override
    public MessageDto<TimeSheetDto> execute(Long employeeId) {
        TimeSheetDto employeeTimeSheet = timeSheetRepository.getCurrentEmployeeTimeSheetById(employeeId);
        if (employeeTimeSheet == null) {
            TimeSheetDto timeSheetDto = employeeCheckInUseCase.execute(employeeId);
            return MessageDto.<TimeSheetDto>builder()
                    .message("Employee checked in successfully")
                    .data(timeSheetDto)
                    .build();
        }
        Integer trackedHours = DateUtils.getDifferenceBetweenInstants(
                employeeTimeSheet.getCheckIn().toInstant(),
                Instant.now());
        if (trackedHours > appProperties.getShift().getMaxDuration()) {
            Integer shiftHours = trackedHours - (trackedHours - appProperties.getShift().getMaxDuration());
            log.info("Employee {} missed his checkout after shift duration of {} hours, " +
                            "Recording {} of his shift hours excluding {} extra hours and closing the shift",
                    employeeId,
                    appProperties.getShift().getMaxDuration(),
                    shiftHours,
                    trackedHours - shiftHours);
            employeeCheckoutUseCase.checkoutWithDuration(employeeTimeSheet, shiftHours);
            TimeSheetDto timeSheetDto = employeeCheckInUseCase.execute(employeeId);
            log.info("Initiated new shift for employee {}", employeeId);
            return MessageDto.<TimeSheetDto>builder()
                    .message("Employee checked in successfully")
                    .data(timeSheetDto)
                    .build();
        }
        TimeSheetDto updateTimeSheetDto = employeeCheckoutUseCase.checkout(employeeTimeSheet);
        return MessageDto.<TimeSheetDto>builder()
                .message("Employee checked out successfully")
                .data(updateTimeSheetDto)
                .build();
    }
}

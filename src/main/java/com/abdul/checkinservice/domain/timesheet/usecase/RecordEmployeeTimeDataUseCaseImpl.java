package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.domain.common.model.MessageDto;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckInUseCase;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckoutUseCase;
import com.abdul.checkinservice.domain.timesheet.port.in.RecordEmployeeTimeDataUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordEmployeeTimeDataUseCaseImpl implements RecordEmployeeTimeDataUseCase {
    private final TimeSheetRepository timeSheetRepository;
    private final EmployeeCheckInUseCase employeeCheckInUseCase;
    private final EmployeeCheckoutUseCase employeeCheckoutUseCase;

    @Override
    public MessageDto<TimeSheetDto> execute(Long employeeId) throws MessagingException {
        TimeSheetDto employeeTimeSheet = timeSheetRepository.getCurrentEmployeeTimeSheetById(employeeId);
        if (employeeTimeSheet == null) {
            TimeSheetDto timeSheetDto = employeeCheckInUseCase.execute(employeeId);
            return MessageDto.<TimeSheetDto>builder()
                    .message("Employee checked in successfully")
                    .data(timeSheetDto)
                    .build();
        }
        // Todo: Mark the existing timesheet as checked out automatically after a certain threshold set in app props
        // And then create a new timesheet for him instead. The usecase is that user forgot to checkout previous day
        TimeSheetDto updateTimeSheetDto = employeeCheckoutUseCase.execute(employeeTimeSheet);
        return MessageDto.<TimeSheetDto>builder()
                .message("Employee checked out successfully")
                .data(updateTimeSheetDto)
                .build();
    }
}

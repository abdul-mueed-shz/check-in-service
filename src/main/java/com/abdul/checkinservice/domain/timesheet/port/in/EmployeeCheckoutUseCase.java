package com.abdul.checkinservice.domain.timesheet.port.in;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import jakarta.mail.MessagingException;

public interface EmployeeCheckoutUseCase {
    TimeSheetDto execute(TimeSheetDto existingTimeSheetDto) throws MessagingException;
}

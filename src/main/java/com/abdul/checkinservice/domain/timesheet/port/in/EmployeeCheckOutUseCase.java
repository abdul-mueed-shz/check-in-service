package com.abdul.checkinservice.domain.timesheet.port.in;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;

public interface EmployeeCheckOutUseCase {
    TimeSheetDto checkout(TimeSheetDto existingTimeSheetDto);

    TimeSheetDto checkoutWithDuration(TimeSheetDto existingTimeSheetDto, Integer hoursWorked);
}

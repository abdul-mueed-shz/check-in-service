package com.abdul.checkinservice.domain.timesheet.port.in;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;

public interface EmployeeCheckInUseCase {
    TimeSheetDto execute(Long employeeId);
}

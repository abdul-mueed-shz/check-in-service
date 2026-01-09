package com.abdul.checkinservice.domain.timesheet.port.in;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;

import java.util.List;

public interface FetchEmployeeTimeSheetUseCase {
    List<TimeSheetDto> allByEmployeeId(Long employeeId);
}

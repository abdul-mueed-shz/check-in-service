package com.abdul.checkinservice.domain.timesheet.port.in;

import com.abdul.checkinservice.domain.common.model.MessageDto;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;

public interface RecordEmployeeTimeDataUseCase {
    MessageDto<TimeSheetDto> execute(Long employeeId);
}

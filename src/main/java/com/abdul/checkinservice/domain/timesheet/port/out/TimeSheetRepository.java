package com.abdul.checkinservice.domain.timesheet.port.out;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;

import java.util.List;

public interface TimeSheetRepository {
    TimeSheetDto getCurrentEmployeeTimeSheetById(Long employeeId);

    TimeSheetDto upsertTimeSheet(TimeSheetDto timeSheetDto);

    List<TimeSheetDto> getAllByEmployeeId(Long employeeId);

    TimeSheetDto getByRecordId(Long recordId);
}

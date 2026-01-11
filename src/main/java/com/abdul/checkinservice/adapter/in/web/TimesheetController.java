package com.abdul.checkinservice.adapter.in.web;

import com.abdul.checkinservice.domain.common.model.MessageDto;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.RecordEmployeeTimeDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/timesheets")
@RestController
@RequiredArgsConstructor
public class TimesheetController {
    private final RecordEmployeeTimeDataUseCase recordEmployeeTimeDataUseCase;

    @PostMapping("/{employeeId}/record")
    public ResponseEntity<MessageDto<TimeSheetDto>> recordEmployeeTimeData(@PathVariable Long employeeId) {
        return ResponseEntity.ok(recordEmployeeTimeDataUseCase.execute(employeeId));
    }

}

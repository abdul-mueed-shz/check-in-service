package com.abdul.checkinservice.adapter.in.web;

import com.abdul.checkinservice.domain.common.model.MessageDto;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.FetchEmployeeTimeSheetUseCase;
import com.abdul.checkinservice.domain.timesheet.port.in.RecordEmployeeTimeDataUseCase;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/timesheets")
@RestController
@RequiredArgsConstructor
public class TimesheetController {
    private final RecordEmployeeTimeDataUseCase recordEmployeeTimeDataUseCase;
    private final FetchEmployeeTimeSheetUseCase fetchEmployeeTimeSheetUseCase;

    @PostMapping("/{employeeId}/record")
    public ResponseEntity<MessageDto<TimeSheetDto>> recordEmployeeTimeData(@PathVariable Long employeeId) throws MessagingException {
        return ResponseEntity.ok(recordEmployeeTimeDataUseCase.execute(employeeId));
    }


    @GetMapping("/{employeeId}/records")
    public ResponseEntity<List<TimeSheetDto>> fetchEmployeeTimeData(@PathVariable Long employeeId) {
        return ResponseEntity.ok(fetchEmployeeTimeSheetUseCase.allByEmployeeId(employeeId));
    }

}

package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.EmployeeCheckInUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeCheckInUseCaseImpl implements EmployeeCheckInUseCase {
    private final TimeSheetRepository timeSheetRepository;

    @Override
    public TimeSheetDto execute(Long employeeId) {
        return timeSheetRepository.upsertTimeSheet(TimeSheetDto.toNewEmployeeTimeRecord(employeeId));
    }
}

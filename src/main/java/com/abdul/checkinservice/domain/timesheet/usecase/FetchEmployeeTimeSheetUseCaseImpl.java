package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.FetchEmployeeTimeSheetUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchEmployeeTimeSheetUseCaseImpl implements FetchEmployeeTimeSheetUseCase {
    private final TimeSheetRepository timeSheetRepository;

    @Override
    public List<TimeSheetDto> allByEmployeeId(Long employeeId) {
        return timeSheetRepository.getAllByEmployeeId(employeeId);
    }
}

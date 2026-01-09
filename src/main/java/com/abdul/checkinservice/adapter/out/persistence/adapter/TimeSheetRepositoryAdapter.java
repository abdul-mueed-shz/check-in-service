package com.abdul.checkinservice.adapter.out.persistence.adapter;

import com.abdul.checkinservice.adapter.out.mapper.TimeSheetMapper;
import com.abdul.checkinservice.adapter.out.persistence.entity.TimeSheet;
import com.abdul.checkinservice.adapter.out.persistence.repository.TimesheetJpaRepository;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimeSheetRepositoryAdapter implements TimeSheetRepository {

    private final TimesheetJpaRepository timesheetJpaRepository;
    private final TimeSheetMapper timeSheetMapper;

    @Override
    public TimeSheetDto getCurrentEmployeeTimeSheetById(Long employeeId) {
        Optional<TimeSheet> timeSheetOptional =
                timesheetJpaRepository.findByEmployeeIdAndCheckInNotNullAndCheckOutIsNull(employeeId);
        return timeSheetOptional.map(timeSheetMapper::toDto).orElse(null);
    }

    @Override
    public TimeSheetDto upsertTimeSheet(TimeSheetDto timeSheetDto) {
        return timeSheetMapper.toDto(timesheetJpaRepository.save(timeSheetMapper.toEntity(timeSheetDto)));
    }

    @Override
    public List<TimeSheetDto> getAllByEmployeeId(Long employeeId) {
        return timeSheetMapper.toDtoList(timesheetJpaRepository.findAllByEmployeeId(employeeId));
    }
}

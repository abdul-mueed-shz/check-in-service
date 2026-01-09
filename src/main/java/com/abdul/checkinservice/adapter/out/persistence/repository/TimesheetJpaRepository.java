package com.abdul.checkinservice.adapter.out.persistence.repository;

import com.abdul.checkinservice.adapter.out.persistence.entity.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimesheetJpaRepository extends JpaRepository<TimeSheet, Long> {
    Optional<TimeSheet> findByEmployeeIdAndCheckInNotNullAndCheckOutIsNull(Long employeeId);

    List<TimeSheet> findAllByEmployeeId(Long employeeId);
}

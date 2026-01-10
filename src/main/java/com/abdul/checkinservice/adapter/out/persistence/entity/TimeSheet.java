package com.abdul.checkinservice.adapter.out.persistence.entity;

import com.abdul.checkinservice.domain.timesheet.enums.EmailStatusEnum;
import com.abdul.checkinservice.domain.timesheet.enums.RecordingServiceAckEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeSheet extends BaseEntity {

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private Timestamp checkIn;

    private Timestamp checkOut;

    @Column
    @Enumerated(value = EnumType.STRING)
    private EmailStatusEnum emailDeliveryStatus = EmailStatusEnum.PENDING;

    @Column
    @Enumerated(value = EnumType.STRING)
    private RecordingServiceAckEnum recordingServiceAcknowledgement = RecordingServiceAckEnum.PENDING;
}

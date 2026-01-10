package com.abdul.checkinservice.domain.timesheet.model;

import com.abdul.checkinservice.domain.common.model.BaseDto;
import com.abdul.checkinservice.domain.timesheet.enums.EmailStatusEnum;
import com.abdul.checkinservice.domain.timesheet.enums.RecordingServiceAckEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetDto extends BaseDto {
    private Long employeeId;
    private Timestamp checkIn;
    private Timestamp checkOut;
    private EmailStatusEnum emailDeliveryStatus;
    private RecordingServiceAckEnum recordingServiceAcknowledgement;


    public static TimeSheetDto toNewEmployeeTimeRecord(Long employeeId) {
        return TimeSheetDto.builder()
                .employeeId(employeeId)
                .checkIn(Timestamp.from(Instant.now()))
                .emailDeliveryStatus(EmailStatusEnum.PENDING)
                .recordingServiceAcknowledgement(RecordingServiceAckEnum.PENDING)
                .build();
    }
}

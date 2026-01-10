package com.abdul.checkinservice.domain.timesheet.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecordingServiceAckEnum {
    PENDING,
    NOTIFIED,
    FAILED
}

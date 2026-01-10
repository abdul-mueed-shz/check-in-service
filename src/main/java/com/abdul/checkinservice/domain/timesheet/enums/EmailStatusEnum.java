package com.abdul.checkinservice.domain.timesheet.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailStatusEnum {
    PENDING, SENT, FAILED;
}

package com.abdul.checkinservice.domain.timesheet.port.in;

import com.abdul.checkinservice.domain.timesheet.enums.EmailStatusEnum;
import com.abdul.checkinservice.domain.timesheet.enums.RecordingServiceAckEnum;

public interface UpdateTimeSheetUseCase {
    void updateEmailDeliveryStatus(Long recordId, EmailStatusEnum emailStatusEnum);

    void updateRecordServiceAcknowledgementStatus(Long recordId, RecordingServiceAckEnum recordingServiceAckEnum);

}

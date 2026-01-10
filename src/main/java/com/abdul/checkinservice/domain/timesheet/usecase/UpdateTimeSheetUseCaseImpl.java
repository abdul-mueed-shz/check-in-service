package com.abdul.checkinservice.domain.timesheet.usecase;

import com.abdul.checkinservice.domain.timesheet.enums.EmailStatusEnum;
import com.abdul.checkinservice.domain.timesheet.enums.RecordingServiceAckEnum;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import com.abdul.checkinservice.domain.timesheet.port.in.UpdateTimeSheetUseCase;
import com.abdul.checkinservice.domain.timesheet.port.out.TimeSheetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTimeSheetUseCaseImpl implements UpdateTimeSheetUseCase {

    private final TimeSheetRepository timeSheetRepository;

    @Async("threadPoolTaskExecutor")
    @Transactional
    @Override
    public void updateEmailDeliveryStatus(Long recordId, EmailStatusEnum emailStatusEnum) {
        findTimeSheetOrLogError(recordId).ifPresent(timeSheet -> {
            timeSheet.setEmailDeliveryStatus(emailStatusEnum);
            timeSheetRepository.upsertTimeSheet(timeSheet);
            log.info("Email delivery status updated to {} for recordId: {}", emailStatusEnum, recordId);
        });
    }

    @Async("threadPoolTaskExecutor")
    @Transactional
    @Override
    public void updateRecordServiceAcknowledgementStatus(Long recordId, RecordingServiceAckEnum recordingServiceAckEnum) {
        findTimeSheetOrLogError(recordId).ifPresent(timeSheet -> {
            timeSheet.setRecordingServiceAcknowledgement(recordingServiceAckEnum);
            timeSheetRepository.upsertTimeSheet(timeSheet);
            log.info("Recording service acknowledgement updated to {} for recordId: {}", recordingServiceAckEnum, recordId);
        });
    }

    private Optional<TimeSheetDto> findTimeSheetOrLogError(Long recordId) {
        TimeSheetDto timeSheetDto = timeSheetRepository.getByRecordId(recordId);
        if (timeSheetDto == null) {
            log.error("TimeSheet record not found for recordId: {}", recordId);
            return Optional.empty();
        }
        return Optional.of(timeSheetDto);
    }
}

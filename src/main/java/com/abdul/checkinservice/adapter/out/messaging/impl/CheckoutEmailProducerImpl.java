package com.abdul.checkinservice.adapter.out.messaging.impl;

import com.abdul.checkinservice.adapter.out.messaging.CheckoutEmailProducer;
import com.abdul.checkinservice.config.kafka.KafkaProperties;
import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutEmailProducerImpl implements CheckoutEmailProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public void produce(EmployeeTrackedHoursDto employeeTrackedHoursDto) {
        log.info("Queuing a call for email service to send a confirmation email to employee {} with tracked hours : {}",
                employeeTrackedHoursDto.employeeId(),
                employeeTrackedHoursDto);
        Message<EmployeeTrackedHoursDto> message = MessageBuilder
                .withPayload(employeeTrackedHoursDto)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        kafkaProperties.getTopics().getEmailsTopic()
                )
                .build();
        kafkaTemplate.send(message);
    }
}


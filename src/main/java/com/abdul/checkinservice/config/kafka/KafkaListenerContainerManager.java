package com.abdul.checkinservice.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerContainerManager {

    public static final String RECORDING_SERVICE_LISTENER_ID = "recordingServiceListener";

    private final ObjectProvider<KafkaListenerEndpointRegistry> registryProvider;

    public KafkaListenerContainerManager(ObjectProvider<KafkaListenerEndpointRegistry> registryProvider) {
        this.registryProvider = registryProvider;
    }

    public void pauseRecordingServiceListener() {
        pauseListener(RECORDING_SERVICE_LISTENER_ID);
    }

    public void resumeRecordingServiceListener() {
        resumeListener(RECORDING_SERVICE_LISTENER_ID);
    }

    public void pauseListener(String listenerId) {
        MessageListenerContainer container = getMessageListenerContainer(listenerId);
        if (container != null && !container.isContainerPaused()) {
            log.info("Pausing Kafka listener: {}", listenerId);
            container.pause();
            return;
        }
        log.debug("Listener already paused: {}", listenerId);
    }

    public void resumeListener(String listenerId) {
        MessageListenerContainer container = getMessageListenerContainer(listenerId);
        if (container != null && container.isContainerPaused()) {
            log.info("Resuming Kafka listener: {}", listenerId);
            container.resume();
            return;
        }
        log.debug("Listener already running: {}", listenerId);
    }

    private MessageListenerContainer getMessageListenerContainer(String listenerId) {
        KafkaListenerEndpointRegistry registry = registryProvider.getIfAvailable();
        if (registry == null) {
            log.warn("KafkaListenerEndpointRegistry not available");
            return null;
        }
        MessageListenerContainer container = registry.getListenerContainer(listenerId);
        if (container == null) {
            log.warn("Listener container not found: {}", listenerId);
            return null;
        }
        return container;
    }
}

package com.abdul.checkinservice.domain.common.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListenerContainerManager {

    private final ObjectProvider<KafkaListenerEndpointRegistry> registryProvider;

    public void pauseListener(String listenerId) {
        MessageListenerContainer container = getMessageListenerContainer(listenerId);
        if (container != null && !container.isContainerPaused()) {
            log.info("Pausing Kafka listener: {}", listenerId);
            container.pause();
            return;
        }
        log.debug("Listener already paused or not found: {}", listenerId);
    }

    public void resumeListener(String listenerId) {
        MessageListenerContainer container = getMessageListenerContainer(listenerId);
        if (container != null && container.isContainerPaused()) {
            log.info("Resuming Kafka listener: {}", listenerId);
            container.resume();
            return;
        }
        log.debug("Listener already running or not found: {}", listenerId);
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

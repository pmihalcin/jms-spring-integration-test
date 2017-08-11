package com.example.jmsspringintegrationtest.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

/**
 * Created by patrik.mihalcin on 4.8.2017.
 */
@Configuration
@Slf4j
public class ErrorFlowConfig {

    @Bean
    public IntegrationFlow errorHandlingFlow() {
        return IntegrationFlows.from(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME)
                .handle(this::errorMessageHandler)
                .get();
    }

    private void errorMessageHandler(Message<?> msg) {
        log.warn("Handling error msg");
        log.warn("headers: {}", msg.getHeaders());
        log.warn("payload: {} ", msg.getPayload());
        MessagingException exception = (MessagingException) msg.getPayload();
        Message<?> failedMsg = exception.getFailedMessage();
        log.warn("original headers: {}", failedMsg.getHeaders());
        log.warn("original payload: {}", failedMsg.getPayload());
        log.warn("exception msg: {}", exception.getMessage());
        // make JMS broker redeliver
        throw exception;
    }
}

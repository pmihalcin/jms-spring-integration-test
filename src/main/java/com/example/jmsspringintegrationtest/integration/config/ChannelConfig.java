package com.example.jmsspringintegrationtest.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

/**
 * Created by patrik.mihalcin on 4.8.2017.
 */
@Configuration
public class ChannelConfig {

    @Bean
    public MessageChannel downstreamEventChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel errorChannel() {
        return MessageChannels.direct().get();
    }
}

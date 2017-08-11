package com.example.jmsspringintegrationtest.integration.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

    private static final String UPSTREAM_EVENT_QUEUE = "upstream.event.queue";
    private static final String DOWNSTREAM_EVENT_QUEUE = "downstream.event.queue";

    @Bean
    public Queue jmsUpstreamEventQueue() {
        return new ActiveMQQueue(UPSTREAM_EVENT_QUEUE);
    }

    @Bean
    public Queue jmsDownstreamEventQueue() {
        return new ActiveMQQueue(DOWNSTREAM_EVENT_QUEUE);
    }

}
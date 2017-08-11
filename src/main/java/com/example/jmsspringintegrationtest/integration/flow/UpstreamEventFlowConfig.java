package com.example.jmsspringintegrationtest.integration.flow;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import com.example.jmsspringintegrationtest.integration.config.ChannelConfig;
import com.example.jmsspringintegrationtest.integration.endpoints.EvaluationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.messaging.MessageChannel;

/**
 * Created by patrik.mihalcin on 4.8.2017.
 */
@Configuration
@Import({ChannelConfig.class})
public class UpstreamEventFlowConfig {
    private final ConnectionFactory jmsConnectionFactory;
    private final Queue jmsUpstreamEventQueue;
    private final Queue jmsDownstreamEventQueue;
    private final MessageChannel errorChannel;
    private final EvaluationHandler handler;

    @Autowired
    public UpstreamEventFlowConfig(ConnectionFactory jmsConnectionFactory,
                                   Queue jmsUpstreamEventQueue,
                                   Queue jmsDownstreamEventQueue,
                                   MessageChannel errorChannel,
                                   EvaluationHandler handler) {
        this.jmsUpstreamEventQueue = jmsUpstreamEventQueue;
        this.jmsConnectionFactory = jmsConnectionFactory;
        this.jmsDownstreamEventQueue = jmsDownstreamEventQueue;
        this.errorChannel = errorChannel;
        this.handler = handler;
    }

    @Bean
    public IntegrationFlow upstreamEventFlow() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(jmsConnectionFactory)
                        .destination(jmsUpstreamEventQueue)
                        .errorChannel(errorChannel)
                )
                .handle(handler)
                .handle(jmsSendingMessageHandler())
                .get();
    }

    @Bean
    public JmsSendingMessageHandler jmsSendingMessageHandler() {
        return Jms.outboundAdapter(jmsConnectionFactory)
                .destination(jmsDownstreamEventQueue)
                .get();
    }
}

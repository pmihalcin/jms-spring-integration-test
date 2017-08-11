package com.example.jmsspringintegrationtest;

import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import com.example.jmsspringintegrationtest.service.Car;
import com.example.jmsspringintegrationtest.service.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@AutoConfigureTestEntityManager
@Transactional
@SpringBootTest
public class ApplicationTests {

    //    @Autowired
//    private TestEntityManager testEntityManager;
    @Autowired
    CarRepository carRepository;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    Queue jmsUpstreamEventQueue;
    @Autowired
    Queue jmsDownstreamEventQueue;
    @Autowired
    ConnectionFactory jmsConnectionFactory;

    @Test
    public void contextLoads() {
        Car car = new Car("BMW");
        carRepository.save(car);
//        testEntityManager.persist(car);

        jmsTemplate.setMessageConverter(new SimpleMessageConverter());
        emptyQueue(jmsTemplate, jmsUpstreamEventQueue);
        emptyQueue(jmsTemplate, jmsDownstreamEventQueue);

        jmsTemplate.convertAndSend(jmsUpstreamEventQueue, "TEST");
        awaitQueueNonEmpty(jmsConnectionFactory, jmsDownstreamEventQueue, 10000L, 300L);

    }

    private static void emptyQueue(JmsTemplate jmsTemplate, Queue queue) {
        long receiveTimeout = jmsTemplate.getReceiveTimeout();
        jmsTemplate.setReceiveTimeout(1000);
        while (jmsTemplate.receive(queue) != null) {
            // just throw away the message
        }
        jmsTemplate.setReceiveTimeout(receiveTimeout);
    }

    private static void doWait(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("The execution has been interrupted", e);
        }
    }

    private static void awaitQueueNonEmpty(ConnectionFactory connectionFactory, Queue queue, long maxEmptyQueueWaitTimeMs, long timeoutMs) {
        long start = System.currentTimeMillis();
        while (!isQueueNonEmpty(connectionFactory, queue) && System.currentTimeMillis() - start < maxEmptyQueueWaitTimeMs) {
            doWait(timeoutMs);
        }
        if (!isQueueNonEmpty(connectionFactory, queue)) {
            try {
                fail("Queue empty: " + queue.getQueueName() + ". Total waiting time was ~ " + (double) ((System.currentTimeMillis() - start) / 1000) + " seconds");
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean isQueueNonEmpty(ConnectionFactory connectionFactory, Queue queue) {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            QueueBrowser browser = connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createBrowser(queue);
            connection.start();
            return browser.getEnumeration().hasMoreElements();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

# jms-spring-integration-test

This sample reproduces the issue with spring integration flow under test:

upstreamEventFlow
JMS upstream queue -> handler -> jmsSendingMessageHandler -> JMS downstream queue

When I use @Transactional on JUnit test, the data which is prepared in test by calling repo.save(...) 
is not visible during the execution of EvaluationServiceImpl which is used by handler (EvaluationHandler)

The easiest way to see the problem is to run ApplicationTests with `@Transactional` annotation and then without.
You will see in a log:
`All cars: []`
and
`All cars: [Car{id=1, name='BMW'}]`
respectively

What is wrong with my transaction boundaries?

How should I set them up accordingly?

How come the data is not visible?

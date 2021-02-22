package com.camel.demospringboot.Route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class jmsRoute extends RouteBuilder {
    @Value("${app.messageSource}")
    private String msgSource;
    @Value("${app.DestinationFile}")
    private String DestinationFile;
    @Override
    public void configure() throws Exception{
        consumeMessageFromAmq();
        produceMessagetoAmqUsingTimer();
        connectUsingDirect();
        readMsgfromFileMoveToMQ();
        moveMsgfromTofile();
    }

    private void moveMsgfromTofile() {
        from("activemq:queue:fileMessage")
                .log(LoggingLevel.INFO,"msg in queue of ActiveMq:${body}")
                .setHeader(Exchange.FILE_NAME,simple("${file:name.next.noext}_recevied_on_${date:now:yyyyMMdd}.${file:name.ext}"))
                .routeId("msg-to-file")
                  .to("file:" + DestinationFile)
                  .log(LoggingLevel.INFO,"file created at destination and msg is:${body}");


    }

    private void log(LoggingLevel info, String s) {
    }

    private void to(String s) {
    }

    private void readMsgfromFileMoveToMQ() {
        from("file:" + msgSource)
                .routeId("moving")
        .log(LoggingLevel.INFO,"existed file msg ${body}")
                  .to("file:" + DestinationFile)
                  .log(LoggingLevel.INFO,"msg moved to mq");


    }
    private void consumeMessageFromAmq() {
        from("activemq:queue:demo")
                .routeId("myroute")
                .log(LoggingLevel.INFO,"incoming msg ${body}");
    }
    private void produceMessagetoAmqUsingTimer() {
        from("timer:mytimer?period=5000")
                .routeId("timer-route")
                .setBody(constant("HELLO from camel"))
                .to("direct:messageproducer");
    }

    private void connectUsingDirect() {
        from("direct:messageproducer")
                .routeId("using-direct")
                .to("activemq:queue:demo");
    }




}

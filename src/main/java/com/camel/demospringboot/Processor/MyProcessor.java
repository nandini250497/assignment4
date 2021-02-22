package com.camel.demospringboot.Processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyProcessor implements Processor {
    private static Logger logger= LogManager.getLogger(MyProcessor.class);
    @Override
    public void process(Exchange exchange)throws Exception{
        String messageBody =exchange.getIn().getBody(String.class);
        Map<String, Object> exchangeProperties = exchange.getProperties();
        logger.info("message body from incoming exchange,{}",messageBody);
        logger.info("exchange properties,{}",exchangeProperties);
        messageBody=messageBody.concat("updating body using processor");
        logger.info("updated msg body exchnage,{}",messageBody);
        exchange.getIn().setBody(messageBody);
    }
}

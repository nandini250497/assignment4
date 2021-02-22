package com.camel.demospringboot.Route;

import com.camel.demospringboot.Processor.MyProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
//import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NewFile extends RouteBuilder {
    @Value("${app.source}")
    private String sourceLocation;
    @Value("${app.destination}")
    private String destination;
    @Autowired
   // private MyProcessor processor;
    private MyProcessor Processor;

    @Override
    public void configure() throws Exception{
        renameTheFile();
    }

    private void renameTheFile() {
        from("file:" + sourceLocation)
                .routeId("renamedfile")
                .log(LoggingLevel.INFO,"existed file name ${file:name}")
              //  .setHeader(Exchange.FILE_NAME,simple("${file:name.noext}_modified.$[file:name.ext}"))
                .setHeader(Exchange.FILE_NAME,simple("${file:name.noext}_${date:now:yyyyMMdd}.${file:name.ext}"))
                //.setHeader(Exchange.FILE_NAME,simple("${fileName:nandu}"))
                .log(LoggingLevel.INFO,"renamed file ${file:name}")
                .to("file:" + destination );
    }
    private void fileCopier(){
        from("file:" + sourceLocation)
                .routeId("demo-route-ID")
                .process(Processor)
                .to("file:" + destination)
                .log(LoggingLevel.INFO,"file moved to destination \n ${body}");
    }


}

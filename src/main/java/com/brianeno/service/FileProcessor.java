package com.brianeno.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.time.LocalDateTime;

public class FileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileContents = exchange.getMessage().getBody(String.class); // message body contains filename
        String fileName = (String) exchange.getMessage().getHeader("CamelFilePath");
        fileContents += LocalDateTime.now().toString();
        long newLen = fileContents.length();
        exchange.getMessage().setHeader("CamelFileLength", newLen);
        exchange.getMessage().setBody(fileContents, String.class);
    }
}

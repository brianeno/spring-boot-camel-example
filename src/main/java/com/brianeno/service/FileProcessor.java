package com.brianeno.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.File;
import java.io.FileOutputStream;

public class FileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileContents = exchange.getIn().getBody(String.class); // message body contains filename
        String fileName = (String) exchange.getIn().getHeader("CamelFilePath");
        fileContents += " added content";
        /*File myFoo = new File(fileName);
        FileOutputStream fooStream = new FileOutputStream(myFoo, false); // true to append
        // false to overwrite.
        byte[] myBytes = fileContents.getBytes();
        fooStream.write(myBytes);
        fooStream.close();*/
        long newLen = fileContents.length();
        exchange.getIn().setHeader("CamelFileLength", newLen);
        exchange.getIn().setBody(fileContents, String.class);
    }
}

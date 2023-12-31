package com.brianeno.route;

import com.brianeno.service.FileProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileToS3Route extends RouteBuilder {

    public FileToS3Route(@Autowired CamelContext context) {
        super(context);
    }

    @Override
    public void configure() throws Exception {
        from("{{from.component}}:{{from.endpoint}}").routeId("camelS3Route")
                .log("${in.header.CamelFileNameOnly} to be uploaded to S3 {{awsS3BucketName}} bucket")
                .log("Before content length: ${in.header.CamelFileLength}")
                .process(new FileProcessor())
                .log("After content length: ${in.header.CamelFileLength}")
                .setHeader(S3Constants.CONTENT_LENGTH, simple("${in.header.CamelFileLength}"))
                //This is the filename on the S3 bucket
                .setHeader(S3Constants.KEY, simple("${in.header.CamelFileNameOnly}" + "-${date:now:yyyyMMddHH-mm-ss}"))
                //aws-s3 the camel component and that is how you tell camel where you want to upload.
                .to("aws-s3://{{awsS3BucketName}}?deleteAfterWrite=false&region={{awsRegion}}&accessKey={{awsAccessKey}}&secretKey=RAW({{awsAccessSecretKey}})")
                //optionally logging success message
                .log("${in.header.CamelFileNameOnly} successfully uploaded to S3 {{awsS3BucketName}} bucket");
    }
}

package com.koreanair.common.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AwsS3Config {
 
    public AmazonS3Client amazonS3Client() {
    	String accessKey = "123456";
    	String secretKey = "123456"; 
    	//Regions region = Regions.AP_NORTHEAST_2;
    	Regions region = Regions.US_EAST_1;
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
									                .withRegion(region)
									                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
									                .build();
    }
}
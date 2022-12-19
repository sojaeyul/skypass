package com.koreanair.common.aws;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class AwsS3Uploader {
	private final static Logger log = LoggerFactory.getLogger(AwsS3Uploader.class);
    private final AmazonS3Client amazonS3Client;
    public String bucket = "bucket";
    
    public AwsS3Uploader() {
    	AwsS3Config config = new AwsS3Config();
    	amazonS3Client = config.amazonS3Client();
    }
    /*
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)        // 파일 생성
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

        return upload(uploadFile, dirName);
    }



    // 1. 로컬에 파일생성
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
    */
    
    private String upload(File uploadFile, String dirName) throws Exception {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);    // s3로 업로드
        return uploadImageUrl;
    }

    // 2. S3에 파일업로드
    private String putS3(File uploadFile, String fileName) throws Exception {
    	
    	PutObjectRequest putOb = new PutObjectRequest(bucket, fileName,uploadFile)
    			.withCannedAcl(CannedAccessControlList.PublicRead);
    	
    	
        amazonS3Client.putObject(putOb);

        String s3Url = amazonS3Client.getUrl(bucket, fileName).toString();
        return s3Url;
    }

    // 2. S3에 파일업로드
    private String putS3(String uploadFilePath, String fileName) throws Exception {    
    	
    	Path pth= Paths.get(uploadFilePath);
    	
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.setContentType(fileName);
    	metadata.setContentLength(0);
    	    	
    	PutObjectRequest putOb = new PutObjectRequest(bucket, fileName,new FileInputStream(pth.toFile()), metadata)
    			.withCannedAcl(CannedAccessControlList.PublicRead);

    	
        amazonS3Client.putObject(putOb);

        String s3Url = amazonS3Client.getUrl(bucket, fileName).toString();
        return s3Url;
    }
    
    public void getObject(String storedFileName, String downloadFilePath ) throws IOException {
    	
    	OutputStream outputStream =null;
    	S3ObjectInputStream s3ObjectInputStream=null;
    	try {
	        S3Object s3Object = amazonS3Client.getObject(bucket, storedFileName);
	        s3ObjectInputStream = s3Object.getObjectContent();
	
	        outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath));
	        byte[] bytesArray = new byte[4096];
	        int bytesRead = -1;
	        while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
	            outputStream.write(bytesArray, 0, bytesRead);
	        }

    	}finally {
            outputStream.close();
            s3ObjectInputStream.close();
    	}
 
    }
    
    //S3에 파일삭제
    public void delete(String fileName) {
    	log.debug("File Delete : " + fileName);
        amazonS3Client.deleteObject(bucket, fileName);
    }
    
    
	public static void main(String[] args) throws Exception {
		AwsS3Uploader service = new AwsS3Uploader();
		
		service.putS3("D:\\test.json","test.json");
	}
}
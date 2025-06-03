package com.abrhernandez.meeting_golden.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${aws.s3.bucket.name}")
    private  String bucketName;
    private final AmazonS3 amazonS3;

    // Upload file to S3 bucket
    public String uploadFile(MultipartFile file) {
        try {
            String s3FileName = file.getOriginalFilename();
            String region = amazonS3.getRegionName();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");
            objectMetadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata));
            log.info("Image successfully saved to S3, size: "+file.getSize());
            return "https://"+bucketName+".s3."+region+".amazonaws.com/"+s3FileName;
        } catch (Exception e) {
            log.error("Error uploading file : "+e.getMessage());
            return "";
        }
    }
}

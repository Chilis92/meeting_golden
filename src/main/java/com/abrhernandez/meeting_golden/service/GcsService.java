package com.abrhernandez.meeting_golden.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GcsService {

    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    public void deleteFile(String imageURL) {
        if (imageURL == null || imageURL.isBlank()) return;
        try {
            String prefix = String.format("https://storage.googleapis.com/%s/", bucketName);
            String fileName = imageURL.replace(prefix, "");
            storage.delete(BlobId.of(bucketName, fileName));
            log.info("File deleted from GCS: {}", fileName);
        } catch (Exception e) {
            log.error("Failed to delete file from GCS: {}", e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();
            storage.create(blobInfo, file.getBytes());
            String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
            log.info("File uploaded to GCS: {}", publicUrl);
            return publicUrl;
        } catch (IOException e) {
            log.error("Failed to upload file to GCS: {}", e.getMessage());
            return "";
        }
    }
}

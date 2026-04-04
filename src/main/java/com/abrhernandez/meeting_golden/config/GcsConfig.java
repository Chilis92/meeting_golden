package com.abrhernandez.meeting_golden.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GcsConfig {

    @Value("${gcs.credentials.path:}")
    private String credentialsPath;

    @Value("${gcs.project.id}")
    private String projectId;

    @Bean
    public Storage googleCloudStorage() throws IOException {
        StorageOptions.Builder builder = StorageOptions.newBuilder().setProjectId(projectId);
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            builder.setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)));
        } else {
            builder.setCredentials(GoogleCredentials.getApplicationDefault());
        }
        return builder.build().getService();
    }
}

package com.portfolio.config;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.US_EAST_1) // Update this with your desired region
            .build();
    }
}

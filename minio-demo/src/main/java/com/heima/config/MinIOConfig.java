package com.heima.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinIOProperties.class)
public class MinIOConfig {

    @Bean
    public MinioClient minioClient(MinIOProperties minIOProperties){
        MinioClient minioClient = new MinioClient(minIOProperties.getEndpoint(), minIOProperties.getAccessKey(), minIOProperties.getSecretKey());
        return minioClient;
    }
}

package com.heima.common.minio;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "minio", name = "endpoint")
@EnableConfigurationProperties(MinIOProperties.class)
public class MinIOConfig {

    @Bean
    public MinioClient minioClient(MinIOProperties minIOProperties){
        MinioClient minioClient = new MinioClient(minIOProperties.getEndpoint(), minIOProperties.getAccessKey(), minIOProperties.getSecretKey());
        return minioClient;
    }
    @Bean
    public MinIOService minIOService(MinioClient minioClient,  MinIOProperties minIOProperties){

         return new MinIOService(minioClient,minIOProperties);

    }


}

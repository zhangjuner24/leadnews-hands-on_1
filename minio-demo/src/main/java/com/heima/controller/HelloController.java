package com.heima.controller;

import com.heima.config.MinIOProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIOProperties properties;

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file) throws Exception{

        PutObjectArgs args = PutObjectArgs.builder()
                .object(file.getOriginalFilename())
                .contentType(file.getContentType())
                .stream(file.getInputStream(),file.getSize(),-1)
                .bucket(properties.getBucketName()).build()
                ;

        // 1、文件名  2、mime类型   3、文件大小 4、文件流 5、桶名
        minioClient.putObject(args);

        return  properties.getEndpoint()+"/"+properties.getBucketName()+"/"+file.getOriginalFilename();  //文件在minio的url
// http://192.168.85.143:9000/heima/0401f22d-f83f-4b39-b9f6-c52e5df97eef.jpeg

    }

}

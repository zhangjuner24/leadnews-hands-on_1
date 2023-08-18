package com.heima.common.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

// @Service
public class MinIOService {
    // @Autowired
    private MinioClient minioClient;

    // @Autowired
    private MinIOProperties properties;


    public  MinIOService(){}

    public  MinIOService(MinioClient minioClient, MinIOProperties properties){
        this.minioClient = minioClient;
        this.properties = properties;
    }
    /**
     * 上传文件
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        // 原文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件的后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 构造新的文件名，名字不重复
        String objectName = UUID.randomUUID().toString() + suffix;

        try {
            ClassPathResource classPathResource = new ClassPathResource("/shui.jpg"); //自己在resources下放一个图片作为水印图片
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Thumbnails.of(file.getInputStream()).scale(0.1f) //10倍的压缩  740  490
            Thumbnails.of(file.getInputStream())
                    .size(740,480) // 缩放大小 width:740  height:480
                   // .scale(0.1f) //10倍的压缩
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(classPathResource.getFile()),0.5f) //水印位于右下角,半透明
                    .toOutputStream(outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray());

        // 上传文件

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .contentType(file.getContentType())
                    .stream(inputStream,  outputStream.toByteArray().length, -1)
                    .bucket(properties.getBucketName())
                    .object(objectName)
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败: " + e.getMessage());
        }
        return properties.getEndpoint() + "/" + properties.getBucketName() + "/" + objectName;
    }
/**
     * 上传文件
     * @param name
     * @param inputStream
     * @param contentType
     * @return
     */
    public String upload(String name, InputStream inputStream, String contentType){
        // 上传文件
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .bucket(properties.getBucketName())
                    .object(name)
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败: " + e.getMessage());
        }
        return properties.getEndpoint() + "/" + properties.getBucketName() + "/" + name;
    }
    /**
     * 删除文件
     * @param url
     */
    public void delete(String url){
        String objectName = url.replace(properties.getEndpoint()+"/","")
                .replace(properties.getBucketName()+"/","");
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(properties.getBucketName())
                .object(objectName)
                .build();
        try {
            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage());
        }
    }
     /**
     * 下载文件
     * @param url
     * @return
     */
    public InputStream download(String url){
        String objectName = url.replace(properties.getEndpoint()+"/","")
                .replace(properties.getBucketName()+"/","");
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(properties.getBucketName())
                .object(objectName)
                .build();
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(args);
        } catch (Exception e) {
            throw new RuntimeException("下载文件失败: " + e.getMessage());
        }
        return inputStream;
    }
}

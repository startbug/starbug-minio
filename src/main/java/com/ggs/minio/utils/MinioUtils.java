package com.ggs.minio.utils;

import com.ggs.minio.config.MinioProperties;
import com.ggs.minio.entity.UploadResponse;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Author lianghaohui
 * @Date 2022/6/23 15:53
 * @Description
 */
@Slf4j
@Component
public class MinioUtils {

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;

    private final Long maxSize = Long.valueOf(1024 * 1024);

    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 上传文件
     */
    public UploadResponse uploadFile(MultipartFile file, String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 非空判断
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 判断存储桶是否存在，不存在则创建
        createBucket(bucketName);

        // 文件名
        String originalFilename = file.getOriginalFilename();

        // 新文件名=时间戳_随机数.后缀名
        if (StringUtils.isBlank(originalFilename)) {
            log.info("需要指定文件名称");
            return null;
        }

        long now = System.currentTimeMillis() / 1000;
        String fileName = new DateTime().toString("yyyyMMdd") + "_" + now + "_" + new Random().nextInt(1000) + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 开始上传
        log.info("file压缩前大小:{}", file.getSize());

        if (file.getSize() > maxSize) {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            FileItem fileItem = diskFileItemFactory.createItem(fileName, "text/plain", true, fileName);
            OutputStream os = fileItem.getOutputStream();
            Thumbnails.of(file.getInputStream())
                    .scale(1f)
                    .outputFormat(originalFilename.substring(originalFilename.lastIndexOf(".") + 1))
                    .outputQuality(0.25f).toOutputStream(os);
            file = new CommonsMultipartFile(fileItem);
        }

        log.info("file压缩后大小:{}", file.getSize());

        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType()).build());

        String url = minioProperties.getEndpoint() + "/" + bucketName + "/" + fileName;
        String urlHost = minioProperties.getNginxHost() + "/" + bucketName + "/" + fileName;
        return new UploadResponse(url, urlHost);
    }

}

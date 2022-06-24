package com.ggs.minio.controller;

import com.ggs.minio.entity.R;
import com.ggs.minio.entity.UploadResponse;
import com.ggs.minio.utils.MinioUtils;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author lianghaohui
 * @Date 2022/6/24 13:19
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioUtils minioUtils;

    /**
     * @param file 文件
     * @author lianghaohui
     * @date 2022/6/24 13:23
     * @Description 上传文件
     */
    @PostMapping("/upload")
    public R minioUpload(@RequestParam(value = "file") MultipartFile file) {
        UploadResponse response;
        try {
            response = minioUtils.uploadFile(file, "starbug-first-bucket");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败");
            return R.failed("上传失败");
        }
        return R.ok(response);
    }

}

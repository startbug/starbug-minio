package com.ggs.minio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lianghaohui
 * @Date 2022/6/23 15:53
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {

    private String minIoUrl;

    private String nginxUrl;

}

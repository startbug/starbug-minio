package com.ggs.minio;

import com.ggs.minio.config.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {MinioProperties.class})
public class StarbugMinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarbugMinioApplication.class, args);
    }

}

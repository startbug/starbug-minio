package com.ggs.minio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author lianghaohui
 * @Date 2022/6/24 13:20
 * @Description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class R {

    private String message;

    private String code;

    private Object data;

    public static R ok() {
        return new R().code("200").message("操作成功");
    }

    public static R ok(Object data) {
        return new R().code("200").message("操作成功").data(data);
    }

    public static R failed() {
        return new R().code("500").message("操作失败");
    }

    public static R failed(String message) {
        return new R().code("500").message(message);
    }

    public static R failed(String code, String message) {
        return new R().code(code).message(message);
    }

    public R data(Object data) {
        this.data = data;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R code(String code) {
        this.code = code;
        return this;
    }

}

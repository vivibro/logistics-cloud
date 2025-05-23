package com.logistics.cloud.common.core.web;

import java.io.Serializable;

/**
 * 统一响应对象
 * 用于所有后端接口的标准返回格式，便于前后端协作和全局异常处理。
 * 扩展字段：traceId、timestamp、path，便于后期排查问题。
 * @param <T> 业务数据类型
 */
public class R<T> implements Serializable {
    /** 状态码，200表示成功，其他为业务错误码 */
    private Integer code;
    /** 提示信息 */
    private String message;
    /** 业务数据 */
    private T data;
    /** 链路追踪ID，便于分布式追踪 */
    private String traceId;
    /** 响应时间戳，毫秒值 */
    private Long timestamp;
    /** 请求路径，便于定位接口 */
    private String path;

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public R(Integer code, String message, T data, String traceId, String path) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = traceId;
        this.path = path;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应，带数据
     */
    public static <T> R<T> success(T data) {
        return new R<>(200, "success", data);
    }

    /**
     * 成功响应，无数据
     */
    public static <T> R<T> success() {
        return new R<>(200, "success", null);
    }

    /**
     * 失败响应，带自定义消息
     */
    public static <T> R<T> fail(String message) {
        return new R<>(500, message, null);
    }

    /**
     * 失败响应，带自定义状态码和消息
     */
    public static <T> R<T> fail(Integer code, String message) {
        return new R<>(code, message, null);
    }

    // Getter 和 Setter
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getTraceId() {
        return traceId;
    }
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
} 
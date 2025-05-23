package com.logistics.cloud.common.core.web;

import org.slf4j.MDC;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 捕获所有Controller层抛出的异常，统一返回R对象，便于前后端协作和排查问题。
 * 自动补充traceId、path等信息。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception ex, HttpServletRequest request) {
        // 获取traceId（如集成Sleuth可自动注入，否则可用MDC或自定义）
        String traceId = MDC.get("traceId");
        String path = request.getRequestURI();
        // 记录异常日志（可选）
        ex.printStackTrace();
        return buildFailResponse(500, "服务器内部错误", traceId, path);
    }

    /**
     * 处理参数校验异常（如@Valid失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String path = request.getRequestURI();
        String msg = ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage() : "参数校验失败";
        return buildFailResponse(400, msg, traceId, path);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String path = request.getRequestURI();
        String msg = ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage() : "参数绑定失败";
        return buildFailResponse(400, msg, traceId, path);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<Void> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String path = request.getRequestURI();
        String msg = "缺少请求参数: " + ex.getParameterName();
        return buildFailResponse(400, msg, traceId, path);
    }

    /**
     * 处理请求体不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String path = request.getRequestURI();
        return buildFailResponse(400, "请求体格式错误", traceId, path);
    }

    /**
     * 构建统一失败响应
     */
    private R<Void> buildFailResponse(int code, String message, String traceId, String path) {
        R<Void> r = new R<>(code, message, null, traceId, path);
        return r;
    }
} 
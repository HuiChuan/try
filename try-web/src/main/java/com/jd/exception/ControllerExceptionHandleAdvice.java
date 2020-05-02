package com.jd.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandleAdvice {

    @ResponseBody
    @ExceptionHandler
    public String handler(HttpServletRequest req, HttpServletResponse res, Exception e) {

        if (res.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            log.info("[SERVICE_INFO]400", e);
            res.setStatus(HttpStatus.OK.value());
        }

        if (e instanceof NullPointerException) {
            log.error("[SERVICE_ERROR]空指针异常", e);
            return "空指针异常";
        } else if (e instanceof IllegalArgumentException) {
            log.error("[SERVICE_ERROR]请求参数类型不匹配", e);
            return "请求参数类型不匹配";
        } else if (e instanceof SQLException) {
            log.error("[SERVICE_ERROR]数据库访问异常", e);
            return "数据库访问异常";
        } else if (e instanceof MissingServletRequestParameterException) {
            log.error("[SERVICE_ERROR]缺少参数异常", e);
            return "缺少参数异常";
        } else {
            log.error("[SERVICE_ERROR]服务器内部错误", e);
            return "服务器内部错误";
        }
    }
}
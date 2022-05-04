package io.vitalir.vitalirspring.common.errors;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

public class SwaggerErrorAttributes implements ErrorAttributes {

    public long timestamp;
    public int status;
    public String error;
    public String exception;
    public String message;
    public String path;

    public SwaggerErrorAttributes(long timestamp, int status, String error, String exception, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.exception = exception;
        this.message = message;
        this.path = path;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        return ErrorAttributes.super.getErrorAttributes(webRequest, options);
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }
}

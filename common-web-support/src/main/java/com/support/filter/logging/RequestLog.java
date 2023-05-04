package com.support.filter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class RequestLog {

    private final ContentCachingRequestWrapper httpServletRequest;
    private final ObjectMapper objectMapper;
    private final String REQUEST_PREFIX = "Request <<<";
    private final String SPACE = " ";

    public RequestLog(ContentCachingRequestWrapper httpServletRequest, ObjectMapper objectMapper) {
        this.httpServletRequest = httpServletRequest;
        this.objectMapper = objectMapper;
    }

    public void logRequest(String traceId) throws IOException {
        this.logStartLine(traceId);
        this.logHeaders(traceId);
        this.logBody(traceId);
    }

    private void logStartLine(String traceId) {
        String startLine = httpServletRequest.getMethod() + this.SPACE + httpServletRequest.getRequestURI();

        String queryString = httpServletRequest.getQueryString();
        if (!StringUtils.isNullOrEmpty(queryString)) {
            startLine = startLine + this.SPACE + queryString;
        }

        log.info("[{}] {} {}", traceId, this.REQUEST_PREFIX, startLine);
    }

    private void logHeaders(String traceId) {
        List<String> headerNames = Collections.list(httpServletRequest.getHeaderNames());
        if (headerNames.isEmpty()) {
            return;
        }

        headerNames.forEach(headerName -> {
            String headerValue = httpServletRequest.getHeader(headerName);
            log.info("[{}] {} Header : {} = {}", traceId, this.REQUEST_PREFIX, headerName, headerValue);
        });
    }

    private void logBody(String traceId) throws IOException {
        byte[] requestBody = this.httpServletRequest.getContentAsByteArray();
        if (requestBody.length == 0) {
            return;
        }

        log.info("[{}] {} Body : {}", traceId, this.REQUEST_PREFIX, objectMapper.readTree(requestBody));
    }

}

package com.support.filter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResponseLog {

    private final ContentCachingResponseWrapper httpServletResponse;
    private final ObjectMapper objectMapper;
    private final String RESPONSE_PREFIX = "Response >>>";

    public ResponseLog(ContentCachingResponseWrapper httpServletResponse, ObjectMapper objectMapper) {
        this.httpServletResponse = httpServletResponse;
        this.objectMapper = objectMapper;
    }

    public void logResponse(String traceId) throws IOException {
        this.logStartLine(traceId);
        this.logHeaders(traceId);
        this.logBody(traceId);
    }

    private void logStartLine(String traceId) {
        int statusCode = httpServletResponse.getStatus();
        log.info("[{}] {} StatusCode : {}", traceId, this.RESPONSE_PREFIX, statusCode);
    }

    private void logHeaders(String traceId) {
        List<String> headerNames = new ArrayList<>(httpServletResponse.getHeaderNames());
        if (headerNames.isEmpty()) {
            return;
        }

        headerNames.forEach(headerName -> {
            String headerValue = httpServletResponse.getHeader(headerName);
            log.info("[{}] {} Header : {} = {}", traceId, this.RESPONSE_PREFIX, headerName, headerValue);
        });
    }

    private void logBody(String traceId) throws IOException {
        byte[] responseBody = this.httpServletResponse.getContentAsByteArray();
        if (responseBody.length == 0) {
            return;
        }

        log.info("[{}] {} Body : {}", traceId, this.RESPONSE_PREFIX, objectMapper.readTree(responseBody));
    }

}

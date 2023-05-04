package com.support.filter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

public class HttpLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public HttpLoggingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String traceId = UUID.randomUUID().toString();
        RequestLog requestLog = new RequestLog(requestWrapper, objectMapper);
        requestLog.logRequest(traceId);

        ResponseLog responseLog = new ResponseLog(responseWrapper, objectMapper);
        responseLog.logResponse(traceId);
        responseWrapper.copyBodyToResponse();
    }
}

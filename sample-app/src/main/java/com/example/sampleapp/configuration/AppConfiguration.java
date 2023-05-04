package com.example.sampleapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.filter.logging.HttpLoggingFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public Filter LoggingFilter() {
        return new HttpLoggingFilter(this.objectMapper);
    }

}

package com.support.jwt;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
public class JsonWebTokenConfiguration {

    public static String PREFIX_KEY = "prefix";
    public static String SECRET_KEY = "secret";
    public static String SUBJECT_KEY = "subject";
    public static String ISSUER_KEY = "issuer";
    public static String EXPIRATION_KEY = "expiration";

    public static String REFRESH_EXPIRATION_KEY = "refresh-expiration";

    @Setter
    @Component
    @ConfigurationProperties("auth.jwt")
    public static class JsonWebTokenSetting {
        private Map<String, String> setting;

        public String getSettingValue(String key) {
            return this.setting.get(key);
        }

        public void changeSettingValue(String key, String value) {
            this.setting.put(key, value);
        }
    }

}

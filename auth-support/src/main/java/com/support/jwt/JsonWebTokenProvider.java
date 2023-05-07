package com.support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonWebTokenProvider {

    private final Integer VALID_TOKEN_PARTS_COUNT = 2;
    private final String SPACE = " ";
    private final JsonWebTokenConfiguration.JsonWebTokenSetting jsonWebTokenSetting;

    private JWTVerifier jwtVerifier;
    private Algorithm algorithm;

    public JsonWebToken generateJsonWebToken(String audienceUniqueKey) {
        LocalDateTime now = LocalDateTime.now();
        String accessToken = this.generateToken(audienceUniqueKey, Integer.parseInt(jsonWebTokenSetting.getSettingValue(JsonWebTokenConfiguration.EXPIRATION_KEY)), now);
        String refreshToken = this.generateToken(audienceUniqueKey, Integer.parseInt(jsonWebTokenSetting.getSettingValue(JsonWebTokenConfiguration.REFRESH_EXPIRATION_KEY)), now);

        return new JsonWebToken(accessToken, refreshToken);
    }

    public boolean isValidToken(String token) {
        String extractedToken = this.extractToken(token);
        try {
            jwtVerifier.verify(extractedToken);
        } catch (TokenExpiredException tokenExpiredException) {
            throw new JsonWebTokenExpiredException();
        } catch (Exception exception) {
            return false;
        }

        return true;
    }

    public String decodeToken(String token) {
        String extractedToken = this.extractToken(token);
        DecodedJWT decodedJWT = jwtVerifier.verify(extractedToken);

        String AUDIENCE_DELIMITER = ",";
        return String.join(AUDIENCE_DELIMITER, decodedJWT.getAudience());
    }

    @PostConstruct
    private void init() {
        algorithm = Algorithm.HMAC512(jsonWebTokenSetting.getSettingValue(JsonWebTokenConfiguration.SECRET_KEY));
        jwtVerifier = JWT.require(algorithm).build();
    }

    private String generateToken(String audienceUniqueKey, int expiration, LocalDateTime now) {
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withSubject(jsonWebTokenSetting.getSettingValue(JsonWebTokenConfiguration.SUBJECT_KEY))
                .withClaim(RegisteredClaims.ISSUER, jsonWebTokenSetting.getSettingValue(JsonWebTokenConfiguration.ISSUER_KEY))
                .withClaim(RegisteredClaims.AUDIENCE, audienceUniqueKey);

        Date expiredAt = Timestamp.valueOf(now.plusSeconds(expiration));
        jwtBuilder.withExpiresAt(expiredAt);

        return jwtBuilder.sign(algorithm);
    }

    private String extractToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException();
        }

        String[] tokenParts = token.split(this.SPACE);
        this.validateTokenFormat(tokenParts);

        return tokenParts[1];
    }

    private void validateTokenFormat(String[] tokenParts) {
        if (tokenParts.length != this.VALID_TOKEN_PARTS_COUNT) {
            throw new IllegalArgumentException();
        }

        String expectedAuthenticationType = jsonWebTokenSetting.getSettingValue(JsonWebTokenConfiguration.PREFIX_KEY);
        String actualAuthenticationType = tokenParts[0];
        if (!expectedAuthenticationType.equals(actualAuthenticationType)) {
            throw new IllegalArgumentException();
        }
    }

}

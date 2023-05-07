package com.example.sampleapp;

import com.support.jwt.JsonWebToken;
import com.support.jwt.JsonWebTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JsonWebTokenProvider jsonWebTokenProvider;

    @PostMapping("/access-token")
    public JsonWebToken accessToken(@RequestParam String userId) {
        JsonWebToken jsonWebToken = this.jsonWebTokenProvider.generateJsonWebToken(userId);
        System.out.println("accessToken >>> " + jsonWebToken.accessToken());
        System.out.println("refreshToken >>> " + jsonWebToken.refreshToken());

        return jsonWebToken;
    }

    @GetMapping("/index")
    public String validateToken(@RequestHeader HttpHeaders httpHeaders) {
        String token = httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0);

        boolean isValidToken = false;

        try {
            isValidToken = this.jsonWebTokenProvider.isValidToken(token);
        } catch (Exception exception) {
            return "invalid token";
        }

        return this.jsonWebTokenProvider.decodeToken(token);
    }

}

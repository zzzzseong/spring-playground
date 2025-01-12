package me.jisung.springplayground.common.component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import me.jisung.springplayground.common.constant.SecurityConst;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt_secret_key}")
    private String secretString;

    @Value("${jwt_access_token_expiration}")
    private int accessTokenExp;

    @Value("${jwt_refresh_token_expiration}")
    private int refreshTokenExp;

    SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }

    public String generateAccessToken(String email) {
        Date now = new Date();

        String accessToken = Jwts.builder()
            .signWith(secretKey)
            .subject(email)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + accessTokenExp))
            .compact();

        return SecurityConst.BEARER_PREFIX + accessToken;
    }

    public String getAccessToken(String bearerToken) {
        if(!bearerToken.startsWith(SecurityConst.BEARER_PREFIX)) {
            throw new ApiException(Api4xxErrorCode.INVALID_AUTHORIZATION_HEADER);
        }

        String accessToken = bearerToken.substring(SecurityConst.BEARER_PREFIX.length());
        validateToken(accessToken);

        return accessToken;
    }

    public String getEmail(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (Exception e) {
            throw new ApiException(e, Api4xxErrorCode.INVALID_JSON_WEB_TOKEN);
        }
    }
}

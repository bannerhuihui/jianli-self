package com.aitalentagent.api.auth;

import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.config.AppProperties;
import com.aitalentagent.api.domain.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenService {

    private final AppProperties appProperties;
    private final SecretKey secretKey;

    public TokenService(AppProperties appProperties) {
        this.appProperties = appProperties;
        byte[] keyBytes = appProperties.getAuth().getJwtSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("app.auth.jwt-secret must be at least 32 characters");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(UserAccount user, String sessionId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(appProperties.getAuth().getAccessTokenTtlSeconds());
        return Jwts.builder()
                .subject(user.getId())
                .claim("sid", sessionId)
                .claim("role", user.getRole())
                .claim("authProvider", user.getAuthProvider())
                .claim("entitlements", user.getEntitlements())
                .claim("bindings", bindings(user))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String userId, String sessionId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(appProperties.getAuth().getRefreshTokenTtlSeconds());
        return Jwts.builder()
                .subject(userId)
                .claim("sid", sessionId)
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public UserPrincipal parseAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if ("refresh".equals(claims.get("type"))) {
                throw new ApiException("AUTH_TOKEN_INVALID", "Token 无效", HttpStatus.UNAUTHORIZED);
            }
            @SuppressWarnings("unchecked")
            List<String> entitlements = (List<String>) claims.get("entitlements", List.class);
            return new UserPrincipal(
                    claims.getSubject(),
                    claims.get("sid", String.class),
                    claims.get("role", String.class),
                    claims.get("authProvider", String.class),
                    entitlements == null ? List.of() : entitlements
            );
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException("AUTH_TOKEN_INVALID", "Token 无效或已过期", HttpStatus.UNAUTHORIZED);
        }
    }

    public RefreshClaims parseRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (!"refresh".equals(claims.get("type"))) {
                throw new ApiException("AUTH_TOKEN_INVALID", "Refresh Token 无效", HttpStatus.UNAUTHORIZED);
            }
            return new RefreshClaims(claims.getSubject(), claims.get("sid", String.class));
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException("AUTH_TOKEN_INVALID", "Refresh Token 无效或已过期", HttpStatus.UNAUTHORIZED);
        }
    }

    public record RefreshClaims(String userId, String sessionId) {
    }

    private Map<String, String> bindings(UserAccount user) {
        Map<String, String> bindings = new HashMap<>();
        bindings.put("wechatMpOpenId", user.getWechatMpOpenId());
        bindings.put("wechatOaOpenId", user.getWechatOaOpenId());
        bindings.put("unionId", user.getUnionId());
        return bindings;
    }
}

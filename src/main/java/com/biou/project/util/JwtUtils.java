package com.biou.project.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT token工具类
 * 
 * @author Jax
 * @since 2024-01-01
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:biou-project-default-secret-key-for-jwt-token-generation}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800}")
    private Long refreshExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            byte[] expandedKey = new byte[32];
            System.arraycopy(keyBytes, 0, expandedKey, 0, keyBytes.length);
            for (int i = keyBytes.length; i < 32; i++) {
                expandedKey[i] = (byte) (keyBytes[i % keyBytes.length] ^ i);
            }
            keyBytes = expandedKey;
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成访问token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT token
     */
    public String generateAccessToken(Long userId, String username) {
        return generateToken(userId, username, expiration * 1000, "access");
    }

    /**
     * 生成刷新token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT refresh token
     */
    public String generateRefreshToken(Long userId, String username) {
        return generateToken(userId, username, refreshExpiration * 1000, "refresh");
    }

    /**
     * 生成token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param expiration 过期时间（毫秒）
     * @param tokenType token类型
     * @return JWT token
     */
    private String generateToken(Long userId, String username, Long expiration, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("tokenType", tokenType);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从token中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 从token中获取token类型
     *
     * @param token JWT token
     * @return token类型
     */
    public String getTokenTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("tokenType", String.class);
    }

    /**
     * 获取token过期时间
     *
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 从token中获取Claims
     *
     * @param token JWT token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * 验证token是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("JWT token is unsupported", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("JWT token is malformed", e);
        } catch (SecurityException e) {
            throw new RuntimeException("JWT signature validation failed", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT token compact of handler are invalid", e);
        }
    }

    /**
     * 检查token是否即将过期（30分钟内）
     *
     * @param token JWT token
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        Date expiration = getExpirationDateFromToken(token);
        Date now = new Date();
        long timeUntilExpiration = expiration.getTime() - now.getTime();
        return timeUntilExpiration <= 30 * 60 * 1000; // 30分钟
    }

    /**
     * 刷新token
     *
     * @param refreshToken 刷新token
     * @return 新的访问token
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String tokenType = getTokenTypeFromToken(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Not a refresh token");
        }

        Long userId = getUserIdFromToken(refreshToken);
        String username = getUsernameFromToken(refreshToken);

        return generateAccessToken(userId, username);
    }
}
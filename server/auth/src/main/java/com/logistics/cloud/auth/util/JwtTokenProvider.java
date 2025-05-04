package com.logistics.cloud.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 * JWT工具类
 * 负责生成、解析、校验accessToken和refreshToken。
 */
public class JwtTokenProvider {
    // 建议密钥长度至少256位
    private static final String SECRET = "logistics-cloud-jwt-secret-key-1234567890abcdef";
    // accessToken有效期（30分钟）
    private static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 30;
    // refreshToken有效期（7天）
    private static final long REFRESH_TOKEN_EXPIRE = 1000L * 60 * 60 * 24 * 7;
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * 生成accessToken
     * @param claims 载荷（如userId, username, roles等）
     */
    public static String generateAccessToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成refreshToken
     * @param claims 载荷
     */
    public static String generateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token，返回Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 校验token是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 
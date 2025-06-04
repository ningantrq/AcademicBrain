package com.academicbrain.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * @author AcademicBrain
 */
@Component
public class JwtUtils {

    private static final String SECRET = "academicbrain";
    private static final long EXPIRATION = 86400000; // 24小时
    private static final long REFRESH_EXPIRATION = 604800000; // 7天

    /**
     * 生成访问token
     */
    public static String generateAccessToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, EXPIRATION);
    }

    /**
     * 生成刷新token
     */
    public static String generateRefreshToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, REFRESH_EXPIRATION);
    }

    /**
     * 创建token
     */
    private static String createToken(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 验证token
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从token中获取用户ID
     */
    public static String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class);
    }

    /**
     * 从token中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    /**
     * 检查token是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
} 
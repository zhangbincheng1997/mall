package com.example.demo.access;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 根据用户信息生成token
     *
     * @param username
     * @return
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // claims.put(Claims.ID, 520);
        claims.put(Claims.SUBJECT, username);
        claims.put(Claims.ISSUED_AT, new Date());
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // 过期时间
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return tokenHead + jwt; // 添加"Bearer "
    }

    /**
     * 从token中获取JWT中的负载
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(tokenHead)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 从token中获取username
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String username = claims.getSubject();
        return username;
    }

    /**
     * 从token中获取expiration
     *
     * @param token
     * @return
     */
    private Date getExpirationFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration;
    }

    /**
     * 验证token是否有效
     *
     * @param token
     * @param userDetails
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Date expiration = getExpirationFromToken(token);
        return username.equals(userDetails.getUsername()) && !expiration.before(new Date());
    }
}

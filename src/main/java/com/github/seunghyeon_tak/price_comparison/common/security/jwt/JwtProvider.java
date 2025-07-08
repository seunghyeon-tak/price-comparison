package com.github.seunghyeon_tak.price_comparison.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    // jwt 생성, 검증, 파싱

    private final Key key;
    private final long accessTokenValidityInMs;
    private final long refreshTokenValidityInMs;

    public JwtProvider(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.accessTokenValidityInMs = jwtProperties.getAccessTokenValidityInMs();
        this.refreshTokenValidityInMs = jwtProperties.getRefreshTokenValidityInMs();
    }

    public String generateAccessToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000L * 60 * 60 * 24 * 7);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRefreshTokenValidityInMillis() {
        return refreshTokenValidityInMs;
    }

    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token)
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}

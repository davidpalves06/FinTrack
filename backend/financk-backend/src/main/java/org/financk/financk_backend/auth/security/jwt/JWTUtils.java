package org.financk.financk_backend.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {
    public static final long JWT_EXPIRATION_TIME = 1000L * 60 * 15;
    private final String secretKey;
    private final SecretKey SECRET_KEY;

    public JWTUtils() {
        //TODO : GET THIS FROM ENVIRONMENT VALUE
        this.secretKey = "TEMPORARYSECRETKEYFORTESTINGLOCAL";
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    }

    // Extract username from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String createAccessToken(String subject) {
        return Jwts.builder().claim("isAccess",true).subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(SECRET_KEY).compact();
    }

    public String createRefreshToken(String subject,long expirationTime) {
        return Jwts.builder().claim("isRefresh",true).subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY).compact();
    }

    // Validate the token
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
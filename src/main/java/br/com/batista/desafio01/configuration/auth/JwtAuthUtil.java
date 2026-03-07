package br.com.batista.desafio01.configuration.auth;

import br.com.batista.desafio01.exception.JwtExpiredException;
import br.com.batista.desafio01.exception.JwtInvalidException;
import br.com.batista.desafio01.exception.JwtUnsupportedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtAuthUtil {

    @Value("${jwt.secret.key}")
    private String secretKeyValue;

    private Key secretKey;

    private static final long EXPIRATION_TIME = 43200000; // 12 hours in milliseconds

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyValue.getBytes(StandardCharsets.UTF_8));
    }

    // Generate a JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return validateTokenAndGetSubject(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }

    public String validateTokenAndGetSubject(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("jwt.error.expired", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedException("jwt.error.unsupported", e);
        } catch (MalformedJwtException e) {
            throw new JwtInvalidException("jwt.error.malformed", e);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidException("jwt.error.empty", e);
        }
    }
}

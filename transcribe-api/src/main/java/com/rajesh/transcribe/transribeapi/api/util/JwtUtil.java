package com.rajesh.transcribe.transribeapi.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class JwtUtil {
    
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.io.sessionTimeout}")
    private int timeoutinMin;
    
    /**
     *
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     *
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     *
     * @param token
     * @return
     */
    public Boolean updateExpiration(String token) {
        Claims claims = extractAllClaims(token);
         claims.setExpiration(Date.from(Instant.now()));
         return Boolean.TRUE;
    }
    
    /**
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     *
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(UTF_8)).parseClaimsJws(token).getBody();
    }
    
    /**
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails,String userFingerprint) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("owner", "auth0");
        claims.put("userFingerPrint", userFingerprint);
        claims.put("typ", "JWT");
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     *
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setNotBefore(Date.from(Instant.now()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * timeoutinMin ))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(UTF_8)).compact();
    }
    
    /**
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     *
     * @param token
     * @param username
     * @return
     */
    public Boolean validateToken(String token, String username) {
        return (username.equalsIgnoreCase(extractUsername(token)) && !isTokenExpired(token));
    }
    
    /**
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean inValidateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && updateExpiration(token));
    }
}

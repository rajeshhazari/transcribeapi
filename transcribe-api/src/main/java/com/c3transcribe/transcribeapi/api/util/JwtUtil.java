package com.c3transcribe.transcribeapi.api.util;

import com.c3transcribe.transcribeapi.api.global.exceptions.CustomException;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class JwtUtil {
    
    @Value("${app.jwt.secret}")
    private String secretKey;
    @Value("${app.io.sessionTimeout}")
    private int timeoutinMin;
    
    private static final Logger logger = getLogger(JwtUtil.class);
    
    public static final String ROLES = "ROLES";
   /* @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    */
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
        return Jwts.parser().setSigningKey(secretKey.getBytes(UTF_8)).parseClaimsJws(token).getBody();
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
    public String generateJwtToken(UserDetails userDetails, String userFingerprint) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("owner", "auth0");
        claims.put("userFingerPrint", userFingerprint);
        claims.put("typ", "JWT");
        return createToken(claims, userDetails.getUsername());
    }
    
    //generate token for user
    public String generateToken(Authentication authentication) {
        final Map<String, Object> claims = new HashMap<>();
        final UserDetails user = (UserDetails) authentication.getPrincipal();
        
        final List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        claims.put(ROLES, roles);
        return createToken(claims, user.getUsername());
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
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(UTF_8)).compact();
    }
    
    /**
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateJwtToken(String token, UserDetails userDetails, HttpServletRequest request) {
        try {
            String userFingerprint = null;
            if (request.getCookies() != null && request.getCookies().length > 0) {
                List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
                Optional<Cookie> cookie = cookies.stream().filter(c -> "__Secure-Fgp"
                        .equalsIgnoreCase(c.getName())).findFirst();
                if (cookie.isPresent()) {
                    userFingerprint = cookie.get().getValue();
                }
            }
            logger.debug("userFingerprint extracted from cookie:: {} ", userFingerprint);
            final String username = extractUsername(token);
            Claims claims = extractAllClaims(token);
            if(userFingerprint.equalsIgnoreCase(claims.get("userFingerPrint", String.class))){
                return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            }else {
                //TODO invalidate jwt token
                logger.warn("Fingerprint validation failed for user {}",userDetails.getUsername());
            }
            
        }catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            //TODO invalidate jwt token and return exception
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            //TODO invalidate jwt token and return exception
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            //TODO invalidate jwt token and return exception
            logger.error("JWT token is unsupported: {}", e.getMessage());
        }catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  false;
    }
    
    /**
     *
     * @param token
     * @param username
     * @return
     */
    public Boolean validateJwtToken(String token, String username) {
        return (username.equalsIgnoreCase(extractUsername(token)) && !isTokenExpired(token));
    }
    
    public Boolean validateJwtToken(String token) {
        final String username = extractUsername(token);
        return username != null && !isTokenExpired(token);
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
    
    
    public List<String> getRoles(String token) {
        return getClaimFromToken(token, claims -> (List) claims.get(ROLES));
    }
    
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}

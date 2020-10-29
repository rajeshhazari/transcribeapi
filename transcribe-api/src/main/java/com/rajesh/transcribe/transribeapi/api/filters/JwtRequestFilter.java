package com.rajesh.transcribe.transribeapi.api.filters;

import com.rajesh.transcribe.transribeapi.api.services.JwtUserDetailsService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    
    @Value("${app.io.sessionTimeout}")
    private int timeoutinMin;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    public static final String BEARER = "Bearer ";
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    
        
        final String authorizationHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        final String requestRemoteIp = request.getRemoteHost();
        final String requestUrl = request.getRequestURL().toString();
        final String AUTHURI = "/api/v1/auth";
        
        String username = null;
        String jwttoken = null;
        String password = null;
        
        /*if (StringUtils.hasText(requestURI) && requestURI.equalsIgnoreCase(AUTHURI) && StringUtils.isEmpty(authorizationHeader)){
            LOGGER.debug("No Authorization Header for requestURI: {}  requestRemoteIp : {} requestUrl: {} ",requestURI,requestRemoteIp,requestUrl);
            //AuthenticationRequestDto authenticationRequestDto =
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mandatory Authorization header is missing!.");
        } else if (StringUtils.hasText(requestURI) && !requestURI.equalsIgnoreCase(AUTHURI) && StringUtils.isEmpty(authorizationHeader)){
            LOGGER.error("No Authorization Header for request URI requestURI: {}  requestRemoteIp : {} requestUrl: {} ",requestURI,requestRemoteIp,requestUrl);
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mandatory Authorization header is missing!.");
        } else if (!StringUtils.hasText(requestURI) && StringUtils.isEmpty(authorizationHeader)){
            LOGGER.error("No Authorization Header with blank request URI requestURI: {}  requestRemoteIp : {} requestUrl: {} ",requestURI,requestRemoteIp,requestUrl);
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mandatory Authorization header is missing!.");
        } else if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer")) {
            jwttoken = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwttoken);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Unable to get username from JWT Token. requestURI: {}  requestRemoteIp : {} requestUrl: {} ",requestURI,requestRemoteIp,requestUrl);
                response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
            } catch (ExpiredJwtException ex) {
                LOGGER.warn("JWT Token has expired! requestURI: {}  requestRemoteIp : {} requestUrl: {} ",requestURI,requestRemoteIp,requestUrl);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()+ " JWT Token has expired, Please Login again! email:  "+request.getParameterNames());
            }
        }else {
            LOGGER.error("JWT Token does not begin with Bearer String");
            response.sendError(HttpStatus.UNAUTHORIZED.value(),"JWT Token does not begin with Bearer String");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username,passwordEncoder.encode(password));
        
            if (jwtUtil.validateJwtToken(jwttoken, userDetails, request)) {
            
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }*/
    
        final Optional<String> jwt = getJwtFromRequest(request);
        jwt.ifPresent(token -> {
            try {
                if (jwtUtil.validateJwtToken(token)) {
                    setSecurityContext(new WebAuthenticationDetailsSource().buildDetails(request), token);
                }
            } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException e) {
                logger.error("Unable to get JWT Token or JWT Token has expired");
                //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("anonymous", "anonymous", null);
                //SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        });
        
        chain.doFilter(request, response);
    }
    
    
    private void setSecurityContext(WebAuthenticationDetails authDetails, String token) {
        final String username = jwtUtil.extractUsername(token);
        final List<String> roles = jwtUtil.getRoles(token);
        //final UserDetails userDetails = new User(username, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(authDetails);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    private static Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
    
    private static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
    
    public static String getUserAgent(HttpServletRequest request) {
        String ua = "";
        if (request != null) {
            ua = request.getHeader("User-Agent");
        }
        return ua;
    }
}

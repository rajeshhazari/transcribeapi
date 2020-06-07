package com.rajesh.transcribe.transribeapi.api.filters;

import com.rajesh.transcribe.transribeapi.api.services.JwtUserDetailsService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${app.io.sessionTimeout}")
    private int timeoutinMin;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    
    
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwttoken = null;
        if (StringUtils.isEmpty(authorizationHeader)){
        
        }
        if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            jwttoken = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwttoken);
            } catch (IllegalArgumentException ex) {
                LOGGER.info("Unable to get JWT Token");
            } catch (ExpiredJwtException ex) {
                LOGGER.info("JWT Token has expired");
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
            }
        }else {
            LOGGER.warn("JWT Token does not begin with Bearer String");
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwttoken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}

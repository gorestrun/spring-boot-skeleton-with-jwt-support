package com.example.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class); //logger field name is already taken in GenericFilterBean
    private String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
    	
    	try {
	    	UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
	
	        if (authentication != null) {
	        	SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	        
	        filterChain.doFilter(request, response);
    	} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
    		log.warn("An exception has occurred: ", e);
    		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    	}
    }

    @SuppressWarnings("unchecked")
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

    	String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && !bearerToken.isEmpty() && bearerToken.startsWith("Bearer ")) {        	
        	//Check if the token is not expired, signature is correct, ...
            Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(bearerToken.replace("Bearer ",""))
                .getBody();

            String username = claims.getSubject();	
			List<String> authorities = (List<String>) claims.get("authorities");			
            if (username != null && !username.isEmpty()) {
            	return new UsernamePasswordAuthenticationToken(username, null, 
                	authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            }
        } 

        return null;
    }

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
package com.example.filter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private String secret;
	private int jwtTimeToLiveInMinutes;
	
    public JwtAuthenticationFilter() {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/token", "GET"));
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    	String username = "";
    	String password = "";
    	String authorization = request.getHeader("Authorization");
    	
    	if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
    	    String base64Credentials = authorization.substring("Basic".length()).trim();
    	    if(base64Credentials.length() > 0) {
    	    	username = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8).split(":")[0];
    	    	password = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8).split(":")[1];
    	    	
				return this.getAuthenticationManager()
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				
    	    }
    	}
    	
        throw new BadCredentialsException("Bad credentials ^^");
    }

    //We need an override for successful authentication 
    //because the default Spring flow would stop the filter chain and proceed with a redirect.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
    	
    	String token = buildJWTToken(authentication);    
        response.addHeader("Authorization", "Bearer " + token);
    }

	private String buildJWTToken(Authentication authentication) {
        DateTime now = new DateTime();
        
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setId(UUID.randomUUID().toString().replace("-", ""))
            .setSubject(((LdapUserDetailsImpl)authentication.getPrincipal()).getUsername())
            .claim("authorities", "authorities") //To add accordingly
            .setIssuedAt(now.toDate()) //issuedAt expects a Date, so need to use joda
            .setExpiration(now.plusMinutes(jwtTimeToLiveInMinutes).toDate())
            .signWith(SignatureAlgorithm.HS512, secret) //base64EncodedSecretKey
            .compact();
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setJwtExpirationTimeInMinutes(int jwtExpirationTimeInMinutes) {
		this.jwtTimeToLiveInMinutes = jwtExpirationTimeInMinutes;
	}
}
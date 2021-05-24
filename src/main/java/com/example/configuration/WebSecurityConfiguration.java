package com.example.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import com.example.filter.JwtAuthenticationFilter;
import com.example.filter.JwtAuthorizationFilter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Value("${jwt.secret}")
    private String secret;
	
	@Value("${jwt.time-to-live-in-minutes}")
    private int jwtTimeToLiveInMinutes;
	
	@Value("${spring.ldap.domain}")
	private String ldapDomain;
	
	@Value("${spring.ldap.url}")
    private String ldapUrl;
	
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
        httpSecurity
        	.requiresChannel()
        		.anyRequest()
        		.requiresSecure()
        	.and()
        	.sessionManagement()
        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        	.csrf().disable() //We recommend disabling CSRF protection completely only if you are creating a service that is used by non-browser clients.
            .authorizeRequests()
            	.antMatchers("/api/health").authenticated()
                .anyRequest().denyAll()
            .and()
            .addFilter(jwtAuthenticationFilter())
            .addFilter(jwtAuthorizationFilter());
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManager() {
    	return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
    
    }
    
    @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
    	
        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);
        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        
        return provider;
    }
    
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
		jwtAuthenticationFilter.setSecret(secret);
		jwtAuthenticationFilter.setJwtExpirationTimeInMinutes(jwtTimeToLiveInMinutes);
		
		return jwtAuthenticationFilter;
	}
	
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager());
		jwtAuthorizationFilter.setSecret(secret);
		
		return jwtAuthorizationFilter;
	}
}
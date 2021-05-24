package com.example.configuration;

import java.time.Duration;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {
    
    @Value(value = "${requested-token.time-to-live}")
    private String requestedTokenTimeToLive;
    private static final int MAX_ELEMENTS_DEFAULT = 100;
    
    @Bean
    public javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration(){
        org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration = 
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(MAX_ELEMENTS_DEFAULT))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.parse(requestedTokenTimeToLive)))
                .build();
        
        return Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
    }
   
    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration) {
        return cacheManager -> cacheManager.createCache("cacheName", jcacheConfiguration);
    }
}

package com.example.configuration;

import javax.sql.DataSource;

import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class HealthIndicatorConfiguration {
	
	/*
	 	This bean is required else it would be deemed as not found,
	 	(although i can find it in the Application Context),
		per following upgrade - 2.2.7.RELEASE > 2.3.0.RELEASE.
	*/
	@Bean
	@Primary
	public DataSourceHealthIndicator dataSourceHealthIndicator(DataSource dataSource) {
		return new DataSourceHealthIndicator(dataSource, "SELECT 1 FROM DUAL");
	}
}

package com.example.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JPAWithHibernateConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf,
        @Value("${spring.transaction.default-timeout}") int defaultTimeout) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        txManager.setDataSource(dataSource);
        txManager.setDefaultTimeout(defaultTimeout); //Default is 30s
        
        return txManager;
    }
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		
		emf.setJpaVendorAdapter(jpaVendorAdapter());
		emf.setJpaProperties(hibernateProperties());

		emf.setPackagesToScan("com.example.model");
		emf.setPersistenceUnitName("org.hibernate.jpa.HibernatePersistenceProvider");
		
		return emf;
	}
	
	@Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        
        //NOTE: Do not set this flag to 'true' while also setting JPA 2.1's javax.persistence.schema-generation.database.action property.
      	adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.Oracle12cDialect");
        
        return adapter;
    }
	
	Properties hibernateProperties() {
	    Properties properties = new Properties();
	    
	    //NOTE: Showing & formatting SQL is controlled by logging level in application-XXX.properties. Do not configure the properties here.
	    properties.put("hibernate.format_sql", Boolean.TRUE.toString());
	    properties.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
	    properties.put("hibernate.hbm2ddl.auto", "none");
	    properties.put("hibernate.hql.bulk_id_strategy", "org.hibernate.hql.spi.id.inline.InlineIdsInClauseBulkIdStrategy"); //To prevent Hibernate from creating temporary tables for bulk operations
	    
	    //Second level cache
	    properties.put("hibernate.cache.region.factory_class", "jcache"); //default: load the default jcache provider & create the default javax.cache.CacheManager
	    properties.put("hibernate.cache.use_second_level_cache", Boolean.TRUE.toString());
	    properties.put("hibernate.cache.use_query_cache", Boolean.TRUE.toString());
	    properties.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider"); //Specify which provider to use
	    properties.put("hibernate.javax.cache.uri", "ehcache.xml"); //Specify configuration for CacheManager & Cache
	    properties.put("hibernate.javax.cache.missing_cache_strategy", "create-warn");
	    
	    //properties.put("hibernate.generate_statistics", Boolean.TRUE.toString()); //Only activate when required
	    //properties.put("hibernate.jmx.enabled", Boolean.TRUE.toString()); //Only activate when required
	    
	    return properties;
	}
}

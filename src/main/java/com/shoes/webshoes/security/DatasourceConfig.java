/**
 * 
 */
package com.shoes.webshoes.security;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan({ "com.shoes.webshoes" })
@EnableTransactionManagement
@EnableJpaRepositories({ "com.shoes.webshoes.dao" })
public class DatasourceConfig {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	private Environment environment;

	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = this.initHikariPoolingConfig("hikari-pool");

		HikariDataSource hikariPoolingDataSource = new HikariDataSource(hikariConfig);

		return hikariPoolingDataSource;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource ds)
			throws PropertyVetoException {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(ds);
		entityManagerFactory.setPackagesToScan(new String[] { "com.shoes.webshoes" });
		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
		return entityManagerFactory;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.shoes.webshoes" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put(org.hibernate.cfg.Environment.DIALECT, environment.getRequiredProperty("hibernate.dialect"));
		properties.put(org.hibernate.cfg.Environment.SHOW_SQL, environment.getRequiredProperty("hibernate.show_sql"));
		properties.put(org.hibernate.cfg.Environment.FORMAT_SQL,
				environment.getRequiredProperty("hibernate.format_sql"));
		return properties;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	private HikariConfig initHikariPoolingConfig(String poolName) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(applicationProperties.getDatabaseDriver());

		hikariConfig.setJdbcUrl(applicationProperties.getDatabaseUrl());
		hikariConfig.setUsername(applicationProperties.getDatabaseUsername());
		hikariConfig.setPassword(applicationProperties.getDatabasePassword());

		hikariConfig.setPoolName(poolName);
		return hikariConfig;
	}

}
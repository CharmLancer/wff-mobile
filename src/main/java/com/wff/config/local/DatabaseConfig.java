package com.wff.config.local;

import java.net.URISyntaxException;

import javax.naming.ConfigurationException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config/application.properties")
@Profile("local")
public class DatabaseConfig {
	@Autowired
	Environment env;

	@Bean
	public BasicDataSource basicDataSource() throws URISyntaxException, ConfigurationException {
		return null;

	}
}

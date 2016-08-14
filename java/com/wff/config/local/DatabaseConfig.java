package com.wff.config.local;

import java.net.URISyntaxException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.wff.exception.ConfigurationException;

@Configuration
// @PropertySource("classpath:com/wff/config/application.properties")
@PropertySource("classpath:config/application.properties")
@Profile("local")
public class DatabaseConfig {
	@Autowired
	Environment env;

	@Bean
	public BasicDataSource basicDataSource() throws URISyntaxException, ConfigurationException {
		// URI dbUri = new URI(System.getenv("DATABASE_URL"));
		// URI dbUri = new URI("jdbc:postgresql://localhost:5432/iwrm");
		// URI dbUri= new URI(env.getProperty("DATABASE_URL"));
		// System.out.println();
		// String username = dbUri.getUserInfo().split(":")[0];
		// String password = dbUri.getUserInfo().split(":")[1];
		// String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
		// + dbUri.getPort() + dbUri.getPath();

		try {
			Class.forName("org.postgresql.Driver");
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setUrl(env.getProperty("DATABASE_URL_LOCAL"));
			basicDataSource.setUsername(env.getProperty("DATABASE_USER_LOCAL"));
			basicDataSource.setPassword(env.getProperty("DATABASE_PASSWORD_LOCAL"));
			return basicDataSource;
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException();
		}
	}
}

package test.config;

import java.net.URISyntaxException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config/application.properties")
public class DatabaseConfig {
	@Autowired
	Environment env;

	@Bean
	public BasicDataSource basicDataSource() throws URISyntaxException {
		String username = env.getProperty("DATABASE_USER_LOCAL");// dbUri.getUserInfo().split(":")[0];
		String password = env.getProperty("DATABASE_PASSWORD_LOCAL");// dbUri.getUserInfo().split(":")[1];
		String dbUrl = env.getProperty("DATABASE_URL_LOCAL");// "jdbc:postgresql://"
																// +
																// dbUri.getHost()
																// + ':' +
																// dbUri.getPort()
																// +
																// dbUri.getPath();

		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl(dbUrl);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);

		return basicDataSource;
	}

	// @Bean
	// public DataSource dataSource() throws URISyntaxException {
	// // URI dbUri = new URI(System.getenv("DATABASE_URL"));
	// // URI dbUri = new URI("jdbc:postgresql://localhost:5432/iwrm");
	// // URI dbUri= new URI(env.getProperty("DATABASE_URL"));
	// // System.out.println();
	// // String username = dbUri.getUserInfo().split(":")[0];
	// // String password = dbUri.getUserInfo().split(":")[1];
	// // String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
	// // + dbUri.getPort() + dbUri.getPath();
	//
	// DataSource basicDataSource = new DriverManagerDataSource(
	// env.getProperty("DATABASE_URL_LOCAL"),
	// env.getProperty("DATABASE_USER_LOCAL"),
	// env.getProperty("DATABASE_PASSWORD_LOCAL"));
	// // basicDataSource.setUrl();
	// // basicDataSource.setUsername();
	// // basicDataSource.setPassword();
	// return basicDataSource;
	// }
}

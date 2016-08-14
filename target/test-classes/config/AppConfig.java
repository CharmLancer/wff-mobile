package test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.wff.database.local.DatabaseService;
import com.wff.database.local.DatabaseServiceImpl;
import com.wff.site.services.StationService;
import com.wff.site.services.StationServiceImpl;
import com.wff.site.services.UserService;
import com.wff.site.services.UserServiceImpl;

@Configuration
// @TestPropertySource("application.properties")
public class AppConfig {
	@Autowired
	Environment env;

	@Bean
	UserService getUserService() {
		return new UserServiceImpl();
	}

	@Bean
	StationService getStationService() {
		return new StationServiceImpl();
	}

	@Bean
	DatabaseService getDatabaseService() {
		return new DatabaseServiceImpl();
	}

}

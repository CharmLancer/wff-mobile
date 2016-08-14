package com.wff.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.wff.config.local.DatabaseConfig;

public class Bootstrap implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext container) throws ServletException {
		container.getServletRegistration("default").addMapping("/resource/*");
		container.getServletRegistration("default").addMapping("/index.html");

		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.getEnvironment().setActiveProfiles("local");
		rootContext.register(RootContextConfiguration.class, AppConfig.class, DatabaseConfig.class);
		container.addListener(new ContextLoaderListener(rootContext));

		AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
		servletContext.register(ServletContextConfiguration.class);
		ServletRegistration.Dynamic dispatcher = container.addServlet("springDispatcher",
				new DispatcherServlet(servletContext)

		);
		dispatcher.setLoadOnStartup(1);
		// dispatcher.addMapping("/wff/*");
		dispatcher.addMapping("/");

	}
}
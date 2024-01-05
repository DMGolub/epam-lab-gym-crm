package com.epam.dmgolub.gym.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.persistence.EntityManagerFactory;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories("com.epam.dmgolub.gym.repository")
@ComponentScan("com.epam.dmgolub.gym")
@PropertySource("classpath:application.properties")
public class AppConfig implements WebMvcConfigurer {

	private final ApplicationContext applicationContext;
	private final Environment environment;
	private List<Converter<String, ?>> converters;

	public AppConfig(final ApplicationContext applicationContext, final Environment environment) {
		this.applicationContext = applicationContext;
		this.environment = environment;
	}

	@Autowired
	public void setConverters(final List<Converter<String, ?>> converters) {
		this.converters = converters;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		final SpringResourceTemplateResolver templateResolver =
			new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

	@Override
	public void configureViewResolvers(final ViewResolverRegistry registry) {
		final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		registry.viewResolver(resolver);
	}

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		converters.forEach(registry::addConverter);
	}

	@Override
	public Validator getValidator() {
		return validator();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
		final LocalContainerEntityManagerFactoryBean entityManagerFactory =
			new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setPackagesToScan("com.epam.dmgolub.gym.entity");
		entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactory.setJpaProperties(additionalProperties());
		return entityManagerFactory;
	}

	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties additionalProperties() {
		final Properties properties = new Properties();
		final String hbm2ddl = "hibernate.hbm2ddl.auto";
		final String dialect = "hibernate.dialect";
		properties.setProperty(hbm2ddl, environment.getProperty(hbm2ddl));
		properties.setProperty(dialect, environment.getProperty(dialect));
		return properties;
	}

	@Bean
	DataSource dataSource() throws NamingException {
		final JndiTemplate jndi = new JndiTemplate();
		return jndi.lookup(Objects.requireNonNull(environment.getProperty("jdbc.url")), DataSource.class);
	}

	@Bean
	JdbcTemplate jdbcTemplate() throws NamingException {
		return new JdbcTemplate(dataSource());
	}
}

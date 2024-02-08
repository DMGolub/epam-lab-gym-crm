package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.constant.Constants;
import com.epam.dmgolub.gym.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.epam.dmgolub.gym.repository")
@EntityScan("com.epam.dmgolub.gym.entity")
public class AppConfig implements WebMvcConfigurer {

	private LoggingInterceptor loggingInterceptor;

	@Autowired
	void setLoggingInterceptor(final LoggingInterceptor loggingInterceptor) {
		this.loggingInterceptor = loggingInterceptor;
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		final String apiPattern = Constants.BASE_API_URL + ApiVersion.VERSION_1 + "/**";
		registry.addInterceptor(loggingInterceptor)
			.addPathPatterns(apiPattern);
	}
}

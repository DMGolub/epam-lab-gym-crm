package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.interceptor.SessionLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	private SessionLoginInterceptor sessionLoginInterceptor;

	@Autowired
	void setSessionLoginInterceptor(final SessionLoginInterceptor sessionLoginInterceptor) {
		this.sessionLoginInterceptor = sessionLoginInterceptor;
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(sessionLoginInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/*", "/trainers/new", "/trainees/new", "/login/*");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

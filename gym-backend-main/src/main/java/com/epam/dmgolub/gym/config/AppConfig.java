package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.constant.Constants;
import com.epam.dmgolub.gym.interceptor.LoggingInterceptor;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.epam.dmgolub.gym.repository")
@EntityScan("com.epam.dmgolub.gym.entity")
public class AppConfig implements WebMvcConfigurer {

	@Value("${circuit-breaker.failure-rate-threshold}")
	private float failureRateThreshold;
	@Value("${circuit-breaker.wait-duration-in-open-state-seconds}")
	private long waitDurationInOpenState;
	@Value("${circuit-breaker.sliding-window-size}")
	private int slidingWindowSize;
	@Value("${circuit-breaker.timeout-duration-seconds}")
	private long timeoutDuration;

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


	@Bean
	WebClient webClient(
		final OAuth2AuthorizedClientManager authorizedClientManager,
		final ReactorLoadBalancerExchangeFilterFunction lbFunction
	) {
		final var oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
		return WebClient.builder()
			.filter(lbFunction)
			.filter(oauth2Client)
			.build();
	}

	@Bean
	public CircuitBreaker circuitBreaker(final CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
		return circuitBreakerFactory.create("circuitBreaker");
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
		final var circuitBreakerConfig = CircuitBreakerConfig.custom()
			.failureRateThreshold(failureRateThreshold)
			.waitDurationInOpenState(Duration.ofSeconds(waitDurationInOpenState))
			.slidingWindowSize(slidingWindowSize)
			.build();
		final var timeLimiterConfig = TimeLimiterConfig.custom()
			.timeoutDuration(Duration.ofSeconds(timeoutDuration))
			.build();
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
			.timeLimiterConfig(timeLimiterConfig)
			.circuitBreakerConfig(circuitBreakerConfig)
			.build());
	}
}

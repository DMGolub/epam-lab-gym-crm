package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.controller.LoginRestController;
import com.epam.dmgolub.gym.controller.TraineeRestController;
import com.epam.dmgolub.gym.controller.TrainerRestController;
import com.epam.dmgolub.gym.controller.TrainingTypeRestController;
import com.epam.dmgolub.gym.security.filter.TokenAuthenticationFilter;
import com.epam.dmgolub.gym.security.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.SecureRandom;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

	private final UserDetailsService userDetailsService;
	private final TokenService tokenService;

	public WebSecurityConfig(final UserDetailsService userDetailsService, final TokenService tokenService) {
		this.userDetailsService = userDetailsService;
		this.tokenService = tokenService;
	}

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		final int strength = 10;
		return new BCryptPasswordEncoder(strength, new SecureRandom());
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(userDetailsService, tokenService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.authorizeHttpRequests(requests -> requests.requestMatchers(
					LoginRestController.URL + "/login",
					TraineeRestController.URL,
					TrainerRestController.URL,
					TrainingTypeRestController.URL + "/**"
				).permitAll()
				.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.logout(AbstractHttpConfigurer::disable)
			.httpBasic(withDefaults())
			.oauth2Client(withDefaults());
		return http.build();
	}

	CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/v*/**", configuration);
		return source;
	}

	@Bean
	OAuth2AuthorizedClientManager authorizedClientManager(
		final ClientRegistrationRepository clientRegistrationRepository,
		final OAuth2AuthorizedClientService authorizedClientService
	) {
		final var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
			.builder()
			.clientCredentials()
			.build();
		final var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
			clientRegistrationRepository,
			authorizedClientService
		);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
		return authorizedClientManager;
	}
}

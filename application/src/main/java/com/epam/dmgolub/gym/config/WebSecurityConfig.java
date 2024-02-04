package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.controller.LoginRestController;
import com.epam.dmgolub.gym.controller.TraineeRestController;
import com.epam.dmgolub.gym.controller.TrainerRestController;
import com.epam.dmgolub.gym.controller.TrainingTypeRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(requests -> requests.requestMatchers(
					LoginRestController.URL,
					TraineeRestController.URL,
					TrainerRestController.URL,
					TrainingTypeRestController.URL + "/**"
				).permitAll()
				.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.logout(LogoutConfigurer::permitAll)
			.httpBasic(withDefaults());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		final int strength = 10;
		return new BCryptPasswordEncoder(strength, new SecureRandom());
	}
}

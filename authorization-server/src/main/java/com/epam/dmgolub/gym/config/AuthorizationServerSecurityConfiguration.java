package com.epam.dmgolub.gym.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class AuthorizationServerSecurityConfiguration {

	private static final String KEY_PAIR_GENERATOR_ALGORITHM = "RSA";
	private static final int KEY_PAIR_GENERATOR_KEY_SIZE = 2048;

	@Value("${auth.issuer}")
	private String issuer;
	@Value("${auth.client-id}")
	private String clientId;
	@Value("${auth.client-secret}")
	private String clientSecret;
	@Value("${auth.redirect-uri}")
	private String redirectUri;
	@Value("${auth.access-token-time-to-live-in-seconds}")
	private long accessTokenTimeToLive;
	@Value("${auth.refresh-token-time-to-live-in-seconds}")
	private long refreshTokenTimeToLive;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.exceptionHandling(exceptions -> exceptions.
			authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))).build();
	}

	@Bean
	public AuthorizationServerSettings providerSettings() {
		return AuthorizationServerSettings.builder().issuer(issuer).build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		final RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
			.clientId(clientId)
			.clientSecret(clientSecret)
			.clientAuthenticationMethods(s -> {
				s.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
				s.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
			})
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.redirectUri(redirectUri)
			.scope("message:read")
			.scope("message:write")
			.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
				.idTokenSignatureAlgorithm(SignatureAlgorithm.PS256)
				.accessTokenTimeToLive(Duration.ofSeconds(accessTokenTimeToLive))
				.refreshTokenTimeToLive(Duration.ofSeconds(refreshTokenTimeToLive))
				.reuseRefreshTokens(true)
				.build())
			.build();
		return new InMemoryRegisteredClientRepository(registeredClient);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		final RSAKey rsaKey = Jwks.generateRsa();
		final JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	static class Jwks {

		private Jwks() {
			// Empty
		}

		public static RSAKey generateRsa() {
			final var keyPair = KeyGeneratorUtils.generateRsaKey();
			final var publicKey = (RSAPublicKey) keyPair.getPublic();
			final var privateKey = (RSAPrivateKey) keyPair.getPrivate();
			return new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		}
	}

	static class KeyGeneratorUtils {

		private KeyGeneratorUtils() {
			// Empty
		}

		static KeyPair generateRsaKey() {
			KeyPair keyPair;
			try {
				final var keyPairGenerator = KeyPairGenerator.getInstance(KEY_PAIR_GENERATOR_ALGORITHM);
				keyPairGenerator.initialize(KEY_PAIR_GENERATOR_KEY_SIZE);
				keyPair = keyPairGenerator.generateKeyPair();
			} catch (final Exception ex) {
				throw new IllegalStateException(ex);
			}
			return keyPair;
		}
	}
}

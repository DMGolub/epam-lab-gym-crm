package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.KeyUtils;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@TestConfiguration
public class IntegrationTestConfig {

	@Bean
	public JWKSource<SecurityContext> jwkSource() throws Exception {
		final var publicKey = KeyUtils.getPublicKey("public.pem");
		final var rsaKey = new RSAKey.Builder(publicKey).build();
		final var jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(final JWKSource<SecurityContext> jwkSource) {
		final var jwsAlgorithm = JWSAlgorithm.RS256;
		final var jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgorithm, jwkSource);
		final var jwtProcessor = new DefaultJWTProcessor<>();
		jwtProcessor.setJWSKeySelector(jwsKeySelector);
		return new NimbusJwtDecoder(jwtProcessor);
	}
}

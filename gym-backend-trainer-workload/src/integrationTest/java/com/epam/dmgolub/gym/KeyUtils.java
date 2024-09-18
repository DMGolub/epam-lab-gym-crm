package com.epam.dmgolub.gym;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

	public static RSAPrivateKey getPrivateKey(final String filename)
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = new ClassPathResource(filename).getInputStream().readAllBytes();
		var privateKeyContent = new String(keyBytes);
		privateKeyContent = privateKeyContent.replaceAll("\\n", "")
			.replace("-----BEGIN PRIVATE KEY-----", "")
			.replace("-----END PRIVATE KEY-----", "");
		final var kf = KeyFactory.getInstance("RSA");
		final var keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
		return (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);
	}

	public static RSAPublicKey getPublicKey(final String filename)
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = new ClassPathResource(filename).getInputStream().readAllBytes();
		var publicKeyContent = new String(keyBytes);
		publicKeyContent = publicKeyContent.replaceAll("\\n", "")
			.replace("-----BEGIN PUBLIC KEY-----", "")
			.replace("-----END PUBLIC KEY-----", "");
		final var kf = KeyFactory.getInstance("RSA");
		final var keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
		return (RSAPublicKey) kf.generatePublic(keySpecX509);
	}

	private KeyUtils() {
		// Empty
	}
}

package com.epam.dmgolub.gym.dto;

import java.util.Objects;

public class SignUpResponseDTO {

	private String token;
	private CredentialsDTO credentials;

	public SignUpResponseDTO() {
		// Empty
	}

	public SignUpResponseDTO(final String token, final CredentialsDTO credentials) {
		this.token = token;
		this.credentials = credentials;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public CredentialsDTO getCredentials() {
		return credentials;
	}

	public void setCredentials(final CredentialsDTO credentials) {
		this.credentials = credentials;
	}

	@Override
	public String toString() {
		return "SignUpResponseDTO{token='" + token + '\'' + ", credentials=" + credentials + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SignUpResponseDTO that = (SignUpResponseDTO) o;

		if (!Objects.equals(token, that.token)) {
			return false;
		}
		return Objects.equals(credentials, that.credentials);
	}

	@Override
	public int hashCode() {
		int result = token != null ? token.hashCode() : 0;
		result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
		return result;
	}
}

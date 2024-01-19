package com.epam.dmgolub.gym.dto.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CredentialsDTOTest {

	private CredentialsDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new CredentialsDTO();
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setUserName(userName);
		assertEquals(userName, requestDTO.getUserName());
	}

	@Test
	void passwordGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String password = "Password";
		requestDTO.setPassword(password);
		assertEquals(password, requestDTO.getPassword());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final CredentialsDTO requestDTO2 = new CredentialsDTO("UserName2", "Word");
		final String expected = "CredentialsDTO{userName='UserName2'}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final CredentialsDTO requestDTO2 = new CredentialsDTO("UserName2", "Word");
		final CredentialsDTO requestDTO3 = new CredentialsDTO("UserName2", "Password");

		assertEquals(requestDTO, requestDTO);
		assertEquals(requestDTO.hashCode(), requestDTO.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2.hashCode(), requestDTO3.hashCode());
	}
}

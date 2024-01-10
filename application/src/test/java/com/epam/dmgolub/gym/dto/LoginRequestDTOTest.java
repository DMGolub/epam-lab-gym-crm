package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LoginRequestDTOTest {

	private LoginRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new LoginRequestDTO();
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
		final LoginRequestDTO requestDTO2 = new LoginRequestDTO("UserName2", "Word");
		final String expected = "LoginRequestDTO{userName='UserName2'}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final LoginRequestDTO requestDTO2 = new LoginRequestDTO("UserName2", "Word");
		final LoginRequestDTO requestDTO3 = new LoginRequestDTO("UserName2", "Password");

		assertEquals(requestDTO, requestDTO);
		assertEquals(requestDTO.hashCode(), requestDTO.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2.hashCode(), requestDTO3.hashCode());
	}
}

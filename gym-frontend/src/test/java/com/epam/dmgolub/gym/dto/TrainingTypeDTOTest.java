package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainingTypeDTOTest {

	private TrainingTypeDTO typeDTO;

	@BeforeEach
	public void setUp() {
		typeDTO = new TrainingTypeDTO();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		typeDTO.setId(1L);
		assertEquals(1L, typeDTO.getId());
	}

	@Test
	void nameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		typeDTO.setName("TestName");
		assertEquals("TestName", typeDTO.getName());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainingTypeDTO type = new TrainingTypeDTO(1L, "Karate");
		final String expected = "TrainingTypeDTO{id=1, name='Karate'}";

		assertEquals(expected, type.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainingTypeDTO typeDTO2 = new TrainingTypeDTO(1L, "TestName");
		final TrainingTypeDTO typeDTO3 = new TrainingTypeDTO(1L, "Another name");

		assertEquals(typeDTO2, typeDTO2);
		assertEquals(typeDTO2.hashCode(), typeDTO2.hashCode());
		assertNotEquals(typeDTO, typeDTO2);
		assertNotEquals(typeDTO.hashCode(), typeDTO2.hashCode());
		assertNotEquals(typeDTO2, typeDTO3);
	}
}

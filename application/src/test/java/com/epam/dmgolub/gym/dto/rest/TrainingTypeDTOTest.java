package com.epam.dmgolub.gym.dto.rest;

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
		final TrainingTypeDTO
			type = new TrainingTypeDTO("Karate", 1L);
		final String expected = "TrainingTypeDTO{name='Karate', id=1}";

		assertEquals(expected, type.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainingTypeDTO typeDTO2 = new TrainingTypeDTO("TestName", 1L);
		final TrainingTypeDTO typeDTO3 = new TrainingTypeDTO("Another name", 1L);

		assertEquals(typeDTO2, typeDTO2);
		assertEquals(typeDTO2.hashCode(), typeDTO2.hashCode());
		assertNotEquals(typeDTO, typeDTO2);
		assertNotEquals(typeDTO.hashCode(), typeDTO2.hashCode());
		assertNotEquals(typeDTO2, typeDTO3);
	}
}

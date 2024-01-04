package com.epam.dmgolub.gym.datasource.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryIdSequenceTest {

	private InMemoryIdSequence<Integer> inMemoryIdSequence;
	private UnaryOperator<Integer> generator;

	@BeforeEach
	void setUp() {
		generator = (i) -> i + 1;
		inMemoryIdSequence = new InMemoryIdSequence<>(1, generator);
	}

	@Test
	void constructor_shouldThrowNullPointerException_whenInitialValueIsNull() {
		assertThrows(NullPointerException.class, () -> new InMemoryIdSequence<>(null, generator));
	}

	@Test
	void constructor_shouldThrowNullPointerException_whenGeneratorIsNull() {
		assertThrows(NullPointerException.class, () -> new InMemoryIdSequence<>(1, null));
	}

	@Test
	void generateNewId_shouldReturnASequenceOfTheeValues_whenInvokedThreeTimes() {
		assertEquals(1, inMemoryIdSequence.generateNewId());
		assertEquals(2, inMemoryIdSequence.generateNewId());
		assertEquals(3, inMemoryIdSequence.generateNewId());
	}

	@Test
	void iteratorNext_shouldReturnASequenceOfTwoValues_whenGenerateNewIdWasInvokedTwice() {
		inMemoryIdSequence.generateNewId();
		inMemoryIdSequence.generateNewId();
		Iterator<Integer> iterator = inMemoryIdSequence.iterator();

		assertTrue(iterator.hasNext());
		assertEquals(1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(2, iterator.next());
		assertFalse(iterator.hasNext());
		assertThrows(NoSuchElementException.class, iterator::next);
	}
}

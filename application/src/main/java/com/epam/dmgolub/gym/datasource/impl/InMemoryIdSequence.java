package com.epam.dmgolub.gym.datasource.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import com.epam.dmgolub.gym.datasource.IdSequence;

public class InMemoryIdSequence<K> implements IdSequence<K>, Iterable<K> {

	private final UnaryOperator<K> generator;
	private final Set<K> generatedSequence = new LinkedHashSet<>();
	private final AtomicReference<K> lastGeneratedValue;

	public InMemoryIdSequence(final K initialValue, final UnaryOperator<K> generator) {
		Objects.requireNonNull(initialValue, "initial value cannot be null");
		Objects.requireNonNull(generator, "generator cannot be null");
		this.lastGeneratedValue = new AtomicReference<>(initialValue);
		this.generator = generator;
	}

	@Override
	public K generateNewId() {
		final K newValue = lastGeneratedValue.getAndUpdate(generator);
		generatedSequence.add(newValue);
		return newValue;
	}

	@Override
	public Iterator<K> iterator() {
		return new Iterator<K>() {

			private final Iterator<K> delegate = generatedSequence.iterator();

			@Override
			public boolean hasNext() {
				return delegate.hasNext();
			}

			@Override
			public K next() {
				return delegate.next();
			}
		};
	}
}

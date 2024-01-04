package com.epam.dmgolub.gym.datasource;

import com.epam.dmgolub.gym.entity.BaseEntity;

import java.util.Map;

public interface DataSource<K> {

	Map<Class<?>, Map<K, BaseEntity<K>>> getData();

	Map<Class<?>, IdSequence<K>> getIdSequencesToClasses();
}

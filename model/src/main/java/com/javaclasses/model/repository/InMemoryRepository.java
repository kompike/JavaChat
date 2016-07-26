package com.javaclasses.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract implementation of {@link Repository} interface
 */
public abstract class InMemoryRepository<Type, Id>
        implements Repository<Type, Id> {

    private Map<Id, Type> entities = new ConcurrentHashMap<>();

    @Override
    public Id add(Type type, Id typeId) {
        entities.put(typeId, type);
        return typeId;
    }

    @Override
    public Type find(Id typeId) {
        return entities.get(typeId);
    }

    @Override
    public Collection<Type> findAll() {
        return entities.values();
    }
}

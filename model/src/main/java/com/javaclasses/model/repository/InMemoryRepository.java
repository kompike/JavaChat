package com.javaclasses.model.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of {@link Repository} interface
 */
public abstract class InMemoryRepository<Type, Id>
        implements Repository<Type, Id> {

    private Map<Id, Type> entities = new HashMap<>();

    @Override
    public Id add(Type type, Id typeId) {
        entities.put(typeId, type);
        return typeId;
    }

    @Override
    public Type find(Id typeId) {
        return null;
    }

    @Override
    public Collection<Type> findAll() {
        return null;
    }
}

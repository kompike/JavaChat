package com.javaclasses.model.repository;

import com.javaclasses.model.entity.Entity;
import com.javaclasses.model.entity.tynitype.EntityId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract instance of {@link Repository} with internal memory
 */
public abstract class InMemoryRepository<Type extends Entity, TypeId extends EntityId> implements Repository<Type, TypeId> {

    private Map<TypeId, Type> entities = new HashMap<>();

    @Override
    public TypeId add(Type type) {
        final TypeId typeId = nextId();
        type.setId(typeId);
        entities.put(typeId, type);
        return typeId;
    }

    @Override
    public Type findById(TypeId typeId) {
        return entities.get(typeId);
    }

    @Override
    public Collection<Type> findAll() {
        return entities.values();
    }

    public abstract TypeId nextId();
}

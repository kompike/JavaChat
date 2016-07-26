package com.javaclasses.model.repository;

import com.javaclasses.model.entity.Entity;
import com.javaclasses.model.entity.tinytype.EntityId;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract implementation of {@link Repository} interface
 */
public abstract class InMemoryRepository<TypeId extends EntityId, Type extends Entity>
        implements Repository<TypeId, Type> {

    private Map<TypeId, Type> entities = new ConcurrentHashMap<>();

    @Override
    public TypeId add(Type type) {

        final TypeId typeId = generateId();
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

    @Override
    public void delete(TypeId typeId) {
        entities.remove(typeId);
    }

    protected abstract TypeId generateId();
}

package com.javaclasses.model.repository;

import com.javaclasses.model.entity.Entity;
import com.javaclasses.model.entity.tinytype.EntityId;

import java.util.Collection;

/**
 * Basic interface for CRUD operations
 */
public interface Repository<TypeId extends EntityId, Type extends Entity> {

    TypeId add(Type type);

    Type findById(TypeId typeId);

    Collection<Type> findAll();

    void delete(TypeId typeId);
}

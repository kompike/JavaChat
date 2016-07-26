package com.javaclasses.model.repository;

import java.util.Collection;

/**
 * Basic interface for CRUD operations
 */
public interface Repository<Type, TypeId> {

    TypeId add(Type type);

    Type findById(TypeId typeId);

    Collection<Type> findAll();

    void delete(Type type);
}

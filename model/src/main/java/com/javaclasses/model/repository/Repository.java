package com.javaclasses.model.repository;

import java.util.Collection;

/**
 * Basic interface for CRUD operations
 */
public interface Repository<Type, Id> {

    Id add(Type type);

    Type find(Id typeId);

    Collection<Type> findAll();
}

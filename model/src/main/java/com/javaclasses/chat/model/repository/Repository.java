package com.javaclasses.chat.model.repository;

import com.javaclasses.chat.model.entity.Entity;
import com.javaclasses.chat.model.entity.tinytype.EntityId;

import java.util.Collection;

/**
 * Basic interface for CRUD operations
 */
public interface Repository<TypeId extends EntityId, Type extends Entity> {

    /**
     * Adds new entity to repository
     * @param type Entity to be added
     * @return Id of added entity
     */
    TypeId add(Type type);

    /**
     * Searches for entity in repository by given id
     * @param typeId Entity's id
     * @return Needed entity
     */
    Type findById(TypeId typeId);

    /**
     * Get all entities from repository
     * @return Collection of entities
     */
    Collection<Type> findAll();

    /**
     * Delete given entity from repository
     * @param typeId Id of entity to be deleted
     */
    void delete(TypeId typeId);
}

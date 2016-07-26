package com.javaclasses.model.entity;

import com.javaclasses.model.entity.tynitype.EntityId;

/**
 * Abstract instance of entity
 */
public interface Entity<TypeId> {

    TypeId getId();

    void setId(TypeId id);
}

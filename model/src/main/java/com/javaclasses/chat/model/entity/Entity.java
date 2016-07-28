package com.javaclasses.chat.model.entity;

import com.javaclasses.chat.model.entity.tinytype.EntityId;

/**
 * Abstract entity interface
 */
public interface Entity<TypeId extends EntityId> {

    TypeId getId();

    void setId(TypeId id);
}

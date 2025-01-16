package me.jisung.springplayground.common.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.io.Serializable;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends SimpleBaseEntity implements Serializable {

    @Enumerated(EnumType.STRING)
    private EntityStatus status;
}

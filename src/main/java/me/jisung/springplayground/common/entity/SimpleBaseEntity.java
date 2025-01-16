package me.jisung.springplayground.common.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import me.jisung.springplayground.common.util.DateUtil;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public class SimpleBaseEntity implements Serializable {

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        ZonedDateTime now = DateUtil.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = DateUtil.now();
    }

}

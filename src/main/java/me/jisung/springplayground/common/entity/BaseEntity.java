package me.jisung.springplayground.common.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import lombok.Getter;
import me.jisung.springplayground.common.util.DateUtil;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private String createdAt;
    private String updatedAt;

    @PrePersist
    public void prePersist() {
        String now = DateUtil.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = DateUtil.now();
    }
}
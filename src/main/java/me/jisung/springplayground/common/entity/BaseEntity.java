package me.jisung.springplayground.common.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import me.jisung.springplayground.common.util.DateUtil;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private String createdAt;
    private String updatedAt;

    @PrePersist
    public void prePersist() {
        String now = this.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = this.now();
    }

    private String now() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(DateUtil.DATE_TIME_ZONE));
        return now.format(DateTimeFormatter.ofPattern(DateUtil.DATE_TIME_FORMAT));
    }
}
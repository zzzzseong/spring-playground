package me.jisung.springplayground.common;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import me.jisung.springplayground.common.util.DateUtil;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Expose
    @Column(name = "created_at")
    private String createdAt;

    @Expose
    @Column(name = "updated_at")
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
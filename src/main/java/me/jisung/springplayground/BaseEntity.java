package me.jisung.springplayground;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public class BaseEntity {

    @CreatedDate
    private String createdAt;

    @LastModifiedDate
    private String modifiedAt;
}

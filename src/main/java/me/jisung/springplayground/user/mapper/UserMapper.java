package me.jisung.springplayground.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.user.dto.UserRequest;
import me.jisung.springplayground.user.dto.UserResponse;
import me.jisung.springplayground.user.entity.UserEntity;
import me.jisung.springplayground.user.entity.UserRole;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserEntity toEntity(UserRequest request, String password) {
        return UserEntity.builder()
                .email(request.getEmail())
                .password(password)
                .role(UserRole.USER)
                .build();
    }
    public static UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .build();
    }
    public static UserResponse toTokenResponse(String accessToken) {
        return UserResponse.builder()
                .accessToken(accessToken)
                .build();
    }

}

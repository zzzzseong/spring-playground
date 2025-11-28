package me.jisung.springplayground.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.util.SecurityUtil;
import me.jisung.springplayground.user.dto.UserRequest;
import me.jisung.springplayground.user.dto.UserResponse;
import me.jisung.springplayground.user.entity.UserEntity;
import me.jisung.springplayground.user.mapper.UserMapper;
import me.jisung.springplayground.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ApiResponse<Void> create(@Validated(UserRequest.Create.class) @RequestBody UserRequest request) {
        userService.create(request);
        return success();
    }

    @PostMapping("/token")
    public ApiResponse<UserResponse> getToken(@Validated(UserRequest.Token.class) @RequestBody UserRequest request) {
        return success(userService.getToken(request));
    }

    @GetMapping("")
    public ApiResponse<UserResponse> getUser() {
        UserEntity user = SecurityUtil.getAuthenticatedUser();
        return success(UserMapper.toResponse(user));
    }
}

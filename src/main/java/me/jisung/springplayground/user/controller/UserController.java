package me.jisung.springplayground.user.controller;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.user.dto.UserRequest;
import me.jisung.springplayground.user.dto.UserResponse;
import me.jisung.springplayground.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j(topic = "UserController")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ApiResponse<Void> create(@Validated(UserRequest.Create.class) @RequestBody UserRequest request) {
        userService.create(request);
        return success();
    }

    @GetMapping("/token")
    public ApiResponse<UserResponse> getToken(@Validated(UserRequest.Token.class) @RequestBody UserRequest request) {
        return success(userService.getToken(request));
    }
}

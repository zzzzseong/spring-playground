package me.jisung.springplayground.common.controller;

import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@RestController
@RequestMapping("/api/app")
@Slf4j(topic = "AppController")
public class AppController {

    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return success();
    }
}

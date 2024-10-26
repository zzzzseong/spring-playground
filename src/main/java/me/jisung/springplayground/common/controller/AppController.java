package me.jisung.springplayground.common.controller;

import static me.jisung.springplayground.common.util.ApiResponseUtil.success;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@Slf4j(topic = "AppController")
public class AppController {

    @GetMapping("/health")
    public String health() {
        return success();
    }
}

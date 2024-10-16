package me.jisung.springplayground.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.jisung.springplayground.common.util.ApiResponseUtil.success;

@RestController
@Slf4j(topic = "AppController")
public class AppController {

    @GetMapping("/health")
    public String health() {
        log.info("health");
        return success();
    }
}

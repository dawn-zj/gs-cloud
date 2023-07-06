package com.gs.webserver.controller;

import io.github.yedaxia.apidocs.Ignore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试
 */
@Ignore
@Controller
public class TestController {

    /**
     * 测试
     * @return
     */
    @GetMapping("/test")
    public String test() {
        return "Welcome to spring boot!";
    }
}

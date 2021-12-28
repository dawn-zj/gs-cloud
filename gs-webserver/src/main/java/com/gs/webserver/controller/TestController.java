package com.gs.webserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administator
 * @date 2021-11-22 16:09
 * @Description
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Welcome to spring boot!";
    }
}

package com.gs.webserver.controller;

import com.gs.webserver.entity.to.response.ResponseTo;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * @author Administator
 */
@Ignore
@RestController
public class TestController {

    /**
     * 测试
     * @return 测试结果
     */
    @GetMapping("/test")
    public ResponseTo<String> test() {
        return ResponseTo.success("Welcome to spring boot!");
    }
}

package com.gs.gswebserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class GsWebserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsWebserverApplication.class, args);
    }

}

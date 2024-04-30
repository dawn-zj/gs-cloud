package com.gs.webserver.service.impl;

import com.gs.webserver.service.IEmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {
    @Override
    public void send(String message) {
        System.out.println("send: " + message);
    }
}

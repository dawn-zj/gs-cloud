package com.gs.webserver.service.impl;

import com.gs.webserver.service.IEmailService;

public class EmailServiceImpl implements IEmailService {
    @Override
    public void send(String message) {
        System.out.println("send: " + message);
    }
}

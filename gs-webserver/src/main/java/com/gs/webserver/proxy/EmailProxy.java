package com.gs.webserver.proxy;

import com.gs.webserver.service.IEmailService;
import com.gs.webserver.service.impl.EmailServiceImpl;

public class EmailProxy implements IEmailService {
    private final EmailServiceImpl emailService;

    public EmailProxy(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(String message) {
        // 调用方法之前，我们可以添加自己的操作
        System.out.println("before method send()");
        emailService.send(message);
        //调用方法之后，我们同样可以添加自己的操作
        System.out.println("after method send()");
    }
}

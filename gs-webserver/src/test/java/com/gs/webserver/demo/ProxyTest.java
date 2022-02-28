package com.gs.webserver.demo;

import com.gs.webserver.proxy.EmailProxy;
import com.gs.webserver.proxy.JdkProxyFactory;
import com.gs.webserver.service.IEmailService;
import com.gs.webserver.service.impl.EmailServiceImpl;
import org.junit.Test;

public class ProxyTest {

    @Test
    public void staticProxy() {
        EmailProxy proxy = new EmailProxy(new EmailServiceImpl());
        proxy.send("静态代理测试");
    }

    @Test
    public void dynamicProxy() {
        IEmailService proxy = (IEmailService)JdkProxyFactory.getProxy(new EmailServiceImpl());
        proxy.send("动态代理测试");
    }
}

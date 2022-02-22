package com.gs.webserver.demo;

import com.gs.webserver.proxy.EmailProxy;
import com.gs.webserver.service.impl.EmailServiceImpl;
import org.junit.Test;

public class ProxyTest {

    @Test
    public void staticProxy() {
        EmailProxy proxy = new EmailProxy(new EmailServiceImpl());
        proxy.send("哈哈哈");
    }

    @Test
    public void dynamicProxy() {

    }
}

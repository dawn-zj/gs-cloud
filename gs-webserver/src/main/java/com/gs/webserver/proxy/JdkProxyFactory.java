package com.gs.webserver.proxy;

import com.gs.webserver.proxy.handle.GsInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * 定义一个接口及其实现类(被代理对象)；
 * 实现 InvocationHandler 接口并重写invoke方法，在 invoke 方法中我们会调用原生方法（被代理类的方法）并自定义一些处理逻辑；
 * 通过 Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h) 方法创建代理对象；
 */
public class JdkProxyFactory {

    /**
     * 创建一个代理对象
     * @param target
     * @return
     */
    public static Object getProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), // 目标类的类加载
                target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
                new GsInvocationHandler(target)   // 代理对象对应的自定义 InvocationHandler
        );
    }
}


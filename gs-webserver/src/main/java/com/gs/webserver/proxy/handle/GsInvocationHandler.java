package com.gs.webserver.proxy.handle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GsInvocationHandler implements InvocationHandler {

    /**
     * 被代理的真实对象
     */
    private final Object target;

    public GsInvocationHandler(Object target) {
        this.target = target;
    }
    /**
     *
     * @param proxy 动态生成的代理类
     * @param method 与代理类对象调用的方法相对应
     * @param args 当前 method 方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 调用方法之前，我们可以添加自己的操作
        System.out.println("before method " + method.getName());
        Object result = method.invoke(target, args);
        // 调用方法之后，我们同样可以添加自己的操作
        System.out.println("after method " + method.getName());
        return result;
    }
}

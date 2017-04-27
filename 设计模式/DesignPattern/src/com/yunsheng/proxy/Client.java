package com.yunsheng.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by shengyun on 16/9/15.
 */
public class Client {

    public static void main(String[] args) {
        RealSubject subject = new RealSubject();
        DynamicProxy dynamicProxy = new DynamicProxy();
        dynamicProxy.setSubject(subject);

        // 
        Subject proxyInstance = (Subject) Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(), dynamicProxy);

        System.out.println(proxyInstance.getClass().getName());
        proxyInstance.hello("yunsheng");
    }
}

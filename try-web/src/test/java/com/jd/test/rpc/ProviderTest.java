package com.jd.test.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProviderTest {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-config-provider.xml");
        context.start();
        System.in.read(); // 按任意键退出
    }
}

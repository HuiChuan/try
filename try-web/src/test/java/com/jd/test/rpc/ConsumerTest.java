package com.jd.test.rpc;

import com.jd.rpc.HelloRpc;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerTest {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-config-consumer.xml");
        context.start();
        HelloRpc helloRpc = (HelloRpc)context.getBean("helloRpcConsumer"); // 获取远程服务代理
        String msg = helloRpc.say(); // 执行远程方法
        System.out.println(msg); // 显示调用结果
    }
}

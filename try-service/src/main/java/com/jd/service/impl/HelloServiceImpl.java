package com.jd.service.impl;

import com.jd.domain.Hello;
import com.jd.service.HelloService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String say(){
        log.info("info HelloServiceImpl#say");
        log.error("error HelloServiceImpl#say");

        Hello hello=new Hello("abc");
        return "I'm " + hello.getName();
    }
}

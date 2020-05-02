package com.jd.manager.impl;

import com.jd.dao.HelloMapper;
import com.jd.domain.dto.HelloDTO;
import com.jd.entity.Hello;
import com.jd.manager.HelloManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloManagerImpl implements HelloManager {

    @Autowired
    HelloMapper helloDao;

    public HelloDTO getHelloByName(String name){
        Hello hello = helloDao.selectHelloByName(name);
        if (null==hello) {
            return null;
        }
        HelloDTO helloDTO = new HelloDTO();
        helloDTO.setName(hello.getName());
        helloDTO.setMsg(hello.getMsg());
        return helloDTO;
    }

    public Integer saveHello(HelloDTO helloDTO){
        Hello hello=new Hello();
        hello.setName(helloDTO.getName());
        hello.setMsg(helloDTO.getMsg());
        return helloDao.insertHello(hello);
    }
}
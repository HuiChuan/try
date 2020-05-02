package com.jd.service.impl;

import com.jd.domain.bo.HelloBO;
import com.jd.domain.dto.HelloDTO;
import com.jd.domain.vo.HelloVO;
import com.jd.manager.HelloManager;
import com.jd.service.HelloService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    HelloManager helloManager;

    @Override
    public HelloVO say(String name){
        log.info("info HelloServiceImpl#say");

        HelloDTO helloDTO = helloManager.getHelloByName(name);
        if(null==helloDTO){
            return null;
        }
        HelloVO helloVO = new HelloVO();
        helloVO.setMsg(helloDTO.getMsg());
        return helloVO;
    }

    @Override
    public boolean save(HelloBO helloBO){
        log.info("info HelloServiceImpl#save");

        HelloDTO helloDTO = new HelloDTO();
        helloDTO.setName(helloBO.getName());
        helloDTO.setMsg(helloBO.getMsg());
        Integer res = helloManager.saveHello(helloDTO);
        if (res!=null && res>0) {
            return true;
        }
        return false;
    }
}

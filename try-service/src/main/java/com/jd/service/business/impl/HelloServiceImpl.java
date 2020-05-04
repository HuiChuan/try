package com.jd.service.business.impl;

import com.jd.domain.bo.HelloBO;
import com.jd.domain.dto.HelloDTO;
import com.jd.domain.vo.HelloVO;
import com.jd.manager.business.HelloManager;
import com.jd.manager.cache.CacheManager;
import com.jd.service.business.HelloService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    HelloManager helloManager;
    @Autowired
    CacheManager cacheManager;

    @Override
    public HelloVO say(String name){
        log.info("HelloServiceImpl#say executing, name=" + name);

        HelloDTO helloDTO = (HelloDTO)cacheManager.getObject(name);
        if (null == helloDTO) {
            helloDTO = helloManager.getHelloByName(name);
            if(null==helloDTO){
                return null;
            }
            cacheManager.setObject(name, helloDTO);
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

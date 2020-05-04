package com.jd.manager.business;

import com.jd.domain.dto.HelloDTO;

public interface HelloManager {
    HelloDTO getHelloByName(String name);
    Integer saveHello(HelloDTO helloDTO);
}
package com.jd.service.business;

import com.jd.domain.bo.HelloBO;
import com.jd.domain.vo.HelloVO;

public interface HelloService {
    HelloVO say(String name);
    boolean save(HelloBO helloBO);
}

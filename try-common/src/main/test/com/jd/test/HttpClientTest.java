package com.jd.test;

import com.jd.utils.http.HttpClientUtil;
import com.jd.utils.http.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class HttpClientTest {
    @Test
    public void httpTest(){
//        Header[] headers = new Header[2];
//        headers[0] = new BasicHeader("referer", "https://m.zhibo8.cc/");
//        headers[1] = new BasicHeader("token", "123-456");

        HttpResult httpResult = HttpClientUtil.sendGet("http://bifen4m.qiumibao.com/json/list.htm" ,null, null, null, "UTF-8");
        log.info("statusCode:" + httpResult.getStatusCode());
        log.info("stringContent:" + httpResult.getStringContent());
    }
}
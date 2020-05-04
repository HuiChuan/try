package com.jd.web;

import com.jd.domain.bo.HelloBO;
import com.jd.service.business.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    HelloService helloService;

    @RequestMapping("/say")
    public ModelAndView hello(@RequestParam("name") String name){
        ModelAndView mav=new ModelAndView("hello");
        mav.addObject("name", name);
        mav.addObject("hello", helloService.say(name));
        return mav;
    }

    @ResponseBody
    @RequestMapping(path = "/save", produces = "text/html;charset=UTF-8")
    public String hello(@RequestParam(value="name",required=true) String name, @RequestParam(value="msg",required=true) String msg){
        HelloBO helloBO = new HelloBO();
        helloBO.setName(name);
        helloBO.setMsg(msg);
        if (helloService.save(helloBO)) {
            return "SUCCESS";
        }
        return "FAIL";
    }
}

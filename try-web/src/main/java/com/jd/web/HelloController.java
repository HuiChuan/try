package com.jd.web;

import com.jd.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/mvc")
public class HelloController {

    @Autowired
    HelloService helloService;

    @RequestMapping("/hello")
    public ModelAndView hello(){
        ModelAndView mav=new ModelAndView();
        mav.setViewName("hello");
        mav.addObject("val", helloService.say());
        return mav;
    }
}

package com.su.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping
    public String index(){
        logger.debug("进入首页");
        return "index";

    }

    @ResponseBody
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
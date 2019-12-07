package com.chengm.pirate.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: CmPirate
 * description: 测试
 * author: ChengMo
 * create: 2019-11-30 17:05
 **/
@Controller
@RequestMapping("/test")
public class TestCmPirate {

    @ResponseBody
    @RequestMapping("/pirate")
    public String testCmPro() {
        return "Hello World";
    }

}

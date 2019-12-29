package com.zxx.wechart.store.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/29 0029 17:51
 * @Description: 用于跳转页面的controller
 */
@Controller
@RequestMapping("/page")
public class PageController {

    Logger logger = LogManager.getLogger(this.getClass());

    @RequestMapping("/userLogin")
    public String loginPage() {
        logger.info("登录页面");
        return "login.html";
    }

}

package cn.coderjia.fp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author CoderJiA
 * @Description UserController
 * @Date 18/5/19 下午11:07
 **/
@RestController
public class UserController {

    @GetMapping("user")
    public String user() {
        return "welcome to user app.";
    }


}

package com.hainu.controller.user;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PUserController {

    @GetMapping("/1")
    @RequiresAuthentication
    public String text(){
        return "111111111";
    }
}

package com.hainu;

import com.hainu.controller.analysis.SysInfoController;
import com.hainu.system.service.LoginInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HainuAdminApplicationTests {


    @Autowired
    LoginInfoService ls;

    @Autowired
    SysInfoController sc;

    @Test
    void contextLoads() {
    }

    @Test
    void testList() {


        Object o = sc.test2();
        System.out.println(o);
    }

}

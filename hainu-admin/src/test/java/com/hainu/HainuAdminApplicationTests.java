package com.hainu;

import com.hainu.system.service.LoginInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HainuAdminApplicationTests {



    @Autowired
    LoginInfoService ls;

    @Test
    void contextLoads() {
    }

    @Test
    void testList() {


        System.out.println(ls.list());
    }

}

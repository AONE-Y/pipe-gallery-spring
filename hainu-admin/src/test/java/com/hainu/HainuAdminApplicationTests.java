package com.hainu;

import com.hainu.system.service.user.PUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HainuAdminApplicationTests {
    @Autowired
    PUsersService ps;

    @Test
    void contextLoads() {
    }
    @Test
    void testList(){
        System.out.println(ps.list());
    }

}

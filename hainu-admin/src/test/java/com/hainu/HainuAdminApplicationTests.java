package com.hainu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.system.dao.LoginInfoMapper;
import com.hainu.system.service.LoginInfoService;
import com.hainu.system.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@SpringBootTest
class HainuAdminApplicationTests {

    @Autowired
    LoginInfoMapper lf;

    @Autowired
    LoginInfoService ls;

    @Test
    void contextLoads() {
    }

    @Test
    void testList() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse("2021-08-24 20:00:33",dtf);
        LocalDateTime now=LocalDateTime.now() ;
        start=null;
        now=null;
//        String[] date={start.toString(),now.toString()};
        System.out.println(start);
        System.out.println(now);
        QueryWrapper queryWrapper = new QueryWrapper<>();
//        queryWrapper.between("login_time",start,now);
        ls.list(queryWrapper);
        System.out.println(ls.list(queryWrapper));
    }

}

package com.hainu;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
public class HainuAdminApplication  {

    public static void main(String[] args) {
        SpringApplication.run(HainuAdminApplication.class, args);
    }


}

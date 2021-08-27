package com.hainu.controller.analysis;

import cn.hutool.http.HttpRequest;
import cn.hutool.system.oshi.OshiUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.controller.analysis
 * @Date：2021/8/26 16:07
 * @Author：yy188
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: yy188
 */
@RestController
@RequestMapping("/sys")
public class SysInfoController {

    @GetMapping("/test")
    public String test(){
        String st=HttpRequest.get("http://mqtt.xinxi.ml:8081/api/v4")
                .basicAuth("admin","public")
                .execute().body();
        return st;
    }
    @GetMapping("/test2")
    public Object test2(){
        ArrayList<Object> re = new ArrayList<>();
        // re.add(OshiUtil.getMemory());
        re.add(OshiUtil.getProcessor().getSystemLoadAverage(3));
        // re.add(OshiUtil.getProcessor().g);
        // re.add(OshiUtil.getDiskStores());
        re.add(OshiUtil.getCpuInfo().getUsed());
        re.add(OshiUtil.getCpuInfo().getToTal());
        re.add(OshiUtil.getCpuInfo().getUsed()/OshiUtil.getCpuInfo().getToTal());
        // re.add(OshiUtil.getNetworkIFs());
        // SysInfoUtil.test();
        return re;
    }

}

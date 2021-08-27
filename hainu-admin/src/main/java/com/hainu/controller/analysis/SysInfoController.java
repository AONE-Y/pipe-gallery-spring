package com.hainu.controller.analysis;

import cn.hutool.http.HttpRequest;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.hainu.common.util.utils.SysInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.List;

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
        re.add(OshiUtil.getProcessor().getSystemLoadAverage(3));//负载状态
        // re.add(OshiUtil.getProcessor().g);
        // re.add(OshiUtil.getDiskStores());
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        // double used = cpuInfo.getUsed();
        // double toTal = cpuInfo.getToTal();
        // double sys = cpuInfo.getSys();
        //cpu
        re.add(cpuInfo.getCpuModel());
        re.add(cpuInfo.getFree());
        re.add(100-cpuInfo.getFree());
        // re.add(OshiUtil.getNetworkIFs());
        // SysInfoUtil.test();

        re.add(OshiUtil.getMemory().getTotal()/1024.0/1024.0/1024.0);
        re.add(OshiUtil.getMemory().getAvailable()/1024/1024/1024);


        return re;
    }

    @GetMapping("test3")
    public List<Object> test3(){
        List<Object> list = new ArrayList<>();
        List<OSFileStore> fileSystemInfo = SysInfoUtil.getFileSystemInfo();
        for (OSFileStore osFileStore : fileSystemInfo) {
            list.add(osFileStore.getDescription());
        }
        return list;
    }
}

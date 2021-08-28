package com.hainu.controller.analysis;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.hainu.common.dto.SysInfoDto;
import com.hainu.common.lang.Result;
import com.hainu.common.util.SysInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.util.FormatUtil;

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
@RequestMapping("/aly")
public class SysInfoController {

    @GetMapping("/sys")
    public Result<?> getSysInfo(){
        SysInfoDto sysInfo= new SysInfoDto();
        //负载信息
        sysInfo.setLoadAverage(OshiUtil.getProcessor().getSystemLoadAverage(3));
        //返回cpu信息
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        sysInfo.setCpuInfo(cpuInfo);
        //返回内存信息
        String[] memoryInfo={FormatUtil.formatBytes(OshiUtil.getMemory().getTotal()),FormatUtil.formatBytes(OshiUtil.getMemory().getAvailable())};
        // sysInfo.setGlobalMemoryInfo(memoryInfo);
        //返回硬盘信息
        sysInfo.setHardDiskInfo(SysInfoUtil.getFileSystemInfo());
        return new Result<>().success().put(sysInfo);
    }
    @GetMapping("/test2")
    public Object test2(){
        ArrayList<Object> re = new ArrayList<>();
        // re.add(OshiUtil.getMemory());
        // re.add();//负载状态
        // re.add(OshiUtil.getProcessor().g);
        // re.add(OshiUtil.getDiskStores());
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        // double used = cpuInfo.getUsed();
        // double toTal = cpuInfo.getToTal();
        // double sys = cpuInfo.getSys();
        //cpu
        re.add(cpuInfo.getCpuModel());
        // re.add();
        re.add(100-cpuInfo.getFree());
        // re.add(OshiUtil.getNetworkIFs());
        // SysInfoUtil.test();

        re.add(FormatUtil.formatBytes(OshiUtil.getMemory().getTotal()));
        re.add(FormatUtil.formatBytes(OshiUtil.getMemory().getAvailable()));


        return re;
    }

    @GetMapping("test3")
    public List<String> test3(){

        return null;
    }
}

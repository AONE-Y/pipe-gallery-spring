package com.hainu.controller.info;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.common.dto.DateRangeDto;
import com.hainu.common.lang.Result;
import com.hainu.system.entity.LoginInfo;
import com.hainu.system.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/info")
public class InfoController {

    @Autowired
    LoginInfoService logininfoService;


    /**
     * @param @param date 日期
     * @return @return {@link Result<?> }
     * @description: 登录信息
     * @author： ANONE
     * @date： 2021/08/28
     */
    @CrossOrigin
    @SaCheckLogin
    @PostMapping("logInfo")
    public Result< ? > loginInfo(@RequestBody(required = false) DateRangeDto date) {
        QueryWrapper<LoginInfo> queryWrapper =null;
        if (date != null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.between("login_time",date.getBeginDate(),date.getEndDate());
        }
        return new Result<>().success().put(logininfoService.list(queryWrapper));
    }


    /**
     * @param
     * @return @return {@link Result<?> }
     * @description: 获取系统信息
     * @author： ANONE
     * @date： 2021/08/28
     */
    @RequestMapping("/sysInfo")
    public Result<?> getSysInfo(){
        // SysInfoDto sysInfo= new SysInfoDto();
        // //负载信息
        // sysInfo.setLoadAverage(OshiUtil.getProcessor().getSystemLoadAverage(3));
        // //返回cpu信息
        // CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        // sysInfo.setCpuInfo(cpuInfo);
        // //返回内存信息
        // double[] memoryInfo={OshiUtil.getMemory().getTotal(),OshiUtil.getMemory().getAvailable()};
        // sysInfo.setGlobalMemoryInfo(memoryInfo);
        // //返回硬盘信息
        // sysInfo.setHardDiskInfo(SysInfoUtil.getFileSystemInfo());
        // return new Result<>().success().put(sysInfo);
        String btSign = "2bLKdfCQZHZFcTiM7TlbbXTy9rTVvNKu";
        String url = "http://192.168.2.219:8888/system?action=GetNetWork";
        String timestamp = (System.currentTimeMillis()+"");
        String md5Sign = SecureUtil.md5(btSign);
        String token = SecureUtil.md5(timestamp + md5Sign);
        String json = "request_time="+timestamp+"&request_token="+token;
        String responseText = HttpRequest.post(url)
                .body(json)
                .execute().body();
        JSON parse = JSONUtil.parse(responseText);
        return new Result<>().success().put(parse);
    }

    @GetMapping("/emqx/broker")
    public Result<?> getEmqInfo(){
        String brokerInfo = HttpRequest.get("http://mqtt.xinxi.ml:8081/api/v4/brokers")
                .basicAuth("admin", "public")
                .execute().body();
        JSON brokerInfoJson = (JSON) JSONUtil.parse(brokerInfo).getByPath("data");
        // brokerInfoJson.getByPath("data");
        return new Result<>().success().put(brokerInfoJson);

    }



}

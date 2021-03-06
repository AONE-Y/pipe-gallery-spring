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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Autowired
    LoginInfoService logininfoService;

    @Value("${info.baseBtUrl}")
    private String baseBtUrl;
    @Value("${info.baseEmqUrl}")
    private String baseEmqUrl;


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
        QueryWrapper<LoginInfo> queryWrapper =Optional.ofNullable(date)
                                                .map(d -> new QueryWrapper<LoginInfo>()
                                                .between("login_time",d.getBeginDate(),d.getEndDate()))
                                                .orElseGet(()->null);
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

        //通过宝塔获取服务器信息
        String btSign = "2bLKdfCQZHZFcTiM7TlbbXTy9rTVvNKu";
        String timestamp = (System.currentTimeMillis()+"");
        String md5Sign = SecureUtil.md5(btSign);
        String token = SecureUtil.md5(timestamp + md5Sign);
        String json = "request_time="+timestamp+"&request_token="+token;
        String responseText = HttpRequest.post(baseBtUrl+"GetNetWork")
                .body(json)
                .execute().body();
        JSON parse = JSONUtil.parse(responseText);



        JSON infoJson=JSONUtil.createObj();

        JSONUtil.putByPath(infoJson,"sysInfo",parse);
        Map<String, String> emqsInfo = new HashMap<>();
        emqsInfo.put("/brokers","emqBrokerInfo");
        emqsInfo.put("/clients","emqClientsInfo");
        emqsInfo.put("/metrics","emqMetricsInfo");

        emqsInfo.forEach((key,value)->{
            //通过emqx获取mqtt服务器及客户端信息
            String emqInfo = HttpRequest.get(baseEmqUrl+key)
                    .basicAuth("admin", "public")
                    .execute().body();
            JSON emqInfoJson = (JSON) JSONUtil.parse(emqInfo).getByPath("data");
            JSONUtil.putByPath(infoJson,value,emqInfoJson);
        });

        //通过emqx获取mqtt服务器及客户端信息
        // String brokerInfo = HttpRequest.get(baseEmqUrl+"/brokers")
        //         .basicAuth("admin", "public")
        //         .execute().body();
        // JSON brokerInfoJson = (JSON) JSONUtil.parse(brokerInfo).getByPath("data");
        //
        // String clientsInfo = HttpRequest.get(baseEmqUrl+"/clients")
        //         .basicAuth("admin", "public")
        //         .execute().body();
        // JSON clientsInfoJson = JSONUtil.parse(clientsInfo);
        //
        // JSON clientsDataInfoJson =(JSON) clientsInfoJson.getByPath("data");



        // JSONUtil.putByPath(infoJson,"emqBrokerInfo",brokerInfoJson);
        // JSONUtil.putByPath(infoJson,"emqClientsInfo",clientsDataInfoJson);
        return new Result<>().success().put(infoJson);
    }


    @RequestMapping("emqSubtopInfo")
    public Result<?> getSubTop( String clientid){
        String emqInfo = HttpRequest.get(baseEmqUrl+"/subscriptions/"+clientid)
                .basicAuth("admin", "public")
                .execute().body();
        JSON emqInfoJson = (JSON) JSONUtil.parse(emqInfo).getByPath("data");
        return new Result<>().success().put(emqInfoJson);

    }


    // @GetMapping("/emqx/broker")
    // public Result<?> getEmqInfo(){
    //     String brokerInfo = HttpRequest.get("http://mqtt.xinxi.ml:8081/api/v4/brokers")
    //             .basicAuth("admin", "public")
    //             .execute().body();
    //     JSON brokerInfoJson = (JSON) JSONUtil.parse(brokerInfo).getByPath("data");
    //
    //     // brokerInfoJson.getByPath("data");
    //     return new Result<>().success().put(brokerInfoJson);
    //
    // }



}

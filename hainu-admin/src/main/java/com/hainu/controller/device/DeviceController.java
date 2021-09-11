package com.hainu.controller.device;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.common.lang.Result;
import com.hainu.system.config.mqtt.MqttPushClient;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.controller.device
 * @Date：2021/9/3 10:25
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @Value("${info.baseEmqUrl}")
    private String baseEmqUrl;

    @Autowired
    private DeviceListService deviceListService;

    @Autowired
    private DeviceCurrentService deviceCurrentService;

    
    @Autowired
    private DeviceLogService deviceLogService;

    /**
     * @param
     * @return @return {@link Result<?> }
     * @description: 订阅主题
     * @author： ANONE
     * @date： 2021/09/05
     */
    @GetMapping("reSubTopic")
    public Result<?> reSubTopic() {


        String unSubInfo = HttpRequest.get(baseEmqUrl + "/routes")
                .basicAuth("admin", "public")
                .execute().body();
        List<JSON> unSubdata = JSONUtil.parseObj(unSubInfo).getByPath("data", ArrayList.class);

        unSubdata.stream()
                .map((e) -> e
                        .getByPath("topic").toString())
                .forEach((o) -> mqttPushClient.unSubscribe(o));
        deviceListService.truncateData();
        deviceCurrentService.truncateData();


        String subInfo = HttpRequest.get(baseEmqUrl + "/routes")
                .basicAuth("admin", "public")
                .execute().body();
        List<JSON> subData = JSONUtil.parseObj(subInfo).getByPath("data", ArrayList.class);

        subData.stream()
                .map((e) -> e
                        .getByPath("topic").toString())
                .forEach((o) -> {
                    mqttPushClient.subscribe(o, 1);
                    saveTopic(o);
                });

        return new Result<>().success().put("话题刷新成功");
    }


    /**
     * @param topic 主题
     * @return
     * @description: 保存主题
     * @author： ANONE
     * @date： 2021/09/05
     */
    public void saveTopic(String topic) {
        DeviceList deviceList = new DeviceList();
        deviceList.setWsTopic(topic);
        String[] topicSplit = topic.split("/");
        String wsName= topicSplit[topicSplit.length - 1];

        DeviceCurrent deviceCurrent = new DeviceCurrent();

        if (topicSplit.length>1) {
            deviceCurrent.setWsName(wsName);
            deviceCurrentService.save(deviceCurrent);
        }

        deviceList.setWsName(wsName);

        deviceListService.save(deviceList);

    }

    @GetMapping("getDeviceCurrent")
    public Result<?> getDeviceCurrent(String wsName,String node){
        QueryWrapper<DeviceCurrent> deviceCurrentQueryWrapper=new QueryWrapper<>();
        if (wsName != null) {
            deviceCurrentQueryWrapper.eq("ws_name",wsName);
        }
        if (node != null) {
            deviceCurrentQueryWrapper.eq("node",node);
        }
        if(wsName==null&&node==null){
            deviceCurrentQueryWrapper=null;
        }

        List<DeviceCurrent> deviceCurrentsInfo = deviceCurrentService.list(deviceCurrentQueryWrapper);
        return new Result<>().success().put(deviceCurrentsInfo);
    }

    @RequestMapping("getDeviceLog")
    public Result<?> getDeviceLog(Integer day, String wsName,String node){
        QueryWrapper<DeviceLog> deviceLogWrapper=new QueryWrapper<DeviceLog>();
        if (wsName != null) {
            deviceLogWrapper.eq("ws_name",wsName);
        }
        if (node != null) {
            deviceLogWrapper.eq("node",node);
        }
        if (wsName==null&&node == null) {
            deviceLogWrapper=null;
        }

        LocalDate date = LocalDate.now();
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());

        LocalDate lastDay = Optional.ofNullable(day)
                .map(e -> {
                    if (e<30){
                        return date.plusDays(e);
                    }
                     return date.with(TemporalAdjusters.lastDayOfMonth());
                })
                .orElseGet(() -> date.with(TemporalAdjusters.lastDayOfMonth()));

        List<DeviceLog> deviceLogs = deviceLogService.selectByAvg(firstDay, lastDay,wsName,node);
        return new Result<>().success().put(deviceLogs);
    }
    


}

package com.hainu.controller.device;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.common.dto.DeviceSwitchDto;
import com.hainu.common.dto.QueryDeviceDto;
import com.hainu.common.lang.Result;
import com.hainu.system.config.tcp.TcpConnect;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
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
    // @Autowired
    // private MqttPushClient mqttPushClient;

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
    // @GetMapping("reSubTopic")
    // public Result<?> reSubTopic() {
    //
    //
    //     String unSubInfo = HttpRequest.get(baseEmqUrl + "/routes")
    //             .basicAuth("admin", "public")
    //             .execute().body();
    //     List<JSON> unSubdata = JSONUtil.parseObj(unSubInfo).getByPath("data", ArrayList.class);
    //
    //     unSubdata.stream()
    //             .map((e) -> e
    //                     .getByPath("topic").toString())
    //             .forEach((o) -> mqttPushClient.unSubscribe(o));
    //     deviceListService.truncateData();
    //     deviceCurrentService.truncateData();
    //
    //
    //     String subInfo = HttpRequest.get(baseEmqUrl + "/routes")
    //             .basicAuth("admin", "public")
    //             .execute().body();
    //     List<JSON> subData = JSONUtil.parseObj(subInfo).getByPath("data", ArrayList.class);
    //
    //     subData.stream()
    //             .map((e) -> e
    //                     .getByPath("topic").toString())
    //             .forEach((o) -> {
    //                 mqttPushClient.subscribe(o, 1);
    //                 saveTopic(o);
    //             });
    //
    //     return new Result<>().success().put("话题刷新成功");
    // }


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
        String wsName = topicSplit[topicSplit.length - 1];

        DeviceCurrent deviceCurrent = new DeviceCurrent();

        if (topicSplit.length > 1) {
            deviceCurrent.setWsName(wsName);
            deviceCurrentService.save(deviceCurrent);
        }

        deviceList.setWsName(wsName);

        deviceListService.save(deviceList);

    }

    // @GetMapping("getDeviceCurrent")
    // public Result<?> getDeviceCurrent(String wsName, String node) {
    //     QueryWrapper<DeviceCurrent> deviceCurrentQueryWrapper = new QueryWrapper<>();
    //     if (wsName != null) {
    //         deviceCurrentQueryWrapper.eq("ws_name", wsName);
    //     }
    //     if (node != null) {
    //         deviceCurrentQueryWrapper.eq("node", node);
    //     }
    //     if (wsName == null && node == null) {
    //         deviceCurrentQueryWrapper = null;
    //     }
    //
    //     List<DeviceCurrent> deviceCurrentsInfo = deviceCurrentService.list(deviceCurrentQueryWrapper);
    //     return new Result<>().success().put(deviceCurrentsInfo);
    // }

    // {
    //     "node" : "tt1",
    //         "temp":36,
    //         "humi":25,
    //         "llv":92,
    //         "gas":58,
    //         "O2":28,
    //         "guard" : 1,
    //         "smoke" : 1,
    //         "infra" : 1,
    //         "lighting" : 0,
    //         "fan" : 1,
    //         "waterpump" : 1
    // }


    @PostMapping("getDeviceCurrent")
    public Result<?> getDeviceCurrent(@RequestBody QueryDeviceDto queryDevice) {
        QueryWrapper<DeviceCurrent> deviceCurrentQueryWrapper = new QueryWrapper<>();

        if (queryDevice.getWsName() != null && !queryDevice.getWsName().equals("")) {
            deviceCurrentQueryWrapper.eq("ws_name", queryDevice.getWsName());

        }
        if (queryDevice.getNode() != null && !queryDevice.getNode().equals("")) {
            deviceCurrentQueryWrapper.eq("node", queryDevice.getNode());
        }
        if (queryDevice.getSw() != null && !queryDevice.getSw().equals("all")) {
            deviceCurrentQueryWrapper.eq("device_" + queryDevice.getSw(), 1);
        }
        if (queryDevice.getMeasure() != null) {

            deviceCurrentQueryWrapper.between("device_" + queryDevice.getMeasure()
                    , queryDevice.getStart() == null ? 0 : queryDevice.getStart()
                    , queryDevice.getEnd() == null ? 999 : queryDevice.getEnd());
        }


        deviceCurrentQueryWrapper.orderByAsc("ws_name");

        // if (wsName != null) {
        //     deviceCurrentQueryWrapper.eq("ws_name", wsName);
        // }
        // if (node != null) {
        //     deviceCurrentQueryWrapper.eq("node", node);
        // }
        // if (wsName == null && node == null) {
        //     deviceCurrentQueryWrapper = null;
        // }

        List<DeviceCurrent> deviceCurrentsInfo = deviceCurrentService.list(deviceCurrentQueryWrapper);
        deviceCurrentsInfo.forEach((deviceCurrentInfo)->{
            long interval = Duration.between(deviceCurrentInfo.getUpdateTime(), LocalDateTime.now()).toSeconds();
            deviceCurrentInfo.setStatus(interval<65 ?1:0);
        });
        return new Result<>().success().put(deviceCurrentsInfo);
    }

    @RequestMapping("getDeviceLog")
    public Result<?> getDeviceLog(Integer day, String wsName, String node) {
        QueryWrapper<DeviceLog> deviceLogWrapper = new QueryWrapper<DeviceLog>();
        if (wsName != null) {
            deviceLogWrapper.eq("ws_name", wsName);
        }
        if (node != null) {
            deviceLogWrapper.eq("node", node);
        }
        if (wsName == null && node == null) {
            deviceLogWrapper = null;
        }

        LocalDate date = LocalDate.now();
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());

        LocalDate lastDay = Optional.ofNullable(day)
                .map(e -> {
                    if (e < 30) {
                        return date.plusDays(e);
                    }
                    return date.with(TemporalAdjusters.lastDayOfMonth());
                })
                .orElseGet(() -> date.with(TemporalAdjusters.lastDayOfMonth()));

        List<DeviceLog> deviceLogs = deviceLogService.selectByAvg(firstDay, lastDay, wsName, node);
        return new Result<>().success().put(deviceLogs);
    }

    // @PostMapping("stateSwitch")
    // public Result<?> stateSwitch(DeviceCurrent deviceCurrent){
    //     UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
    //     deviceUpdate.eq("ws_name",deviceCurrent.getWsName());
    //     deviceCurrentService.update(deviceCurrent,deviceUpdate);
    //     return new Result<>().success().put("操作成功");
    // }

    @PostMapping("stateSwitch")
    public Result<?> stateSwitch(@RequestBody DeviceCurrent deviceCurrent) {
        DeviceSwitchDto deviceSwitchDto = DeviceSwitchDto.builder()
                .clientId("Monitor")
                .node(deviceCurrent.getNode())
                .smoke(deviceCurrent.getDeviceSmoke())
                .waterpump(deviceCurrent.getDeviceWaterpump())
                .infra(deviceCurrent.getDeviceInfra())
                .lighting(deviceCurrent.getDeviceLighting())
                .fan(deviceCurrent.getDeviceFan())
                .guard(deviceCurrent.getDeviceGuard())
                .build();

        // mqttPushClient.publish(1, true, "/dev/" + deviceCurrent.getWsName(), JSONUtil.toJsonStr(deviceSwitchDto));
        UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
        deviceUpdate.eq("ws_name", deviceCurrent.getWsName());
        deviceUpdate.eq("node", deviceCurrent.getNode());
        deviceCurrentService.update(deviceCurrent, deviceUpdate);
        return new Result<>().success().put("操作成功");
    }

    // @Autowired
    // TcpSever tcpSever;
    // @GetMapping("test")
    // public String test(){
    //     Socket socket = tcpSever.getSocket();
    //     tcpSever.sendMessage("147258");
    //     return "123";
    // }
    // @Autowired
    // ServerSocket1 serverSocket1;
    // @GetMapping("/test1")
    // public void test1(){
    //     serverSocket1.ServerSocketDemo();
    // }


    @GetMapping("test")
    public String test2(String id) {
        Map<String, Socket> socketClient = TcpConnect.socketClient;
        Socket user1111 = socketClient.get("user"+id);
        try {
            user1111.getOutputStream().write("hello".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user1111.getInetAddress().toString()+":"+user1111.getPort();
    }

}

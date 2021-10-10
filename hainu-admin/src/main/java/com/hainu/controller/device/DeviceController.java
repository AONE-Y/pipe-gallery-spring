package com.hainu.controller.device;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.common.constant.StaticObject;
import com.hainu.common.dto.DeviceCurrentSw;
import com.hainu.common.dto.QueryCmdDto;
import com.hainu.common.dto.QueryDeviceDto;
import com.hainu.common.guard.GuardObject;
import com.hainu.common.lang.Result;
import com.hainu.system.config.nioudp.NioUDP;
import com.hainu.system.config.tcp.TcpConnect;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.entity.DeviceRes;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeoutException;


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

    // @Autowired
    // private DeviceResService deviceResService;

    private DeviceCurrentSw deviceCurrentSwTemp;

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


    @PostMapping("getDeviceCurrent")
    public Result<?> getDeviceCurrent(@RequestBody QueryDeviceDto queryDevice) {
        QueryWrapper<DeviceCurrent> deviceCurrentQueryWrapper = new QueryWrapper<>();



        if (queryDevice.getWsName() != null && !queryDevice.getWsName().equals("")) {
            if (queryDevice.getWsName().equals("2AC1")) {
                Iterator<String> ipAddrs = NioUDP.udpClientHost.keySet().iterator();
                if (ipAddrs.hasNext()) {
                    queryDevice.setWsName(ipAddrs.next());
                }
            }
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
        deviceCurrentsInfo.forEach((deviceCurrentInfo) -> {
            long interval = Duration.between(deviceCurrentInfo.getUpdateTime(), LocalDateTime.now()).toSeconds();
            deviceCurrentInfo.setStatus(interval < 65 ? 1 : 0);
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

    @PostMapping("queryDevice")
    public Result<?> queryDevice(@RequestBody QueryCmdDto queryCmdDto) {
        if (queryCmdDto.getWsName() == null || queryCmdDto.getWsName().equals("")) {
            return new Result<>().error().put("仓端名不能为空");
        }
        if (queryCmdDto.getNode() == null || queryCmdDto.getNode().equals("")) {
            return new Result<>().error().put("节点名不能为空");
        }
        if (queryCmdDto.getOptions() == null || queryCmdDto.getOptions().isEmpty()) {
            return new Result<>().error().put("选项不能为空");
        }


        ByteBuffer bytes = ByteBuffer.allocate(100);
        bytes.put(new byte[]{(byte) 0xfe, 0x11,0x04,0x00});

        byte nodeByte = (byte) HexUtil.hexToInt(queryCmdDto.getNode().substring(2,4));
       bytes.put(nodeByte);

        ByteArrayOutputStream options = new ByteArrayOutputStream();

        queryCmdDto.getOptions().forEach((option) -> {
            options.write(StaticObject.options.get(option));
            options.write((byte) 0x11);
            options.write((byte) 0x11);
            options.write((byte) 0x11);
        });
        bytes.put(options.toByteArray());
        bytes.put((byte)0x99);
        bytes.put((byte) 0xFD);

        //tcp发送
        // Socket socket = TcpConnect.socketClient.get(queryCmdDto.getWsName());
        // System.out.println(TcpConnect.socketClient);
        // if (socket == null) {
        //     return new Result<>().error().put("设备离线或不存在此设备");
        // }
        //
        // try {
        //     bytes.flip();
        //     socket.getOutputStream().write(ByteUtils.getBytes(bytes));
        //     StaticLog.info("发送成功");
        // } catch (IOException e) {
        //return new Result<>().error().put("设备离线或不存在此设备");
        // }

        //udp发送
        DatagramChannel datagramChannel=null;
        String ip=null;
        String wsName = queryCmdDto.getWsName();
        if (!wsName.equals("2AC1")) {
            datagramChannel = NioUDP.udpClientHost.get(queryCmdDto.getWsName());
            ip=queryCmdDto.getWsName();
        }else {
            Collection<DatagramChannel> datagramChannels = NioUDP.udpClientHost.values();
            Iterator<DatagramChannel> iterator = datagramChannels.iterator();
            if (iterator.hasNext()) {
                datagramChannel = iterator.next();
            }
            Iterator<String> ipAddrs = NioUDP.udpClientHost.keySet().iterator();
            if (ipAddrs.hasNext()) {
                ip=ipAddrs.next();
            }
        }



        if (datagramChannel == null) {
            return new Result<>().error().put("设备离线或不存在此设备");
        }
        try {
            bytes.flip();
            datagramChannel.send(bytes, new InetSocketAddress(ip,1347) );
            StaticLog.info("发送成功");
        } catch (IOException e) {
            return new Result<>().error().put("设备离线或不存在此设备");
        }

        //netty tcp 发送
        // Channel channel = NettyServer.clientChannel.get(queryCmdDto.getWsName());
        // if (channel == null) {
        //     return new Result<>().error().put("设备未连接");
        // }
        // ByteBuf responseMsg = ByteBufAllocator.DEFAULT.buffer();
        // bytes.flip();
        // responseMsg.writeBytes(bytes);
        // channel.writeAndFlush(responseMsg);


        return new Result<>().success().put("操作成功");
    }


    @GetMapping("resData")
    public Result<?> getResData( String wsName, String node,String code){
        StaticObject.guardObject=new GuardObject();
        DeviceRes deviceRes = null;
        try {
            deviceRes = (DeviceRes) StaticObject.guardObject.get(3000);
        } catch (TimeoutException e) {
            StaticObject.guardObject=null;
            return new Result<>().error().put(e.getMessage());
        }



        // String codeStr = HexUtil.encodeHexStr(new byte[]{StaticObject.options.get(code)});
        // QueryWrapper<DeviceRes> deviceResQueryWrapper = new QueryWrapper<>();
        // deviceResQueryWrapper.eq(DeviceRes.COL_WS_NAME,wsName);
        // deviceResQueryWrapper.eq(DeviceRes.COL_NODE,node);
        // deviceResQueryWrapper.eq(DeviceRes.COL_CODE,codeStr);
        // deviceResQueryWrapper.orderByDesc(DeviceRes.COL_ID);


        UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
        DeviceCurrent deviceCurrent = new DeviceCurrent();

        deviceUpdate.eq("ws_name", wsName);
        deviceUpdate.eq("node", node).or().eq("node", "");
        deviceCurrent.setWsName(wsName);
        deviceCurrent.setNode(node);
        deviceCurrent.setUpdateTime(LocalDateTime.now());
        // deviceResService.remove(deviceResQueryWrapper);
        try {
            deviceCurrent = setSensor(deviceCurrent, code, deviceRes.getCodeValue());

        } catch (RuntimeException e) {
            return new Result<>().error().put(e.getMessage());
        }
        if (!deviceCurrentService.update(deviceCurrent, deviceUpdate)) {
            deviceCurrentService.save(deviceCurrent);
        }

        deviceCurrentSwTemp=null;
        StaticObject.guardObject=null;
        return new Result<>().success().put(deviceRes);
    }

    public  DeviceCurrent setSensor(DeviceCurrent deviceCurrent, String code
            , Double codeValue) throws RuntimeException{

        if (code.equals("deviceTemp")) {
            deviceCurrent.setDeviceTemp(codeValue);
        }
        if (code.equals("deviceHumi")) {
            deviceCurrent.setDeviceHumi(codeValue);
        }
        if (code.equals("deviceLlv")) {
            deviceCurrent.setDeviceLlv(codeValue);
        }
        if (code.equals("deviceGas")) {
            deviceCurrent.setDeviceGas(codeValue);
        }
        if (code.equals("deviceO2")) {
            deviceCurrent.setDeviceO2(codeValue);
        }


        Double switchValuetemp = codeValue;
        if (switchValuetemp == 1) {
            switchValuetemp = (double) -1;
        }
        if (switchValuetemp == 25.5 || switchValuetemp == 255) {
            switchValuetemp = 1.0;
        }
        int switchValue = switchValuetemp.intValue();

        if (switchValue == 1 || switchValue == 0) {
            if (deviceCurrentSwTemp.getChangeValue()!=switchValue){
                throw new RuntimeException("操作失败");
            }


            if (code.equals("deviceSmoke")) {
                deviceCurrent.setDeviceSmoke(switchValue);
            }
            if (code.equals("deviceLighting")) {
                deviceCurrent.setDeviceLighting(switchValue);
            }
            if (code.equals("deviceWaterpump")) {
                deviceCurrent.setDeviceWaterpump(switchValue);
            }
            if (code.equals("deviceFan")) {
                deviceCurrent.setDeviceFan(switchValue);
            }
            if (code.equals("deviceInfra")) {
                deviceCurrent.setDeviceInfra(switchValue);
            }
            if (code.equals("deviceGuard")) {
                deviceCurrent.setDeviceGuard(switchValue);
            }
        }


        return deviceCurrent;
    }


    // @PostMapping("stateSwitch")
    // public Result<?> stateSwitch(DeviceCurrent deviceCurrent){
    //     UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
    //     deviceUpdate.eq("ws_name",deviceCurrent.getWsName());
    //     deviceCurrentService.update(deviceCurrent,deviceUpdate);
    //     return new Result<>().success().put("操作成功");
    // }

    @PostMapping("stateSwitch")
    public Result<?> stateSwitch(@RequestBody DeviceCurrentSw deviceCurrentSw) {

        ByteBuffer bytes = ByteBuffer.allocate(100);
        bytes.put(new byte[]{(byte) 0xfe, 0x12, 0x04,0x00});
        bytes.put((byte) HexUtil.hexToInt(deviceCurrentSw.getNode().substring(2,4)));
        bytes.put(StaticObject.options.get(deviceCurrentSw.getChangeSw()));
        bytes.put((byte) 0x12);
        bytes.put(StaticObject.swChangeValue.get(deviceCurrentSw.getChangeValue()));
        bytes.put(StaticObject.swChangeValue.get(deviceCurrentSw.getChangeValue()));
        bytes.put((byte) 0x99);
        bytes.put( (byte) 0xfd);

        // //tcp发送
        // Socket socket = TcpConnect.socketClient.get(deviceCurrentSw.getWsName());
        // System.out.println(TcpConnect.socketClient);
        // if (socket == null) {
        //     return new Result<>().error().put("设备未连接");
        // }
        //
        //
        // try {
        //     bytes.flip();
        //     socket.getOutputStream().write(ByteUtils.getBytes(bytes));
        //     StaticLog.info("发送成功");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        //udp发送
        DatagramChannel datagramChannel=null;
        String ip=null;
        String wsName = deviceCurrentSw.getWsName();
        if (!wsName.equals("2AC1")) {
             datagramChannel = NioUDP.udpClientHost.get(deviceCurrentSw.getWsName());
             ip=deviceCurrentSw.getWsName();
        }else {
            Collection<DatagramChannel> datagramChannels = NioUDP.udpClientHost.values();
            Iterator<DatagramChannel> iterator = datagramChannels.iterator();
            if (iterator.hasNext()) {
                 datagramChannel = iterator.next();
            }
            Iterator<String> ipAddrs = NioUDP.udpClientHost.keySet().iterator();
            if (ipAddrs.hasNext()) {
                ip=ipAddrs.next();
            }
        }
//#############################################
        if (datagramChannel == null) {
            return new Result<>().error().put("设备未连接");
        }
        try {
            bytes.flip();
            datagramChannel.send(bytes, new InetSocketAddress(ip,1347) );
            StaticLog.info("发送成功");
        } catch (IOException e) {
            return new Result<>().error().put("设备离线或不存在此设备");
        }

        //netty tcp 发送
        // Channel channel = NettyServer.clientChannel.get(deviceCurrentSw.getWsName());
        // if (channel == null) {
        //     return new Result<>().error().put("设备未连接");
        // }
        // ByteBuf responseMsg = ByteBufAllocator.DEFAULT.buffer();
        // bytes.flip();
        // responseMsg.writeBytes(bytes);
        // channel.writeAndFlush(responseMsg);

        deviceCurrentSwTemp=deviceCurrentSw;
        // mqttPushClient.publish(1, true, "/dev/" + deviceCurrent.getWsName(), JSONUtil.toJsonStr(deviceSwitchDto));

        return new Result<>().success().put("操作成功");
    }

    public boolean saveSwitchData(){
        DeviceCurrent deviceCurrent = new DeviceCurrent();
        UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
        deviceUpdate.eq("ws_name", deviceCurrentSwTemp.getWsName());
        deviceUpdate.eq("node", deviceCurrentSwTemp.getNode());

        BeanUtil.copyProperties(deviceCurrentSwTemp, deviceCurrent, "changeSw", "changeValue");
        deviceCurrentService.update(deviceCurrent, deviceUpdate);
        deviceCurrentSwTemp=null;
        return true;
    }


    @GetMapping("test")
    public String test2(String id) {
        Map<String, Socket> socketClient = TcpConnect.socketClient;
        Socket socket = socketClient.get("127.0.0.1");
        if (socket == null) {
            return "147258";
        }


        try {
            socket.getOutputStream().write("hello".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket.getInetAddress().toString() + ":" + socket.getPort();
    }

}

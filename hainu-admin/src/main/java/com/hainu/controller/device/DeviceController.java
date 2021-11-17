package com.hainu.controller.device;

import cn.hutool.core.util.HexUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.common.constant.StaticObject;
import com.hainu.common.dto.DeviceCurrentSw;
import com.hainu.common.dto.QueryCmdDto;
import com.hainu.common.dto.QueryDeviceDto;
import com.hainu.common.dto.SensorNodeRes;
import com.hainu.common.lang.Result;
import com.hainu.common.queue.MessageQueue;
import com.hainu.system.config.netty.handle.ResponseHandler;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceRes;
import com.hainu.system.entity.NodeSensor;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.DeviceLogService;
import com.hainu.system.service.NodeSensorService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
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


    @Value("${info.baseEmqUrl}")
    private String baseEmqUrl;

    @Autowired
    private DeviceListService deviceListService;

    @Autowired
    private DeviceCurrentService deviceCurrentService;

    @Autowired
    private DeviceLogService deviceLogService;

    @Autowired
    private NodeSensorService nodeSensorService;


    private DeviceCurrentSw deviceCurrentSwTemp;


    @PostMapping("getDeviceCurrent")
    public Result<?> getDeviceCurrent(@RequestBody QueryDeviceDto queryDevice) {
        QueryWrapper<DeviceCurrent> deviceCurrentQueryWrapper = new QueryWrapper<>();
        deviceCurrentQueryWrapper.select("ws_id", "ws_name", "MAX(status) as status", "node", "MAX(device_temp) as device_temp",
                "MAX(device_humi) as device_humi", "MAX(device_O2) as device_O2", "MAX(device_gas) as device_gas",
                "MAX(device_llv) as device_llv", "MAX(device_smoke) as device_smoke", "MAX(device_infra) as device_infra",
                "MAX(device_lighting) as device_lighting", "MAX(device_waterpump) as device_waterpump",
                "MAX(device_fan) as device_fan", "MAX(device_manhole) as device_manhole",
                "MAX(update_time) as update_time");
        deviceCurrentQueryWrapper.groupBy(DeviceCurrent.COL_WS_NAME);


        if (queryDevice.getWsName() != null && !queryDevice.getWsName().equals("")) {
            if (queryDevice.getWsName().equals("2AC1")) {
                Iterator<String> ipAddrs = ResponseHandler.udpClientHost.keySet().iterator();
                if (ipAddrs.hasNext()) {
                    queryDevice.setWsName(ipAddrs.next());
                }
            }
            deviceCurrentQueryWrapper.eq(DeviceCurrent.COL_WS_NAME, queryDevice.getWsName());

        }
        if (queryDevice.getNode() != null && !queryDevice.getNode().equals("")) {
            deviceCurrentQueryWrapper.eq(DeviceCurrent.COL_NODE, queryDevice.getNode());

        }
        if (queryDevice.getSw() != null && !queryDevice.getSw().equals("all")) {
            deviceCurrentQueryWrapper.eq("device_" + queryDevice.getSw(), 1);
        }
        if (queryDevice.getMeasure() != null) {

            deviceCurrentQueryWrapper.between("device_" + queryDevice.getMeasure()
                    , queryDevice.getStart() == null ? 0 : queryDevice.getStart()
                    , queryDevice.getEnd() == null ? 999 : queryDevice.getEnd());
        }


        deviceCurrentQueryWrapper.orderByAsc(DeviceCurrent.COL_WS_NAME);


        List<DeviceCurrent> deviceCurrentsInfo = deviceCurrentService.list(deviceCurrentQueryWrapper);
        return new Result<>().success().put(deviceCurrentsInfo);
    }

    // @RequestMapping("getDeviceLog")
    // public Result<?> getDeviceLog(Integer day, String wsName, String node) {
    //     QueryWrapper<DeviceLog> deviceLogWrapper = new QueryWrapper<>();
    //     if (wsName != null) {
    //         deviceLogWrapper.eq(DeviceLog.COL_WS_NAME, wsName);
    //     }
    //     if (node != null) {
    //         deviceLogWrapper.eq(DeviceLog.COL_NODE, node);
    //     }
    //     if (wsName == null && node == null) {
    //         deviceLogWrapper = null;
    //     }
    //
    //     LocalDate date = LocalDate.now();
    //     LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
    //
    //     LocalDate lastDay = Optional.ofNullable(day)
    //             .map(e -> {
    //                 if (e < 30) {
    //                     return date.plusDays(e);
    //                 }
    //                 return date.with(TemporalAdjusters.lastDayOfMonth());
    //             })
    //             .orElseGet(() -> date.with(TemporalAdjusters.lastDayOfMonth()));
    //
    //     List<DeviceLog> deviceLogs = deviceLogService.selectByAvg(firstDay, lastDay, wsName, node);
    //     return new Result<>().success().put(deviceLogs);
    // }


    @PostMapping("queryDevice")
    public Result<?> queryDevice(@RequestBody QueryCmdDto queryCmdDto) {
        if (queryCmdDto.getWsName() == null || queryCmdDto.getWsName().equals("")) {
            return new Result<>().error().put("仓端名不能为空");
        }
        if (queryCmdDto.getNode() == null || queryCmdDto.getNode().equals("")) {
            return new Result<>().error().put("节点名不能为空");
        }
        if (queryCmdDto.getOption() == null || queryCmdDto.getOption().equals("")) {
            return new Result<>().error().put("选项不能为空");
        }


        //netty udp
        ChannelHandlerContext ctx = null;
        InetSocketAddress inetSocketAddress = null;
        String wsName = queryCmdDto.getWsName();
        if (!wsName.equals("2AC1")) {
            ctx = ResponseHandler.udpClientHost.get(wsName);
            inetSocketAddress = ResponseHandler.udpClientInet.get(wsName);
        } else {
            Collection<ChannelHandlerContext> ctxs = ResponseHandler.udpClientHost.values();
            Iterator<ChannelHandlerContext> iterator = ctxs.iterator();
            if (iterator.hasNext()) {
                ctx = iterator.next();
            }
            Iterator<InetSocketAddress> inetIterator = ResponseHandler.udpClientInet.values().iterator();
            if (inetIterator.hasNext()) {
                inetSocketAddress = inetIterator.next();
            }
        }
//#############################################
        if (ctx == null) {
            return new Result<>().error().put("设备未连接");
        }


        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(new byte[]{(byte) 0xfe, 0x11, 0x04, 0x01});

        byte nodeByte = (byte) HexUtil.hexToInt(queryCmdDto.getNode().substring(2, 4));
        buffer.writeByte(nodeByte);

        buffer.writeByte((byte) 0x11);
        buffer.writeByte(StaticObject.getOptions().get(queryCmdDto.getOption()));
        buffer.writeByte((byte) 0x11);
        buffer.writeByte((byte) 0x11);
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Byte.toUnsignedInt(buffer.readByte());
        }
        int result = sum & 0xff;
        buffer.resetReaderIndex();
        buffer.writeByte((byte) result);
        buffer.writeByte((byte) 0xFD);
        buffer.retain(2);
        ctx.writeAndFlush(new DatagramPacket(buffer, inetSocketAddress));
        ctx.writeAndFlush(new DatagramPacket(buffer, inetSocketAddress));
        ctx.writeAndFlush(new DatagramPacket(buffer, inetSocketAddress));
        StaticLog.info("发送成功");

        return new Result<>().success().put("操作成功");
    }


    @GetMapping("resData")
    public Result<?> getResData(String wsName, String node, String code) {
        // StaticObject.guardObject=new GuardObject();
        DeviceRes deviceRes = null;
        StaticObject.setMessageQueue(new MessageQueue(1));
        try {
            // deviceRes = (DeviceRes) StaticObject.guardObject.get(3000);
            deviceRes = (DeviceRes) StaticObject.getMessageQueue().take(10000);
        } catch (TimeoutException e) {
            StaticObject.setMessageQueue(null);
            return new Result<>().error().put(e.getMessage());
        }


        UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
        DeviceCurrent deviceCurrent = new DeviceCurrent();
        if (wsName.equals("2AC1")) {
            Iterator<InetSocketAddress> inetIterator = ResponseHandler.udpClientInet.values().iterator();
            if (inetIterator.hasNext()) {
                wsName = inetIterator.next().getAddress().getHostAddress();
            }
        }
        deviceUpdate.eq(DeviceCurrent.COL_WS_NAME, wsName);
        deviceUpdate.eq(DeviceCurrent.COL_NODE, node).or().eq(DeviceCurrent.COL_NODE, "");
        deviceCurrent.setWsName(wsName);
        deviceCurrent.setNode(node);
        deviceCurrent.setUpdateTime(LocalDateTime.now());
        deviceCurrent.setStatus(1);
        // deviceResService.remove(deviceResQueryWrapper);
        try {
            deviceCurrent = setSensor(deviceCurrent, code, deviceRes.getCodeValue());

        } catch (RuntimeException e) {
            return new Result<>().error().put(e.getMessage());
        }
        if (!deviceCurrentService.update(deviceCurrent, deviceUpdate)) {
            deviceCurrentService.save(deviceCurrent);
        }

        deviceCurrentSwTemp = null;
        StaticObject.setMessageQueue(null);
        // StaticObject.guardObject=null;
        return new Result<>().success().put(deviceRes);
    }

    public DeviceCurrent setSensor(DeviceCurrent deviceCurrent, String code
            , Double codeValue) throws RuntimeException {

        if (code.equals("deviceTemp")) {
            deviceCurrent.setDeviceTemp(codeValue);
            return deviceCurrent;
        }
        if (code.equals("deviceHumi")) {
            deviceCurrent.setDeviceHumi(codeValue);
            return deviceCurrent;
        }
        if (code.equals("deviceO2")) {
            deviceCurrent.setDeviceO2(codeValue);
            return deviceCurrent;
        }
        if (code.equals("deviceGas")) {
            deviceCurrent.setDeviceGas(codeValue);
            return deviceCurrent;
        }
        if (code.equals("deviceLlv")) {
            deviceCurrent.setDeviceLlv(codeValue);
            return deviceCurrent;
        }
        if (code.equals("deviceSmoke")) {
            deviceCurrent.setDeviceSmoke(codeValue);
            return deviceCurrent;
        }
        if (code.equals("deviceInfra")) {
            deviceCurrent.setDeviceInfra(codeValue > 0 ? 1 : 0);
            return deviceCurrent;
        }


        Double switchValuetemp = codeValue;
        if (switchValuetemp == 25.5 || switchValuetemp == 255) {
            switchValuetemp = 1.0;
        }else if (switchValuetemp ==17||switchValuetemp==1.7){
            switchValuetemp = 0.0;
        }
        int switchValue = switchValuetemp.intValue();

        if (switchValue == 1 || switchValue == 0) {
            // if (deviceCurrentSwTemp.getChangeValue() != switchValue) {
            //     throw new RuntimeException("设备已打开或关闭");
            // }
            if (code.equals("deviceManhole")) {
                deviceCurrent.setDeviceManhole(switchValue);
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

        }


        return deviceCurrent;
    }


    @PostMapping("stateSwitch")
    public Result<?> stateSwitch(@RequestBody DeviceCurrentSw deviceCurrentSw) {

        if (deviceCurrentSw.getWsName() == null || deviceCurrentSw.getWsName().equals("")) {
            return new Result<>().error().put("仓端名不能为空");
        }
        if (deviceCurrentSw.getNode() == null || deviceCurrentSw.getNode().equals("")) {
            return new Result<>().error().put("节点名不能为空");
        }
        if (deviceCurrentSw.getChangeSw() == null || deviceCurrentSw.getChangeSw().isEmpty()) {
            return new Result<>().error().put("选项不能为空");
        }
        if (deviceCurrentSw.getChangeValue() == -1) {
            return new Result<>().error().put("开关命令不能为空");
        }

        //netty udp
        ChannelHandlerContext ctx = null;
        InetSocketAddress inetSocketAddress = null;
        String wsName = deviceCurrentSw.getWsName();

        if (!wsName.equals("2AC1")) {
            ctx = ResponseHandler.udpClientHost.get(wsName);
            inetSocketAddress = ResponseHandler.udpClientInet.get(wsName);
        } else {
            Collection<ChannelHandlerContext> ctxs = ResponseHandler.udpClientHost.values();
            Iterator<ChannelHandlerContext> iterator = ctxs.iterator();
            if (iterator.hasNext()) {
                ctx = iterator.next();
            }
            Iterator<InetSocketAddress> inetIterator = ResponseHandler.udpClientInet.values().iterator();
            if (inetIterator.hasNext()) {
                inetSocketAddress = inetIterator.next();
            }
        }
//#############################################
        if (ctx == null) {
            return new Result<>().error().put("设备未连接");
        }


        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(new byte[]{(byte) 0xfe, 0x12, 0x04, 0x01});
        buffer.writeByte((byte) HexUtil.hexToInt(deviceCurrentSw.getNode().substring(2, 4)));
        buffer.writeByte((byte) 0x12);
        buffer.writeByte(StaticObject.getOptions().get(deviceCurrentSw.getChangeSw()));

        buffer.writeByte(StaticObject.getSwChangeValue().get(deviceCurrentSw.getChangeValue()));
        buffer.writeByte(StaticObject.getSwChangeValue().get(deviceCurrentSw.getChangeValue()));
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Byte.toUnsignedInt(buffer.readByte());
        }
        int result = sum & 0xff;
        buffer.resetReaderIndex();
        buffer.writeByte((byte) result);
        buffer.writeByte((byte) 0xfd);

        buffer.retain(2);
        ctx.writeAndFlush(new DatagramPacket(buffer, inetSocketAddress));
        ctx.writeAndFlush(new DatagramPacket(buffer, inetSocketAddress));
        ctx.writeAndFlush(new DatagramPacket(buffer, inetSocketAddress));
        StaticLog.info("发送成功");

        deviceCurrentSwTemp = deviceCurrentSw;

        return new Result<>().success().put("操作成功");
    }


    @GetMapping("nodeSensor")
    public Result<?> getNodeSensors() {
        List<NodeSensor> list = nodeSensorService.list();
        ArrayList<SensorNodeRes> sensorNodeRes = new ArrayList<>();
        list.forEach((e) -> {
            String sensor = e.getSensor();
            String[] array = sensor.substring(1, sensor.length() - 1).replace(" ", "").split(",");
            sensorNodeRes.add(new SensorNodeRes(e.getNode(), Arrays.asList(array)));
        });
        return new Result<>().success().put(sensorNodeRes);
    }

    @PostMapping("nodeSensor")
    public Result<?> addOrUpdateNodeSensor(@RequestBody SensorNodeRes sensorNodeRes) {
        NodeSensor nodeSensor = new NodeSensor();
        nodeSensor.setNode(sensorNodeRes.getNode());
        nodeSensor.setSensor(sensorNodeRes.getSensors().toString());
        QueryWrapper<NodeSensor> nodeSensorQueryWrapper = new QueryWrapper<>();
        nodeSensorQueryWrapper.eq(NodeSensor.COL_NODE, nodeSensor.getNode());
        if (!nodeSensorService.update(nodeSensor, nodeSensorQueryWrapper)) {
            nodeSensorService.save(nodeSensor);
        }
        return new Result<>().success().put("操作成功");
    }

    @GetMapping("deleteNodeSensor")
    public Result<?> deleteNodeSensor(@RequestParam(name = "node") String node) {
        UpdateWrapper<NodeSensor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(NodeSensor.COL_NODE, node);
        if (nodeSensorService.remove(updateWrapper)) {
            return new Result<>().success().put("操作成功！");
        }
        return new Result<>().success().put("操作失败！");
    }

}

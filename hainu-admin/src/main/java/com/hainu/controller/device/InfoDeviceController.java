package com.hainu.controller.device;

import cn.hutool.core.util.HexUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hainu.common.constant.DeviceConst;
import com.hainu.common.constant.StaticObject;
import com.hainu.common.lang.Result;
import com.hainu.system.config.netty2.handler.ResponseHandler;
import com.hainu.system.dto.DeviceBaseDto;
import com.hainu.system.dto.DeviceInfoDto;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceData;
import com.hainu.system.entity.DeviceInfo;
import com.hainu.system.entity.DeviceRes;
import com.hainu.system.service.DeviceDataService;
import com.hainu.system.service.DeviceInfoService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.controller.device
 * @Date：2021/11/20 20:48
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@RestController
@RequestMapping("/device/info")
public class InfoDeviceController {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private DeviceDataService deviceDataService;

    @GetMapping("/get")
    public Result<?> getDeviceData(@RequestParam(value = "wsName",required = false) String wsName) {
        if (wsName == null||wsName.equals(StringUtils.EMPTY)){
            return new Result<>().success().put(null);
        }
        String tempWsName=wsName;
        if (wsName.equals(DeviceConst.DEFAULT_WS_NAME)) {
            wsName= ResponseHandler.firstIp;
        }


        List<DeviceData> deviceData = deviceDataService.getDeviceData(wsName);
        Map<Integer, List<DeviceData>> groupData = deviceData.stream()
                .collect(Collectors.groupingBy(DeviceData::getType));
        DeviceInfoDto deviceInfoDto = DeviceInfoDto.builder()
                .wsName(tempWsName).queries(groupData.get(DeviceInfo.querySensor))
                .cmds(groupData.get(DeviceInfo.cmdSensor)).build();
        return new Result<>().success().put(deviceInfoDto);
    }

    @GetMapping("/getSensor")
    public Result<?>  getSensor(int type){

        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
        if (type == DeviceInfo.cmdSensor) {
            queryWrapper.eq(DeviceInfo.COL_TYPE,DeviceInfo.cmdSensor);
        }else{
            queryWrapper.eq(DeviceInfo.COL_TYPE,DeviceInfo.querySensor);
        }
        queryWrapper.orderByDesc(DeviceInfo.COL_WEIGHT);

        List<DeviceInfo> deviceInfos = deviceInfoService.list(queryWrapper);
        return new Result<>().success().put(deviceInfos);
    }

    @PostMapping("/addSensor")
    public Result<?>  addSensor(DeviceInfo deviceInfo){

        UpdateWrapper<DeviceInfo> updateWrapper = new UpdateWrapper<>();
       updateWrapper.eq(DeviceInfo.COL_NODE,deviceInfo.getNode())
               .eq(DeviceInfo.COL_CODE,deviceInfo.getCode());
       if (!deviceInfoService.update(deviceInfo,updateWrapper)){
           deviceInfoService.save(deviceInfo);
       }
        return new Result<>().success();
    }

    @GetMapping("/deleteSensor")
    public Result<?>  deleteSensor(String node,String code){

        UpdateWrapper<DeviceInfo> deleteWrapper = new UpdateWrapper<>();
        deleteWrapper.eq(DeviceInfo.COL_NODE,node)
                .eq(DeviceInfo.COL_CODE,code);
        if (deviceInfoService.remove(deleteWrapper)) {
            return new Result<>().success();
        }else {
            return new Result<>().error();
        }

    }


//============================================================================================

    @PostMapping("queryDevice")
    public Result<?> queryDevice(@RequestBody DeviceBaseDto deviceBaseDto) {
        if (deviceBaseDto.getWsName() == null || deviceBaseDto.getWsName().equals("")) {
            return new Result<>().error("仓端名不能为空");
        }
        if (deviceBaseDto.getNode() == null || deviceBaseDto.getNode().equals("")) {
            return new Result<>().error("节点名不能为空");
        }
        int type = deviceBaseDto.getType();
        if (type==DeviceInfo.cmdSensor){
            if (deviceBaseDto.getOnOrOff() == null || deviceBaseDto.getOnOrOff().equals("")) {
                return new Result<>().error("开关选项不能为空");
            }
        }


        //netty udp
        ChannelHandlerContext ctx;
        InetSocketAddress inetSocketAddress;
        String wsName = deviceBaseDto.getWsName();
        if (wsName.equals(DeviceConst.DEFAULT_WS_NAME)) {
            wsName = ResponseHandler.firstIp;
        }
        ctx = ResponseHandler.udpClientHost.get(wsName);
        inetSocketAddress = ResponseHandler.udpClientInet.get(wsName);
//#############################################
        if (ctx == null) {
            return new Result<>().error().put("设备未连接");
        }
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        if (type==DeviceInfo.querySensor){
            buffer.writeBytes(new byte[]{(byte) 0xfe, 0x11, 0x04});
        }else if (type == DeviceInfo.cmdSensor) {
            buffer.writeBytes(new byte[]{(byte) 0xfe, 0x12, 0x04});
        }

        byte nodeByte1 = (byte) HexUtil.hexToInt(deviceBaseDto.getNode().substring(1, 2));
        buffer.writeByte(nodeByte1);
        byte nodeByte2 = (byte) HexUtil.hexToInt(deviceBaseDto.getNode().substring(2, 4));
        buffer.writeByte(nodeByte2);
        if (type==DeviceInfo.querySensor) {
            buffer.writeByte((byte) 0x11);
        }else if (type == DeviceInfo.cmdSensor) {
            buffer.writeByte(0x12);
        }

        buffer.writeByte(HexUtil.hexToInt(deviceBaseDto.getCode()));

        if (type==DeviceInfo.querySensor) {
            buffer.writeByte((byte) 0x11);
            buffer.writeByte((byte) 0x11);
        }else if (type == DeviceInfo.cmdSensor) {
            if (deviceBaseDto.getOnOrOff().equals("1")) {
                buffer.writeByte(DeviceConst.TO_CMD_ON);
                buffer.writeByte(DeviceConst.TO_CMD_ON);
            }else if (deviceBaseDto.getOnOrOff().equals("0")){
                buffer.writeByte(DeviceConst.TO_CMD_OFF);
                buffer.writeByte(DeviceConst.TO_CMD_OFF);
            }
        }

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
        DeviceRes deviceRes;
        try {
            deviceRes = getResData(deviceBaseDto);
        } catch (TimeoutException e) {
            return new Result<>().error("操作超时");
        }
        return new Result<>().success().put(deviceRes);
    }

    public DeviceRes getResData(DeviceBaseDto deviceBaseDto)throws TimeoutException {
        DeviceRes deviceRes;
        String wsName=deviceBaseDto.getWsName();
        String node=deviceBaseDto.getNode();
        try {
            String flag = deviceBaseDto.getCode();
            deviceRes = (DeviceRes)StaticObject.getMessageQueue().take(10000, flag);
        } catch (TimeoutException e) {
            throw new TimeoutException(e.getMessage());
        }
        UpdateWrapper<DeviceData> deviceUpdate = new UpdateWrapper<>();
        DeviceData deviceData = new DeviceData();
        if (wsName.equals(DeviceConst.DEFAULT_WS_NAME)) {
            wsName = ResponseHandler.firstIp;
        }
        deviceUpdate.eq(DeviceCurrent.COL_WS_NAME, wsName);
        deviceUpdate.eq(DeviceCurrent.COL_NODE, node).or().eq(DeviceCurrent.COL_NODE, "");
        deviceData.setWsName(wsName);
        deviceData.setNode(node);
        deviceData.setCodeValue(deviceRes.getCodeValue());
        deviceData.setConnectTime(LocalDateTime.now());
        deviceData.setStatus(1);

        if (!deviceDataService.update(deviceData, deviceUpdate)) {
            deviceDataService.save(deviceData);
        }
        return deviceRes;
    }

}

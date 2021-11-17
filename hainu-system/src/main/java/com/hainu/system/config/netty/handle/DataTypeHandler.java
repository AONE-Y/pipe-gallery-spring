package com.hainu.system.config.netty.handle;

import cn.hutool.core.util.HexUtil;
import com.hainu.common.dto.DataAddrDto;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceRes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.netty.handle
 * @Date：2021/10/10 15:42
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class DataTypeHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket rec = (DatagramPacket) msg;
        String hostAddress = rec.sender().getAddress().getHostAddress();
        ByteBuf recBuf = rec.content();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("dp", rec);
        byte type = recBuf.readByte();
        //舍弃长度字节，固定为11字节
        recBuf.readByte();

        byte[] bytes = new byte[2];
        recBuf.readBytes(bytes);
        String node = HexUtil.encodeHexStr(bytes);

        //舍弃一字节
        recBuf.readByte();

        byte sensorName = recBuf.readByte();
        byte valueByte1 = recBuf.readByte();
        double sensorValue = Byte.toUnsignedInt(valueByte1);
        // Double switchValuetemp = sensorValue;
        sensorValue = sensorValue > 99 ? sensorValue / 10 : sensorValue;
        //舍弃重复字节值
        byte valueByte2 = recBuf.readByte();
        double  sensorValue2 = Byte.toUnsignedInt(valueByte2);
        sensorValue2 = sensorValue2 > 99 ? sensorValue2 / 10 : sensorValue2;

        String tempStrV = HexUtil.encodeHexStr(new byte[]{valueByte1, valueByte2});
        String tempStrV1 = HexUtil.encodeHexStr(new byte[]{valueByte1});
        if (tempStrV1.equals("ff")){
            tempStrV=HexUtil.encodeHexStr(new byte[]{valueByte2});;
        }
        double tempV = HexUtil.hexToInt(tempStrV);

        DeviceCurrent deviceCurrent = new DeviceCurrent();
        deviceCurrent.setWsName(hostAddress);
        deviceCurrent.setNode(node);
        deviceCurrent.setUpdateTime(LocalDateTime.now());

        if (type == (byte) 0x83) {

            if (sensorName == (byte) 0x01) {
                deviceCurrent.setDeviceTemp(tempV/100);
            }
            if (sensorName == (byte) 0x02) {
                deviceCurrent.setDeviceHumi(tempV / 100);
            }

            if (sensorName == (byte) 0x03) {
                deviceCurrent.setDeviceO2(tempV/10);
            }
            if (sensorName == (byte) 0x04) {
                deviceCurrent.setDeviceGas(tempV/10);
            }
            if (sensorName == (byte) 0x05) {
                double temp = Byte.toUnsignedInt(valueByte2);
                temp=temp/100;
                deviceCurrent.setDeviceLlv(tempV/100);
            }
            if (sensorName == (byte) 0x06) {
                deviceCurrent.setDeviceSmoke(tempV/10);
            }
            if (sensorName == (byte) 0xa5) {
                deviceCurrent.setDeviceInfra(sensorValue2==25.5?1:0);
            }
            objectMap.put("dc", deviceCurrent);
            super.channelRead(ctx, objectMap);
        } else if (type == (byte) 0x84) {

            int switchValue=-1;
            if (sensorValue2==17){
                switchValue=0;
            }
            if (sensorValue2==25.5){
                switchValue=1;
            }
            if (sensorName == (byte) 0xa1) {
                deviceCurrent.setDeviceManhole(switchValue);
            }
            if (sensorName == (byte) 0xa2) {
                deviceCurrent.setDeviceLighting(switchValue);
            }
            if (sensorName == (byte) 0xa4) {
                deviceCurrent.setDeviceWaterpump(switchValue);
            }
            if (sensorName == (byte) 0xa3) {
                deviceCurrent.setDeviceFan(switchValue);
            }
            objectMap.put("dc", deviceCurrent);
            super.channelRead(ctx, objectMap);
        } else if (type == (byte) 0x81 || type == (byte) 0x82) {
            DeviceRes deviceRes = new DeviceRes();
            deviceRes.setWsName(hostAddress);
            deviceRes.setNode(node);
            deviceRes.setCodeType(HexUtil.encodeHexStr(new byte[]{type}));

            deviceRes.setCode(HexUtil.encodeHexStr(new byte[]{sensorName}));
            if (sensorName == (byte) 0x01||sensorName == (byte)0x02||sensorName == (byte) 0x05) {
                deviceRes.setCodeValue(tempV/100);
            } else {
                deviceRes.setCodeValue(tempV/10);
            }

            objectMap.put("dr", deviceRes);
            super.channelRead(ctx, objectMap);
        } else {
            ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        }
    }
}


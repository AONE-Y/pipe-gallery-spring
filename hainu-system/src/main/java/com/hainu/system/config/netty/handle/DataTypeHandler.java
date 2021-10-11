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
        if (type == (byte) 0x83) {
            byte sensorName = recBuf.readByte();
            //舍弃一字节
            recBuf.readByte();
            double sensorValue = recBuf.readByte();
            //舍弃重复字节值
            recBuf.readByte();

            DeviceCurrent deviceCurrent = new DeviceCurrent();
            deviceCurrent.setWsName(hostAddress);
            deviceCurrent.setNode(node);
            deviceCurrent.setUpdateTime(LocalDateTime.now());

            if (sensorName == (byte) 0x01) {
                deviceCurrent.setDeviceTemp(sensorValue);
            }
            if (sensorName == (byte) 0x02) {
                deviceCurrent.setDeviceHumi(sensorValue);
            }
            if (sensorName == (byte) 0x03) {
                deviceCurrent.setDeviceLlv(sensorValue);
            }
            if (sensorName == (byte) 0x04) {
                deviceCurrent.setDeviceGas(sensorValue);
            }
            if (sensorName == (byte) 0x05) {
                deviceCurrent.setDeviceO2(sensorValue);
            }


            Double switchValuetemp = sensorValue;
            if (switchValuetemp == 1) {
                switchValuetemp = (double) -1;
            }
            if (switchValuetemp == 25.5 || switchValuetemp == 255) {
                switchValuetemp = 1.0;
            }
            int switchValue = switchValuetemp.intValue();

            if (switchValue == 1 || switchValue == 0) {
                if (sensorName == (byte) 0x80) {
                    deviceCurrent.setDeviceSmoke(switchValue);
                }
                if (sensorName == (byte) 0x81) {
                    deviceCurrent.setDeviceLighting(switchValue);
                }
                if (sensorName == (byte) 0x82) {
                    deviceCurrent.setDeviceWaterpump(switchValue);
                }
                if (sensorName == (byte) 0x83) {
                    deviceCurrent.setDeviceFan(switchValue);
                }
                if (sensorName == (byte) 0x84) {
                    deviceCurrent.setDeviceInfra(switchValue);
                }
                if (sensorName == (byte) 0x85) {
                    deviceCurrent.setDeviceGuard(switchValue);
                }
            }

            objectMap.put("dc", deviceCurrent);
            super.channelRead(ctx, objectMap);
        } else if (type == (byte) 0x81 || type == (byte) 0x82) {
            DeviceRes deviceRes = new DeviceRes();
            deviceRes.setWsName(hostAddress);
            deviceRes.setNode(node);
            deviceRes.setCodeType(HexUtil.encodeHexStr(new byte[]{type}));
            deviceRes.setCode(HexUtil.encodeHexStr(new byte[]{recBuf.readByte()}));
            //舍弃一字节
            recBuf.readByte();
            String codeValueStr = HexUtil.encodeHexStr(new byte[]{recBuf.readByte()});
            int codeValueTemp = HexUtil.hexToInt(codeValueStr);
            double codeValue = codeValueTemp > 99 ? (double) codeValueTemp / 10 : (double) codeValueTemp;
            deviceRes.setCodeValue(codeValue);
            objectMap.put("dr", deviceRes);
            super.channelRead(ctx, objectMap);
        } else {
            ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        }
    }
}


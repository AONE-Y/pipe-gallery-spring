package com.hainu.system.config.netty2.handler;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.common.constant.StaticObject;
import com.hainu.common.dto.DataAddrDto;
import com.hainu.system.entity.DeviceData;
import com.hainu.system.entity.DeviceRes;
import com.hainu.system.service.DeviceDataService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.StringUtil;

import java.util.Map;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.netty.handle
 * @Date：2021/10/10 16:33
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class TailAndStoreHandler extends ChannelInboundHandlerAdapter {

    private DeviceDataService deviceDataService;

    public TailAndStoreHandler(DeviceDataService deviceDataService) {
        this.deviceDataService = deviceDataService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map objectMap = (Map) msg;
        DatagramPacket rec = (DatagramPacket) objectMap.get("dp");
        DeviceData deviceData = (DeviceData) objectMap.get("dd");
        DeviceRes deviceRes = (DeviceRes) objectMap.get("dr");
        ByteBuf recBuf = rec.content();
        if (recBuf.readByte() == (byte) 0xfd) {
            if (deviceData != null) {
                deviceData.setStatus(1);
                UpdateWrapper<DeviceData> deviceUpdate = new UpdateWrapper<>();
                deviceUpdate.eq(DeviceData.COL_WS_NAME, deviceData.getWsName());
                deviceUpdate.eq(DeviceData.COL_NODE, deviceData.getNode()).or().eq(DeviceData.COL_NODE, StringUtil.EMPTY_STRING);
                if (!deviceDataService.update(deviceData, deviceUpdate)) {
                    deviceDataService.save(deviceData);
                }
            }
            if (deviceRes != null) {
                StaticObject.getMessageQueue().put(deviceRes, deviceRes.getCode());
            }
            ctx.writeAndFlush(new DataAddrDto((byte) 0x11, rec.sender()));

        } else {
            ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        }
    }
}

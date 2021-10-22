package com.hainu.system.config.netty.handle;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.common.constant.StaticObject;
import com.hainu.common.dto.DataAddrDto;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceRes;
import com.hainu.system.service.DeviceCurrentService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

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

    private DeviceCurrentService deviceCurrentService;

    public TailAndStoreHandler(DeviceCurrentService deviceCurrentService) {
        this.deviceCurrentService=deviceCurrentService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map objectMap = (Map) msg;
        DatagramPacket rec = (DatagramPacket) objectMap.get("dp");
        DeviceCurrent deviceCurrent = (DeviceCurrent) objectMap.get("dc");
        DeviceRes deviceRes = (DeviceRes) objectMap.get("dr");
        ByteBuf recBuf = rec.content();
        if (recBuf.readByte()==(byte) 0xfd) {
            if (deviceCurrent != null) {
                deviceCurrent.setStatus(1);
                UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
                deviceUpdate.eq("ws_name", deviceCurrent.getWsName());
                deviceUpdate.eq("node", deviceCurrent.getNode()).or().eq("node", "");
                if (!deviceCurrentService.update(deviceCurrent, deviceUpdate)) {
                    deviceCurrentService.save(deviceCurrent);
                }
            }
            if (deviceRes!=null){
                    StaticObject.getMessageQueue().put(deviceRes);
            }

            ctx.writeAndFlush(new DataAddrDto((byte) 0x11, rec.sender()));

        }else {
            ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        }
    }
}

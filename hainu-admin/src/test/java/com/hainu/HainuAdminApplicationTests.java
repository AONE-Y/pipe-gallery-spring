package com.hainu;


import com.hainu.system.config.netty.handle.ResponseHandler;
import com.hainu.system.dao.DeviceCmdMapper;
import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.dao.DeviceLogMapper;
import com.hainu.system.dao.DeviceQueryMapper;
import com.hainu.system.entity.DeviceCmd;
import com.hainu.system.entity.DeviceQuery;
import com.hainu.system.service.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetSocketAddress;
import java.util.List;

@SpringBootTest
class HainuAdminApplicationTests {


    @Autowired
    LoginInfoService ls;

    @Autowired
    DeviceListService dls;

    @Autowired
    DeviceListMapper dlm;

    @Autowired
    DeviceCurrentService dcs;

    @Autowired
    DeviceLogService dlogs;

    @Autowired
    DeviceLogMapper dlogm;

    @Autowired
    NodeSensorService nss;

    @Autowired
    DeviceQueryMapper deviceQueryMapper;

    @Autowired
    DeviceCmdMapper deviceCmdMapper;


    @Test
    void contextLoads() {
    }

    @Test
    void testList() throws Exception {

        ChannelHandlerContext ctx = ResponseHandler.udpClientHost.get("127.0.0.1");
        InetSocketAddress in = ResponseHandler.udpClientInet.get("127.0.0.1");
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(new byte[]{(byte) 0xfe, 0x12, 0x04, 0x01});
        ChannelFuture channelFuture = ctx.writeAndFlush(new DatagramPacket(buffer, in));
        Channel channel = channelFuture.sync().channel();

    }

    @Test
    void testDevice() {
        List<DeviceQuery> deviceQueryInfo = deviceQueryMapper.getDeviceQueryInfo(null);
        List<DeviceCmd> deviceCmdInfo = deviceCmdMapper.getDeviceCmdInfo(null);
        deviceQueryInfo.forEach(System.out::println);
        System.out.println("======================================");
        deviceCmdInfo.forEach(System.out::println);
    }


}

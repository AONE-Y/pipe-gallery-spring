package com.hainu.system.config.netty.handle;

import com.hainu.common.dto.DataAddrDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.netty.handle
 * @Date：2021/10/10 17:17
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class ResponseHandler extends ChannelOutboundHandlerAdapter {
    public static Map<String, ChannelHandlerContext> udpClientHost = new ConcurrentHashMap<>();
    public static Map<String, InetSocketAddress> udpClientInet = new ConcurrentHashMap<>();
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        DataAddrDto dataPack = (DataAddrDto) msg;

        udpClientHost.put(dataPack.getAddr().getAddress().getHostAddress(),ctx);
        udpClientInet.put(dataPack.getAddr().getAddress().getHostAddress(),dataPack.getAddr());

        ByteBuf res = ctx.alloc().buffer();
        res.writeBytes(new byte[]{(byte) 0xfa, (byte) 0x88, 0x01});
        res.writeByte((byte)dataPack.getData());
        res.writeBytes(new byte[]{(byte) 0x99, (byte) 0xfb});
        ctx.writeAndFlush(new DatagramPacket(res,dataPack.getAddr()));
    }
}

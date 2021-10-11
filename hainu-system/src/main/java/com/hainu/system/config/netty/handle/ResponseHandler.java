package com.hainu.system.config.netty.handle;

import com.hainu.common.dto.DataAddrDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;

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
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        DataAddrDto dataPack = (DataAddrDto) msg;
        ByteBuf res = ctx.alloc().buffer();
        res.writeBytes(new byte[]{(byte) 0xfe, 0x01, 0x01});
        res.writeByte((byte)dataPack.getData());
        res.writeBytes(new byte[]{(byte) 0x99, (byte) 0xfd});
        ctx.writeAndFlush(new DatagramPacket(res,dataPack.getAddr()));
        super.write(ctx, msg, promise);
    }
}

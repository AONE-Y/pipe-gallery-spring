package com.hainu.system.config.netty.handle;

import com.hainu.common.dto.DataAddrDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.nioudp.handle
 * @Date：2021/10/10 11:34
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class HeaderHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket rec = (DatagramPacket) msg;
        ByteBuf buf = rec.content();
        if (buf.readableBytes() == 11) {
            if (buf.readByte()==(byte)0xfe){
                super.channelRead(ctx, rec);
            }else {
                ctx.writeAndFlush(new DataAddrDto((byte)0xff,rec.sender()));
            }
        }else {
            ctx.writeAndFlush(new DataAddrDto((byte)0xff,rec.sender()));
        }
    }
}

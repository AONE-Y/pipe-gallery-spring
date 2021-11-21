package com.hainu.system.config.netty2.handler;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.util.Map;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.netty.handle
 * @Date：2021/10/10 16:26
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class CheckoutNumHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map objectMap = (Map) msg;
        DatagramPacket rec = (DatagramPacket) objectMap.get("dp");
        ByteBuf recBuf = rec.content();
        recBuf.resetReaderIndex();
        int sum = 0;
        int needCheckSize = recBuf.readableBytes() - 2;
        for (int i = 0; i < needCheckSize; i++) {
            sum += Byte.toUnsignedInt(recBuf.readByte());
        }
        int checkNum = HexUtil.hexToInt(HexUtil.encodeHexStr(new byte[]{recBuf.readByte()}));
        super.channelRead(ctx, objectMap);
        // int checkResult=sum&0xff;
        // if (checkResult  == checkNum) {
        //     super.channelRead(ctx, objectMap);
        // } else {
        //     ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        // }
    }
}

package com.hainu.system.config.netty.handle;

import cn.hutool.core.util.HexUtil;
import com.hainu.common.dto.DataAddrDto;
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
            String strNum = HexUtil.encodeHexStr(new byte[]{recBuf.readByte()});
            sum += HexUtil.hexToInt(strNum);
        }
        int checkNum = HexUtil.hexToInt(HexUtil.encodeHexStr(new byte[]{recBuf.readByte()}));

        //测试时用
        if (checkNum==153){
            super.channelRead(ctx, objectMap);
        }else {
            ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        }
        //记得删
        //
        // if (sum % 100 == checkNum) {
        //     super.channelRead(ctx, objectMap);
        // } else {
        //     ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        // }
    }
}
